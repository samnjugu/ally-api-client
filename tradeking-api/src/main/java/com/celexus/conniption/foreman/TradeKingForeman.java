/**
 * Copyright 2013 Cameron Cook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.celexus.conniption.foreman;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.celexus.conniption.foreman.util.properties.AllyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celexus.conniption.foreman.util.TradekingAPI;
import com.celexus.conniption.foreman.util.builder.APIBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * A Helper to interact with the TradeKing API
 *
 * @author cam, khoanguyen0791
 *
 */
public class TradeKingForeman  {

	private static final long serialVersionUID = 7830844282343108561L;
	private OAuth1AccessToken accessToken;
	private OAuth10aService srv;
	private Logger log = LoggerFactory.getLogger(TradeKingForeman.class);

	public TradeKingForeman() throws ForemanException {
		connect();
	}

	public TradeKingForeman(String consumerKey, String consumerSecret, String oauthToken, String oauthTokenSecret) throws ForemanException {
		connect(consumerKey, consumerSecret, oauthToken, oauthTokenSecret);
	}

	/**
	 * Make an API call. If the connection with Ally is not live then connect,
	 * construct the full-fledge URL link and make the API call.
	 * 
	 * @param b
	 *            the API call to make
	 * @return TKResponse, a response from Ally/TradeKing
	 * @throws ForemanException
	 */
	public TKResponse makeAPICall(APIBuilder b)
			throws ForemanException, InterruptedException, ExecutionException, IOException {
		log.trace("\t ... Verb:" + b.getVerb());
		log.info("\t ... Resource URL:" + b.getResourceURL());
		log.trace("\t ... Body:" + b.getBody());
		log.trace("\t ... Parameters:" + (b.getParameters().isEmpty() ? "No Params" : b.getParameters()));
		log.trace("Making an API Call");
		return sendRequest(makeRequest(b.getVerb(), b.getResourceURL(), b.getParameters(), b.getBody()));
	}

	/**
	 * Connect to Ally using the secret keys and token from properties file. This uses
	 * sribejava for OAuth and configures the TradekingAPI() passed into the
	 * constructor.
	 * 
	 * This sets the srv variable.
	 * 
	 * @throws ForemanException
	 */
	private void connect() throws ForemanException {
		log.trace("Connecting to Ally");
		srv = new ServiceBuilder(AllyProperties.CONSUMER_KEY).apiSecret(AllyProperties.CONSUMER_SECRET)
				.build(new TradekingAPI());
		log.trace("\t ... Service built!");
		accessToken = new OAuth1AccessToken(AllyProperties.OAUTH_TOKEN,AllyProperties.OAUTH_TOKEN_SECRET);
		log.trace("\t ... Access Token built!");
		log.trace("Connection Established");
	}

	/**
	 * Connect to Ally using the secret keys and token from constructor. This uses
	 * sribejava for OAuth and configures the TradekingAPI() passed into the
	 * constructor.
	 *
	 * This sets the srv variable.
	 *
	 * @throws ForemanException
	 */
	private void connect(String consumerKey, String consumerSecret, String oauthToken, String oauthTokenSecret) throws ForemanException {
		log.trace("Connecting to Ally");
		srv = new ServiceBuilder(consumerKey).apiSecret(consumerSecret).build(new TradekingAPI());
		log.trace("\t ... Service built!");
		accessToken = new OAuth1AccessToken(oauthToken,oauthTokenSecret);
		log.trace("\t ... Access Token built!");
		log.trace("Connection Established");
	}

	/**
	 * Make sure connect() got the authorization.
	 * 
	 * @return
	 */
	public boolean hasOAuth() {
		return srv != null;
	}

	/**
	 * Make sure accessToken exists.
	 * 
	 * @return
	 */
	public boolean hasAccessToken() {
		return accessToken != null;
	}

	public boolean isConnected() {
		return hasOAuth() && hasAccessToken();
	}

	/**
	 * Create an OAuthRequest using verb (GET, POST), a base URL, a Map of param and
	 * payload (only used in making buy/sell order). Then sign the completed URL
	 * request with accessToken credentials. The constructor is deprecated. It only
	 * takes verb and resourceURL and not an OAuth10aService. The current code
	 * appears to work.
	 * 
	 * @param verb
	 *            usually GET or POST
	 * @param resourceURL
	 *            the base URL. This can be found on Ally's website
	 * @param parameters
	 *            map of stock symbols chained together and other filters.
	 * @param payload
	 *            only used when making buy/sell order.
	 * @return an OAuthRequest that contains the credentials and the full-fledged
	 *         URL.
	 */
	private OAuthRequest makeRequest(Verb verb, String resourceURL, Map<String, String> parameters, String payload)
			throws ForemanException {

		OAuthRequest request = new OAuthRequest(verb, resourceURL);
		for (Entry<String, String> entry : parameters.entrySet()) {
			request.addBodyParameter(entry.getKey(), entry.getValue());
		}
		if (payload != null) {
			request.addHeader("Content-Length", Integer.toString(payload.length()));
			request.addHeader("Content-Type", "text/xml");
			request.setPayload(payload);
		}
		srv.signRequest(accessToken, request);
		
		// TODO delete this after done
		log.debug(request.getBodyParams().asFormUrlEncodedString());
//		System.out.println(request.getBodyParams().asOauthBaseString());
//		System.out.println(request.getQueryStringParams().asFormUrlEncodedString());
//		System.out.println(request.getQueryStringParams().asOauthBaseString());
//		System.out.println(request.getCharset());
//		System.out.println(request.getHeaders().toString());
//		System.out.println(request.getOauthParameters().toString());
//		System.out.println(request.getCompleteUrl());
		return request;
	}

	/**
	 * Send a request to Ally and get a Response object back. The old sribejava
	 * dependency uses request.send(). Unfortunately, this method is removed.
	 * OAuthRequest now returns OAuth1RequestToken instead of a Response based
	 * object.
	 * 
	 * Change to using OAuthService execute() to get response. Tested and work with
	 * getting quotes.
	 *
	 * Auth exceptions return a null response object
	 * 
	 * @param request
	 *            an OAuthRequest (OAuth 1) request
	 * @return a Response based object.
	 */
	private TKResponse sendRequest(OAuthRequest request) throws InterruptedException, ExecutionException, IOException {
		TKResponse response = new TKResponse(srv.execute(request));
		return response;
	}
}

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
package com.celexus.conniption.foreman.util;

import java.io.Serializable;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * This class is required for Scribe OAuth.
 *
 * @author cam
 *
 */
public class TradekingAPI extends DefaultApi10a implements Serializable {

    private static final long serialVersionUID = 4355447429052389540L;
    private static final String REQUEST_TOKEN_RESOURCE = "https://developers.tradeking.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "https://developers.tradeking.com/oauth/access_token";
    private static final String AUTHORIZE_URL = "https://developers.tradeking.com/oauth/authorize?oauth_token=%s";
    

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

	@Override
	protected String getAuthorizationBaseUrl() {
		return AUTHORIZE_URL;
	}
}

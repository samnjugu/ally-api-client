package com.celexus.conniption.foreman.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.oauth.ConsumerKey;
import org.asynchttpclient.oauth.OAuthSignatureCalculator;
import org.asynchttpclient.oauth.RequestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celexus.conniption.foreman.ForemanConstants;
import com.celexus.conniption.foreman.util.APICall;
import com.celexus.conniption.foreman.enums.ResponseFormat;
import com.celexus.conniption.model.quotes.Quote;
import com.celexus.conniption.model.util.JAXBUtils;

import io.netty.handler.codec.http.HttpHeaders;

/**
 * A representation of TradeKing's Market Quote that permits streaming. Tried to
 * update this to org.asynchttpclient v2.4.7 and netty 4.1. AsyncHttpClient
 * became a public interface so I have to use DefaultAsyncHttpClient. <br>
 * setExecutorService() to create thread pool is removed.
 * 
 * <h1Streaming will not work after hours because market is closed. Don't bother
 * using it when market is close.</h1>
 * 
 * @author xgp (rewritten with Ning AsyncHttpClient)
 */
public class StreamingQuote {
	static private final Logger log = LoggerFactory.getLogger(StreamingQuote.class);

	private final AsyncHttpClient client;

	public StreamingQuote() {
		DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
				.setConnectTimeout(60 * 60 * 1000).setRequestTimeout(60 * 60 * 1000).setMaxConnectionsPerHost(500);
		// .setExecutorService(
		// Executors
		// .newCachedThreadPool()); //TODO dangerous. 250 in cam's code
		this.client = new DefaultAsyncHttpClient(builder.build());
	}

	/**
	 * Convert the array of symbols into comma delimited string. The result is added
	 * to the URL.
	 * 
	 * @param symbols
	 *            String[] of symbols
	 * @return a partial URL that includes the list of symbols.
	 */
	private String getParameters(String[] symbols) {
		StringBuilder sb = new StringBuilder("?symbols=");
		for (String sym : symbols) {
			sb.append(sym.toString().replace("^", "%5E")).append(",");
		}
		return sb.toString().replaceAll(",$", "");
	}

	/**
	 * Call this function to stream stock data.
	 * 
	 * @param <T>
	 * 
	 * @param handler
	 * @param symbols
	 *            List of stock symbols. Maximum is 256 symbols.
	 * @return
	 */
	public ListenableFuture<List<Quote>> stream(final StreamHandler<Quote> handler, String... symbols) {
		ConsumerKey consumer = new ConsumerKey(ForemanConstants.CONSUMER_KEY.toString(),
				ForemanConstants.CONSUMER_SECRET.toString());
		RequestToken user = new RequestToken(ForemanConstants.OAUTH_TOKEN.toString(),
				ForemanConstants.OAUTH_TOKEN_SECRET.toString());
		OAuthSignatureCalculator calc = new OAuthSignatureCalculator(consumer, user);

		ListenableFuture<List<Quote>> response = client
				.prepareGet(APICall.getStreamingQuote(ResponseFormat.XML) + getParameters(symbols))
				.setSignatureCalculator(calc).execute(new AsyncHandler<List<Quote>>() {
					private List<Quote> quotes = new ArrayList<Quote>();

					@Override
					public AsyncHandler.State onStatusReceived(HttpResponseStatus s) throws Exception {

						if (s.getStatusCode() > 400) {
							log.error(s.getStatusCode() + " " + s.getStatusText());
							return AsyncHandler.State.ABORT;
						} else
							log.info(s.getStatusCode() + " " + s.getStatusText());
						return AsyncHandler.State.CONTINUE;
					}

					@Override
					public State onHeadersReceived(HttpHeaders headers) throws Exception {
						log.debug(headers.toString());
						return AsyncHandler.State.CONTINUE;
					}

					/**
					 * Three kinds of HttpResponseBodyParts response: <br>
					 * - <status>connected</status> : this means it's connect. Send continue
					 * signal.<br>
					 * - <quote> ... </quote> : this is the quote for each symbol. Same structure as
					 * regular quote but fewer fields.<br>
					 * - <trade> ... </trade> : this is given when a trade is placed. Similar
					 * structure as regular quote, but with a <cvol> field. This is fixed by adding
					 * <cvol> to the quotes.xsd schema.
					 * <p>
					 * 
					 * For the third case, this method will replace "trade" with "quote". The
					 * unmarshal operation uses the quotes.xsd schema (the same schema for single
					 * quote request)
					 * <p>
					 * For multiple symbol streaming, sometime the quotes and trades will come in a
					 * block. The method will split it into an ArrayList of string and unmarshal one
					 * at a time.
					 * <p>
					 * The body response also has invisible characters and spaces and need to be
					 * removed using regex in JABUtils.getElement() method.
					 */
					@Override
					public AsyncHandler.State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
						String q = new String(bodyPart.getBodyPartBytes());

						log.info("onBodyPartReceived {}", q);

						if (q.equals("<status>connected</status>")) {
							log.info(q);
							return AsyncHandler.State.CONTINUE;
						}

						// if it's a <trade> response, replace "trade" with "quote". Not perfect, still
						// throw JAXB errors in some responses.
						String replacedTag = q.replaceAll("trade>", "quote>").replace(">na<", ">0<");

						// Split string right between </quote> and <quote> without consuming the tag.
						List<String> list = new ArrayList<String>(Arrays.asList(replacedTag.split("(?=(<quote>))")));

						for (String s : list) {
							try {

								// this split handles a block XML response with no root element. Also works for
								// a single response.
								JAXBUtils.getElement("com.celexus.conniption.model.quotes", s.trim(), null,
										Quote.class);

								// handler.handle(quote);

							} catch (Exception e) {
								// If JAXBUtils throw error, just continue processing. Might lead to
								// stackoverflow error.
								log.error("Error in JAXB" + e.getMessage() + replacedTag);
								return AsyncHandler.State.CONTINUE;
							}

							Quote quote = JAXBUtils.getElement("com.celexus.conniption.model.quotes", s, null,
									Quote.class);
							handler.handle(quote);
							quotes.add(quote);
						}

						return AsyncHandler.State.CONTINUE;

					}

					@Override
					public List<Quote> onCompleted() throws Exception {
						return quotes;
					}

					@Override
					public void onThrowable(Throwable t) {
						log.warn("onThrowable", t);
					}

				});
		return response;
	}

}

package com.celexus.conniption.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.celexus.conniption.foreman.enums.AccountHistoryField;
import com.celexus.conniption.model.accounts.AccountHoldingsResponse;
import com.celexus.conniption.model.accounts.HistoryResponse;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.celexus.conniption.foreman.TKResponse;
import com.celexus.conniption.foreman.TradeKingForeman;
import com.celexus.conniption.foreman.stream.StreamHandler;
import com.celexus.conniption.foreman.stream.StreamingQuote;
import com.celexus.conniption.foreman.enums.ResponseFormat;
import com.celexus.conniption.foreman.util.builder.APIBuilder;
import com.celexus.conniption.foreman.util.builder.AccountsBuilder;
import com.celexus.conniption.foreman.util.builder.MarketBuilder;
import com.celexus.conniption.foreman.util.builder.OrdersBuilder;
import com.celexus.conniption.model.accounts.AccountsResponse;
import com.celexus.conniption.model.clock.ClockResponse;
import com.celexus.conniption.model.order.OrderResponse;
import com.celexus.conniption.model.orders.OrdersResponse;
import com.celexus.conniption.model.quotes.Quote;
import com.celexus.conniption.model.quotes.QuotesResponse;
import com.celexus.conniption.model.util.JAXBUtils;

/**
 * Main entry point for use of this library. Implements just a few methods.
 * 
 * @author xgp
 */
public class TradeKing {

	private final TradeKingForeman foreman;

	public TradeKing(TradeKingForeman foreman) {
		this.foreman = foreman;
	}

	/**
	 * Build an API call, send it to Ally, get a clock response and unmarshal it to
	 * clock schema.
	 * 
	 * @return ClockResponse object. Check XSD for more info of what method is available.
	 */
	public ClockResponse clock() {
		return get(MarketBuilder.getClock(ResponseFormat.XML), null, "com.celexus.conniption.model.clock",
				ClockResponse.class);
	}

	/**
	 * Build an API call, send it to Ally, get an account response and unmarshal it
	 * to account schema.
	 * 
	 * @return AccountsResponse  object. Check XSD for more info of what method is available.
	 */
	public AccountsResponse accounts() {
		return get(AccountsBuilder.getAccounts(ResponseFormat.XML), null, "com.celexus.conniption.model.accounts",
				AccountsResponse.class);
	}

	/**
	 * Request history for all time and transaction types
	 * @param accountId  - Account Number
	 ** *
	 **/
	public HistoryResponse history(String accountId) {
		return get(AccountsBuilder.getAccountHistory(accountId,ResponseFormat.XML), null, "com.celexus.conniption.model.accounts",
				HistoryResponse.class);
	}
	/**
	 * Request history for particular range and transaction types
	 * @param accountId  - Account Number
	 * @param range - values: all, today, current_week, current_month, last_month
	 * @param transactions - values: all, bookkeeping, trade
	 * Note - Ally doesn't always filter on the passed in parameters
	 * *
	 */
	public HistoryResponse history(String accountId, AccountHistoryField range, AccountHistoryField transactions) {
		return get(AccountsBuilder.getAccountHistory(accountId,ResponseFormat.XML,range,transactions), null, "com.celexus.conniption.model.accounts",
				HistoryResponse.class);
	}

	public AccountHoldingsResponse holdings(String accountId){
		return get(AccountsBuilder.getAccountHoldings(accountId,ResponseFormat.XML),null,"com.celexus.conniption.model.accounts",
				AccountHoldingsResponse.class);
	}


	/**
	 * Preview response and unmarshal it to fixml schema.
	 * 
	 * @param accountId
	 * @param fixml
	 * @return OrderResponse  object. Check XSD for more info of what method is available.
	 */
	public OrderResponse preview(String accountId, String fixml) {
		return get(OrdersBuilder.preview(accountId, fixml, ResponseFormat.XML), null,
				"com.celexus.conniption.model.order", OrderResponse.class);
	}

	/**
	 * Build an API call, send it to Ally, get order response (not a preview and
	 * unmarshal it to fixml schema.
	 * 
	 * @param accountId
	 * @param fixml
	 * @return OrderResponse object  object. Check XSD for more info of what method is available.
	 */
	public OrderResponse order(String accountId, String fixml) {
		return get(OrdersBuilder.postOrder(accountId, fixml, ResponseFormat.XML), null,
				"com.celexus.conniption.model.order", OrderResponse.class);
	}

	/**
	 * Build an API call, send it to Ally, get all orders from an account and
	 * unmarshal them to orders schema.
	 * 
	 * @param accountId
	 * @return OrdersResponse object, unmarshalled to XSD. 
	 */
	public OrdersResponse orders(String accountId) {
		return get(OrdersBuilder.getOrders(accountId, ResponseFormat.XML), null, "com.celexus.conniption.model.orders",
				OrdersResponse.class);
	}

	/**
	 * Make APICall for options of a certain stock. Options response has the same
	 * structure as QuotesResponse.
	 * 
	 * @param symbol
	 *            Stock symbol to look up option.
	 * @param query
	 *            a string of query
	 * @param fields
	 *            (optional) to limit the result to certain tags to reduce data
	 *            usage.
	 * @return an object that contains a list of Quotes.
	 */
	public QuotesResponse options(String symbol, List<String[]> query, String[] fields) {
		return get(MarketBuilder.getOptions(ResponseFormat.XML, symbol, query, fields), null,
				"com.celexus.conniption.model.quotes", QuotesResponse.class);
	}

	/**
	 * Make APICall for a list of stock quotes and unmarshal them to QuotesResponse
	 * schema.
	 * 
	 * @param symbols
	 *            list of stock symbols
	 * @return QuotesResponse object. Check XSD for more info of what method is available.
	 */
	public QuotesResponse quotes(String[] symbols, String... fields) {
		return get(MarketBuilder.getQuotes(ResponseFormat.XML, symbols, fields), null,
				"com.celexus.conniption.model.quotes", QuotesResponse.class);
	}

	/**
	 * Use the given APIBuilder to make API call and convert the response to string.
	 * Then, replace all data fields of type na e.g. "&lt;type>na&lt;/type>" with
	 * "". (because the unmarshaller cannot reconcile na and XML data type. Then,
	 * pass it into the unmarshaller.
	 * 
	 * @param builder
	 *            APIBuilder that contains all elements of an http url.
	 * @param root
	 * @param packageName
	 *            package name to construct JAXBContext instance. Can use clazz
	 *            instead.
	 * @param clazz
	 *            name of class to unmarshal, depends on what kind of call you are
	 *            using.
	 * @return an object of a given type that has data unmarshalled into XSD schema.
	 */
	private <T> T get(APIBuilder builder, String root, String packageName, Class<T> clazz) {
		try {
			TKResponse response = foreman.makeAPICall(builder);
//			System.out.println(response);
			// replace na data here
			String temp = response.toString().replace(">na<", ">0<");
			return JAXBUtils.getElement(packageName, temp, root, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Future<List<Quote>> streamQuotes(StreamHandler<Quote> handler, String... symbols) {
		return new StreamingQuote().stream(handler, symbols);
	}

	static public void main(String[] argv) throws Exception {

		String[] symbols = { "BAC", "AAPL", "AMAT", "F", "M", "MSFT", "CRM", "FSLR" };
		Arrays.sort(symbols);
		String[] fids = { "symbol", "ask", "bid" };

		//get clock
		TradeKing tk = new TradeKing(new TradeKingForeman());
		ClockResponse c = tk.clock();
		System.out.println(c.getElapsedtime());

		QuotesResponse q = tk.quotes(symbols, fids);
		for (Quote qo : q.getQuotes().getQuote()) {
			System.out.println(qo.getSymbol() + " " + qo.getAsk());

		}

		List<String[]> queries = new ArrayList<String[]>();
		queries.add(new String[] { "strikeprice", "<", "125" });
		queries.add(new String[] { "put_call", "eq", "put" });

		//option search use case
		String symbol = "NXPI";
		QuotesResponse quotesResponse = tk.options(symbol, queries, fids);
		for (Quote quote : quotesResponse.getQuotes().getQuote()) {
			System.out.println(quote.getSymbol() + " " + quote.getAsk());
		}

//		Future f = tk.streamQuotes(handler, "TWTR", "XIV"); wait(f, 15000L);
		
		// first use case
		Future f = tk.streamQuotes(new StreamHandler<Quote>() {
			public void handle(Quote quote) {
				if (quote.getBid() > 1) {

					System.out.println(quote.getSymbol() + " : " + quote.getBid() + "\t\t\t" + quote.getAsk());
				} else
					System.out.println(quote.getSymbol() + " : " + quote.getLast());
			}
		}, symbols);

		// second use case
		// StreamingQuote quote = new StreamingQuote();
		// Future<List<Quote>> future = quote.stream(handler, temp);
		//  wait(future, 1000L);
		// List<Quote> qList = future.get();
		// for (Quote qu : qList)
		// System.out.println(qu.getSymbol() + " : " + qu.getBid());

		/*
		 * FIXMLBuilder fixml = new FIXMLBuilder().
		 * id(ForemanConstants.TK_ACCOUNT_NO.toString())
		 * .timeInForce(TimeInForceField.DAY_ORDER) .symbol("TWTR")
		 * .priceType(PriceType.LIMIT) .securityType(SecurityType.STOCK) .quantity(1)
		 * .executionPrice(.01) .side(MarketSideField.BUY);
		 * System.err.println(fixml.build().toString()); Order o =
		 * tk.preview(ForemanConstants.TK_ACCOUNT_NO.toString(),
		 * fixml.build().toString()); log(o);
		 * 
		 * Clock c = tk.clock(); log(c);
		 * 
		 * Accounts a = tk.accounts(); log(a);
		 * 
		 * Quotes q = tk.quotes("TWTR", "XIV"); log(q);
		 * 
		 * Future f = tk.quotes(handler, "TWTR", "XIV"); wait(f, 15000L);
		 */
	}

	/**
	 * Wait function for thread before sending next request.
	 * 
	 * @param f
	 * @param millis
	 * @throws Exception
	 */
	static private void wait(Future f, long millis) throws Exception {
		Thread.sleep(millis);
		if (!f.isDone())
			f.cancel(true);
	}

	static private <T> void log(T o) {
		System.err.println(ReflectionToStringBuilder.toString(o, new RecursiveToStringStyle()));
	}

	/**
	 * Implementation of StreamHandler interface. Log the quote.
	 */
	static private final StreamHandler<Quote> handler = new StreamHandler<Quote>() {
		public void handle(Quote quote) {
			log("Start logging: ");
			log(quote);
			log("\nEnd logging");

		}
	};

}

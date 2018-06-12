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
package com.celexus.conniption.foreman.util.builder;

import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celexus.conniption.foreman.stream.StreamingQuote;
import com.celexus.conniption.foreman.util.APICall;
import com.celexus.conniption.foreman.util.ResponseFormat;
import com.celexus.conniption.foreman.util.APICall.MARKET;
import com.github.scribejava.core.model.Verb;

import io.netty.util.internal.logging.Log4J2LoggerFactory;

/**
 * Extends APIBuilder to build TradeKing Market calls.
 *
 * @author cam
 *
 */
public class MarketBuilder extends APIBuilder {

	private static final long serialVersionUID = -7542591696724178699L;
	static private final Logger log = LoggerFactory.getLogger(MarketBuilder.class);

	/**
	 * Pick an API call verb (GET or POST)
	 * 
	 * @param v
	 *            Enum for choosing GET or POST
	 */
	private MarketBuilder(Verb v) {
		verb = v;
	}

	public static MarketBuilder getClock(ResponseFormat format) {
		MarketBuilder b = new MarketBuilder(Verb.GET);
		b.resourceURL = APICall.getMarketClock(format);
		return b;
	}

	/**
	 * This processes the list of query parameters and send it to getQuote, which
	 * processes stock symbols, fids and query into parameter map.
	 * 
	 * @param xml
	 *            response format: XML, JSON or JSONP. Only XML has a parser right
	 *            now.
	 * @param symbol
	 *            one symbol for option or a list of symbols for stock quote.
	 * @param query
	 *            the list of String[] to be converted into a single string. Each
	 *            String[] has 3 elements: queryable field, operator and value
	 *            (maximum of two decimal places). Example: strikeprice-lt:200
	 * @param fields
	 *            list of fields to be requested from the server. Empty means Ally
	 *            will send everything.
	 * @return APIBuilder object, ready to be used to create OAuthRequest object.
	 */
	public static MarketBuilder getOptions(ResponseFormat format, String symbol, List<String[]> query,
			String[] fields) {

		// build query
		if (query.isEmpty()) {
			return getQuotes(format, new String[] { symbol }, null, fields);
		}

		StringBuilder httpQuery = new StringBuilder();
		for (String[] sa : query) {
			httpQuery.append(QueryableField.getQueryableField(sa[0]));
			httpQuery.append(QueryOperator.getQueryOperator(sa[1]));
			httpQuery.append(sa[2]);
			httpQuery.append("AND");
		}

		return getQuotes(format, new String[] { symbol }, httpQuery.toString(), fields);
	}

	/**
	 * Pass the params to another getQuotes method that actually concatenates the
	 * stock symbol string.
	 * 
	 * @param format
	 *            an Enum from ResponseFormat class for choosing XML, JSON or JSONP
	 * @param symbols
	 *            a list of stock symbols, maximum 256 strings. This string can also
	 *            be 3 String arrays, one for symbol, one for query and one for
	 *            fields.
	 * @return
	 */
	public static MarketBuilder getQuotes(ResponseFormat format, String[] symbols, String[] fields) {
		return getQuotes(format, symbols, null, fields);
	}

	/**
	 * Create a MarketBuilder object, choose the verb (POST), response type (default
	 * is XML for now) append the Symbol list (with keyword "symbol" for options or
	 * "symbols" for quotes). Then append the query to key-value map if applicable.
	 * Finally, append the fids to key-value map.
	 * 
	 * @param format
	 *            an Enum from ResponseFormat class for choosing XML, JSON or JSONP
	 * @param symbols
	 *            a list of stock symbols, maximum 256 strings.
	 * @param fields
	 *            an array of MarketQuotesResponseField objects (bid, ask, last,
	 *            symbol ...) this is not used right now.
	 * @return a MarketBuilder object with base URL and all filters. It only need a
	 *         token to be sent to Ally.
	 */
	public static MarketBuilder getQuotes(ResponseFormat format, String[] symbols, String query, String[] fields) { 
		MarketBuilder b = new MarketBuilder(Verb.POST);

		if (ArrayUtils.isNotEmpty(symbols)) {
			// add the symbols to a StringBuilder. Usually symbol is guaranteed to have
			// values.
			StringBuilder sb = new StringBuilder();
			for (String sym : symbols) {
				sb.append(sym + " ");
			}

			// if this is an option call, the query must not be null or Ally will give an
			// error.
			if (query != null) {
				b.resourceURL = APICall.searchOptions(format);
				b.params.put(MarketQuotesField.SYMBOL.toString(), sb.toString().trim().replace(" ", ","));
				b.params.put(MarketQuotesField.QUERY.toString(), query);
			} else {

				// if this is not an option, Ally doesn't support having a query
				b.resourceURL = APICall.getQuote(format);

				b.params.put(MarketQuotesField.SYMBOLS.toString(), sb.toString().trim().replace(" ", ","));
			}
		} else
			log.debug("No symbol provided.");

		// fids are allowed in both option and quote API calls
		StringBuilder fids = new StringBuilder();
		if (ArrayUtils.isNotEmpty(fields)) {
			for (String f : fields) {
				if (MarketQuotesResponseField.has(f)) {
					fids.append(f.toString() + " ");
				} else
					log.error("Field doesn't exist: " + f);

				b.params.put(MarketQuotesField.FIELDS.toString(), fids.toString().trim().replace(" ", ","));
			}
		} else
			log.debug("No field provided.");

		return b;
	}

	/**
	 * Fields used in limiting quote results to these fields. These come after
	 * "fields=" and are comma delimited.
	 * 
	 * @author khoa
	 *
	 */
	public enum MarketQuotesResponseField implements Serializable {
		/**
		 * adp_100 Stock, Average Daily Price - 100 day
		 */
		AVG_DAILY_PRICE_100_DAYS("adp_100"),
		/**
		 * adp_200 Stock, Average Daily Price - 200 day
		 */
		AVG_DAILY_PRICE_200_DAYS("adp_200"),
		/**
		 * adp_50 Stock, Average Daily Price - 50 day
		 */
		AVG_DAILY_PRICE_50_DAYS("adp_50"),
		/**
		 * adv_21 Stock, Average Daily Volume - 21 day
		 */
		AVG_DAILY_VOLUME_21_DAYS("adv_21"),
		/**
		 * adv_30 Stock, Average Daily Volume - 30 day
		 */
		AVG_DAILY_VOLUME_30_DAYS("adv_30"),
		/**
		 * adv_90 Stock, Average Daily Volume - 90 day
		 */
		AVG_DAILY_VOLUME_90_DAYS("adv_90"),
		/**
		 * ask Stock, Option Ask price
		 */
		ASK_PRICE("ask"),
		/**
		 * ask_time Stock, Option Time of latest ask
		 */
		ASK_TIME("ask_time"),
		/**
		 * asksz Stock, Option Size of latest ask (in 100's)
		 */
		ASK_SIZE("asksz"),
		/**
		 * basis Stock, Option Reported precision (quotation decimal places)
		 */
		PRECISION("basis"),
		/**
		 * beta Stock, Beta volatility measure
		 */
		BETA_VOLATILITY("beta"),
		/**
		 * bid Stock, Option Bid price
		 */
		BID_PRICE("bid"),
		/**
		 * bid_time Stock, Option Time of latest bid
		 */
		BID_TIME("bid_time"),
		/**
		 * bidsz Stock, Option Size of latest bid (in 100's)
		 */
		BID_SIZE("bidsz"),
		/**
		 * bidtick Stock, Tick direction since last bid
		 */
		BID_TICK_DIRECTION("bidtick"),
		/**
		 * chg Stock, Option Change since prior day close (cl)
		 */
		DAY_CHANGE("chg"),
		/**
		 * chg_sign Stock, Option Change sign (e, u, d) as even, up, down
		 */
		CHANGE_DIRECTION("chg_sign"),
		/**
		 * chg_t Stock, Option change in text format
		 */
		CHANGE_PLAIN_TEXT("chg_t"),
		/**
		 * cl Stock, Option previous close
		 */
		PREVIOUS_CLOSE("cl"),
		/**
		 * contract_size Option, contract size for option
		 */
		CONTRACT_SIZE("contract_size"),
		/**
		 * cusip Stock, Cusip
		 */
		CUSIP("cusip"),
		/**
		 * date Stock, Option Trade date of last trade
		 */
		DATE_OF_LAST_TRADE("date"),
		/**
		 * datetime Stock, Option Date and time
		 */
		DATE_TIME("datetime"),
		/**
		 * days_to_expiration Option, Days until option expiration date
		 */
		DAYS_TO_EXPIRATION("days_to_expiration"),
		/**
		 * div Stock, Latest announced cash dividend
		 */
		DIVIDEND("div"),
		/**
		 * divexdate Stock, Ex-dividend date of div(YYYYMMDD)
		 */
		DIVIDEND_EX_DATE("divexdate"),
		/**
		 * divfreq Stock, Dividend frequency, A - Annual Dividend, S - Semi Annual
		 * Dividend, Q - Quarterly Dividend, M - Monthly Dividend, N - Not applicable or
		 * No Set Dividend Frequency.
		 */
		DIVIDEND_FREQUENCY("divfreq"),
		/**
		 * divpaydt Stock, Dividend pay date of last announced div
		 */
		DIVIDEND_PAY_DATE("divpaydt"),
		/**
		 * dollar_value Stock, Option Total dollar value of shares traded today
		 */
		DOLLAR_VALUE("dollar_value"),
		/**
		 * eps Stock, Earnings per share
		 */
		EARNINGS_PER_SHARE("eps"),
		/**
		 * exch Stock, Option exchange code
		 */
		EXCHANGE_CODE("exch"),
		/**
		 * exch_desc Stock, Option exchange description
		 */
		EXCHANGE_DESCRIPTION("exch_desc"),
		/**
		 * hi Stock, Option High Trade Price for the trading day
		 */
		HIGH_TRADE_PRICE("hi"),
		/**
		 * iad Stock, Indicated annual dividend
		 */
		INDICATED_ANNUAL_DIVIDEND("iad"),
		/**
		 * idelta Option, Option risk measure of delta using implied volatility
		 */
		IMPLIED_DELTA_VOLATILITY("idelta"),
		/**
		 * igamma Option, Option risk measure of gamma using implied volatility
		 */
		IMPLIED_GAMMA_VOLATILITY("igamma"),
		/**
		 * imp_volatility Option, Implied volatility of option price based current stock
		 * price
		 */
		IMPLIED_VOLATILITY("imp_volatility"),
		/**
		 * incr_vl Stock, Option Volume of last trade
		 */
		VOLUME_LAST_TRADE("incr_vl"),
		/**
		 * irho Option, Option risk measure of rho using implied volatility
		 */
		RHO("irho"),
		/**
		 * issue_desc Option, Issue description
		 */
		ISSUE_DESCRIPTION("issue_desc"),
		/**
		 * itheta Option, Option risk measure of theta using implied volatility
		 */
		THETA_VOLATILITY("itheta"),
		/**
		 * ivega Option, Option risk measure of vega using implied volatility
		 */
		VEGA_VOLATILITY("ivega"),
		/**
		 * last Stock, Option Last trade price
		 */
		LAST_TRADE_PRICE("last"),
		/**
		 * lo Stock, Option Low Trade Price for the trading day
		 */
		LOW_TRADE_PRICE("lo"),
		/**
		 * name Stock, Option Company name
		 */
		COMPANY_NAME("name"),
		/**
		 * op_delivery Option, Option Settlement Designation – S Std N – Non Std X - NA
		 */
		OPTION_SETTLEMENT_DESIGNATION("op_delivery"),
		/**
		 * op_flag Stock, Security has options (1=Yes, 0=No).
		 */
		SECURITY_HAS_OPTIONS("op_flag"),
		/**
		 * op_style Option, Option Style – values are “A” American and “E” European
		 */
		OPTION_STYLE("op_style"),
		/**
		 * op_subclass Option, Option class (0=Standard, 1=Leap, 3=Short Term)
		 */
		OPTION_CLASS("op_subclass"),
		/**
		 * openinterest Option, Open interest of option contract
		 */
		OPEN_INTEREST("openinterest"),
		/**
		 * opn Stock, Option Open trade price
		 */
		OPEN_TRADE_PRICE("opn"),
		/**
		 * opt_val Option, Estimated Option Value – via Ju/Zhong or Black-Scholes
		 */
		ESTIMATED_OPTION_VALUE("opt_val"),
		/**
		 * pchg Stock, Option percentage change from prior day close
		 */
		PERCENT_CHANGE("pchg"),
		/**
		 * pchg_sign Stock, Option prchg sign (e, u, d) as even, up, down
		 */
		PERCENT_CHANGE_DIRECTION("pchg_sign"),
		/**
		 * pcls Stock, Option Prior day close
		 */
		PRIOR_DAY_CLOSE("pcls"),
		/**
		 * pe Stock, Price earnings ratio
		 */
		PRICE_EARNINGS_RATIO("pe"),
		/**
		 * phi Stock, Option Prior day high value
		 */
		PRIOR_DAY_HIGH("phi"),
		/**
		 * plo Stock, Option Prior day low value
		 */
		PRIOR_DAY_LOW("plo"),
		/**
		 * popn Stock, Option Prior day open
		 */
		PRIOR_DAY_OPEN("popn"),
		/**
		 * pr_adp_100 Stock, Prior Average Daily Price "100"trade days
		 */
		PRIOR_AVG_DAILY_PRICE_100_DAYS("pr_adp_100"),
		/**
		 * pr_adp_200 Stock, Prior Average Daily Price "200"trade days
		 */
		PRIOR_AVG_DAILY_PRICE_200_DAYS("pr_adp_200"),
		/**
		 * pr_adp_50 Stock, Prior Average Daily Price "50"trade days
		 */
		PRIOR_AVG_DAILY_PRICE_50_DAYS("pr_adp_50"),
		/**
		 * pr_date Stock, Option Trade Date of Prior Last
		 */
		PRIOR_DATE("pr_date"),
		/**
		 * pr_openinterest Option, Prior day's open interest
		 */
		PRIOR_DAY_OPEN_INTEREST("pr_openinterest"),
		/**
		 * prbook Stock, Book Value Price
		 */
		BOOK_VALUE_PRICE("prbook"),
		/**
		 * prchg Stock, Option Prior day change
		 */
		PRIOR_DAY_CHANGE("prchg"),
		/**
		 * prem_mult Option, Option premium multiplier
		 */
		PREMIUM_MULTIPLIER("prem_mult"),
		/**
		 * put_call Option, Option type (Put or Call)
		 */
		PUT_OR_CALL("put_call"),
		/**
		 * pvol Stock, Option Prior day total volume
		 */
		PRIOR_DAY_TOTAL_VOLUME("pvol"),
		/**
		 * qcond Option, Condition code of quote
		 */
		CONDITION_CODE("qcond"),
		/**
		 * rootsymbol Option, Option root symbol
		 */
		ROOT_SYMBOL("rootsymbol"),
		/**
		 * secclass Stock, Option Security class (0=stock, 1=option)
		 */
		SECURITY_CLASS("secclass"),
		/**
		 * sesn Stock, Option Trading session as (pre, regular, &amp, post)
		 */
		TRADING_SESSION("sesn"),
		/**
		 * sho Stock, Shares Outstanding
		 */
		SHARES_OUTSTANDING("sho"),
		/**
		 * strikeprice Option, Option strike price (not extended by multiplier)
		 */
		STRIKE_PRICE("strikeprice"),
		/**
		 * symbol Stock, Option Symbol from data provider
		 */
		SYMBOL("symbol"),
		/**
		 * tcond Stock, Option Trade condition code – (H) halted or (R) resumed
		 */
		TRADE_CONDITION_CODE("tcond"),
		/**
		 * timestamp Stock, Option Timestamp
		 */
		TIMESTAMP("timestamp"),
		/**
		 * tr_num Stock, Option Number of trades since market open
		 */
		NUMBER_OF_TRADES("tr_num"),
		/**
		 * tradetick Stock, Option Tick direction from prior trade – (e,u,d) even, up,
		 * down)
		 */
		TICK_DIRECTION("tradetick"),
		/**
		 * trend Stock, Option Trend based on 10 prior ticks (e,u,d) even, up, down
		 */
		TICK_TREND("trend"),
		/**
		 * under_cusip Option, An option's underlying cusip
		 */
		UDERLYING_CUSIP("under_cusip"),
		/**
		 * undersymbol Option, An option's underlying symbol
		 */
		UNDERLYING_SYMBOL("undersymbol"),
		/**
		 * vl Stock, Option Cumulative volume
		 */
		CULUMLATIVE_VOLUME("vl"),
		/**
		 * volatility12 Stock, one year volatility measure
		 */
		ONE_YEAR_VOLATILITY("volatility12"),
		/**
		 * vwap Stock, Option Volume weighted average price
		 */
		VOLUME_WEIGHT_AVG_PRICE("vwap"),
		/**
		 * wk52hidate Stock, Option 52 week high date
		 */
		YEAR_HIGH("wk52hi"),
		/**
		 * wk52lo Stock, Option 52 week low
		 */
		YEAR_HIGH_DATE("wk52hidate"),
		/**
		 * wk52lodate Stock, Option 52 week low date
		 */
		YEAR_LOW("wk52lo"),
		/**
		 * wk52lodate Stock, Option 52 week low date
		 */
		YEAR_LOW_DATE("wk52lodate"),
		/**
		 * xdate Option, Expiration date of option in the format of (YYYYMMDD)
		 */
		FORMATTED_OPTION_EXPIRATION("xdate"),
		/**
		 * xday Option, Expiration day of option
		 */
		OPTION_DAY_EXPIRATION("xday"),
		/**
		 * xmonth Option, Expiration month of option
		 */
		OPTION_MONTH_EXPIRATION("xmonth"),
		/**
		 * xyear Option, Expiration year of option
		 */
		OPTION_YEAR_EXPIRATION("xyear"),
		/**
		 * yield Stock, Dividend yield as %
		 */
		DIVIDEND_YEILD("yield"), ERROR("error") {
			@Override
			public String toString() {
				return "//error";
			}
		};

		private final String field;

		private static final Map<String, MarketQuotesResponseField> fieldMap = new HashMap<String, MarketQuotesResponseField>();
		static {
			for (MarketQuotesResponseField m : MarketQuotesResponseField.values())
				fieldMap.put(m.toString(), m);
		}

		MarketQuotesResponseField(String field) {
			this.field = field;
		}

		@Override
		public String toString() {
			return field;
		}

		/**
		 * Look up the input to choose the right enum.
		 * 
		 * @param field
		 *            field to look up enum.
		 * @return
		 */
		public static MarketQuotesResponseField getFieldByName(String field) {
			return fieldMap.get(field);
		}

		public static boolean has(String field) {
			return fieldMap.containsKey(field.toLowerCase());
		}
	}

	/**
	 * Enum used in creating the URL: SYMBOLS for stock quote, SYMBOL for option
	 * quote, QUERY for option quote and FIDS to get specific fields in response
	 * (reduce data usage).
	 * 
	 * @author khoa, xgp
	 *
	 */
	public enum MarketQuotesField implements Serializable { 
		SYMBOLS("symbols"), SYMBOL("symbol"), QUERY("query"), FIELDS("fids");

		private String tag;

		/**
		 * Assign the a string of tag to the enum? The tag is a comma or space delimited
		 * string.
		 * 
		 * @param tag
		 */
		MarketQuotesField(String tag) {
			this.tag = tag;
		}

		@Override
		public String toString() { 
			return tag;
		}
	}

	/**
	 * Operators used in stock option searches. These are lt, lte, gt, gte and eq
	 * (equal). The list is here:
	 * https://www.ally.com/api/invest/documentation/market-options-search-get-post/
	 * 
	 * I used a design pattern to look up multiple possible values in an enum.
	 * 
	 * @author khoa
	 *
	 */
	public enum QueryOperator implements Serializable {
		LESS_THAN("lt", "<") {
			public String toString() {
				return "-lt:";
			}
		},
		LESS_THAN_OR_EQUAL_TO("lte", "<=", "=<") {
			public String toString() {
				return "-lte:";
			}
		},
		GREATER_THAN("gt", ">") {
			public String toString() {
				return "-gt:";
			}
		},
		GREATER_THAN_OR_EQUAL_TO("gte", ">=", "=>") {
			public String toString() {
				return "-gte:";
			}
		},
		EQUAL_TO("eq", "=") {
			public String toString() {
				return "-eq:";
			}
		};
		static private final Map<String, QueryOperator> operatorMap = new HashMap<String, QueryOperator>();
		private final List<String> operators;

		static {
			for (QueryOperator queryOperator : QueryOperator.values()) {
				operatorMap.put(queryOperator.name().toLowerCase(), queryOperator);
				for (String operator : queryOperator.operators)
					operatorMap.put(operator.toLowerCase(), queryOperator);
			}
		}

		private QueryOperator(String... operator) {
			this.operators = Arrays.asList(operator);
		}

		public static QueryOperator getQueryOperator(String operator) {
			if (operator == null)
				throw new NullPointerException("Operator is null");
			QueryOperator qOperator = operatorMap.get(operator.toLowerCase());
			if (qOperator == null)
				throw new IllegalArgumentException("Not an operator: " + operator);
			return qOperator;
		}
	}

	/**
	 * Enum for queryable fields used in stock option searches. The list is here:
	 * https://www.ally.com/api/invest/documentation/market-options-search-get-post/.
	 * Only strikeprice, xdate and put_call are usable.
	 * 
	 * @author khoa
	 *
	 */
	public enum QueryableField implements Serializable {
		STRIKEPRICE("strikeprice"), XDATE("xdate"), PUT_CALL("put_call");
		private final String queryableField;

		private QueryableField(String queryableField) {
			this.queryableField = queryableField;
		}

		@Override
		public String toString() {
			return queryableField;
		}

		public static QueryableField getQueryableField(String field) {
			for (QueryableField f : QueryableField.values()) {
				if (f.name().equalsIgnoreCase(field.toLowerCase()))
					return f;
			}
			return null;
		}
	}
}

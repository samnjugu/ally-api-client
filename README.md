
### khoanguyen0791's version

This is a fork of Garth xgp's project, which is forked from ccook's Conniption project. The goal is:

- [x] To update the dependency to latest version of scribejava. The version this project uses is 2.8 while scribejava is already 5.5.0
- [x] Update streaming to work with the new async-http-client version 2.3.0 and netty 4.1.25.Final
- [x] Make jaxb2-maven-plugin work.
- [x] Update xsd schema for various responses from Ally.
- [x] To implement option search. (high priority)
- [ ] Pull request to xgp or Ccook Conniption's project
- [ ] Explore how well the news feature works and how to search (low priority)

## Version 1.3.0
 - Implemented options search:
	 - Changed MarketQuotesResponseField, MarketQuotesField enum in MarketBuilder.java.
	 - Added QueryableFields, QueryOperator enums.
	 - Changed signature of method getQuotes.
	 - Added method getOptions.
 - Added fids (limiting search to certain fields). Useful in limiting result for options as there are a lot of options for any single stock. Fids were implemented before but it couldn't be added to URL.

**Caveats:**
 - xyear, xmonth filters are unusable although Ally says they are usable.
 - Ally says individual option query conditions must be separated by a space, but Ally gives error when I do this. This situation also shouldn't exist because the conditions are always used together.
 - Quotes from Ally has an extra line (quotetype) but it's not mentioned on their website. I updated the XSD schema.

## Version 1.2.2 SNAPSHOT

 - Fixed streaming. There are still problems with unmarshalling when responses come as a block. Maybe a thread-safe issue. However, it still delivers data most of the time.
 - Replaced "na" data with zero so there are fewer JAXB issues (no more null data).
 - Added regex to remove non-printable characters. (reduce streaming unmarshalling errors)
  
## Version 1.2.1 SNAPSHOT

 - Streaming can now print out the XML data, but still cannot unmarshal using given schema because of invisible characters.
 - Updated streaming to work with the new Async-http-client version 2.3.0 and Netty 4.1.25.Final
 - Updated streaming to handle two different types of response from Ally (`<quote>` and `<trade>` response) by combining them into one class. (They can be subclasses of a regular quote).
 - Updated XSD of quotes.xsd so that streaming `<quote>` and `<trade>` response can both use the schema. (adding `<cvol>` element)
 - Added logging.

**To-do:**
 - Trim XML response and remove whitespaces and invisible characters so JAXB unmarshaller will not throw error. There is an invisible `"."` (dot) at the beginning of the HTTP response body. A regular quote doesn't have this issue but I haven't checked it with HEX editor.
 - Update XSD for other requests to Ally and make sure they work.
 - Write some tests for streaming.
 - See what FIXML can do.

## Version 1.2
- Updated scribejava dependency to 5.5.0.
- Added system environment variable.
- Updated JAXB in POM so it works in Java 8. No guarantee it will work in Java 9 and beyond.
- Added shell script to build classes from XSD files using XJC since jaxb2-maven-plugin refuse to work. To re-enable it, just uncomment it in POM file.

**To-do:**
- Going to update streaming to work with new version of Async-http-client soon.
- Test the program with real data. Right now all tests for non-streaming pass.

### NOTICE
Fork of Ccook's [Conniption](https://github.com/Ccook/conniption) project. This is now significantly departed. We now use xjc to automatically build Java classes for the model with [XSD](https://github.com/xgp/tradeking/blob/master/tradeking-model/src/main/xsd/) built by hand from TradeKing's XML examples. Full FIXML parsing is also now supported using Java classes built from the [FIX Protocol](http://www.fixprotocol.org/) [FIXML](https://github.com/xgp/fixml) schemas.

# Tradeking
The missing TradeKing API for Java.
  
## Getting an API Key from TradeKing
*  [Visit the Ally Developers Website](https://developers.tradeking.com/applications/)
* Fill in their info and you will get 4 important values needed for [OAuth](http://oauth.net/)
* An API Key
* An API Secret Key
* An Access Token
* A Secret Access Token
*  [Go over the TradeKing API Docs](https://developers.tradeking.com/documentation/getting-started)

## Installing
Keys and tokens given to you by TradeKing can be stored as environment variables (via System.getEnv()) or as system properties (via System.getProperty()). System properties take precedence.

### Keys
 - TK_ACCOUNT_NO 
 - API_KEY 
 - API_SECRET 
 - ACCESS_TOKEN 
 - ACCESS_TOKEN_SECRET

For environment variables, put these lines in */etc/environment* or */etc/profile* (Linux) or */etc/launchd.conf* (Mac, requires restart). 
To check if the variables are loaded, use `printenv` or `env` in terminal. Sometime Java (Ubuntu 16.04 LTS) won't pick up new variables without a full shutdown and restart while Ubuntu does. Use the code below to check:

    Map<String, String> env1 = System.getenv();
    TreeMap<String,String> env = new TreeMap<String,String>(env1);
    for (String envName : env.keySet()) {
        System.out.format("%s=%s%n", envName, env.get(envName));
    }
### Building from source
- Use the maven clean install directive `mvn clean install -T 4`to compile and make sure everything works. I highly recommend you do not use `-DskipTests`. 
- The tests will not work if you do not set environment variables. The tests will check your connection to Ally.
- An example use case ( `main()` function) is in `TradeKing.java`. Run it inside `tradeking-api` folder using: 
`mvn exec:java -Dexec.mainClass="com.celexus.conniption.api.TradeKing"`
- You can also run the shell scripts in `tradeking` folder and `tradeking-api` folder
- **Streaming will not work after market closed because there is nothing to stream.**

### Maven

```xml
<dependency>
<groupId>com.github.xgp</groupId>
<artifactId>tradeking-api</artifactId>
<version>1.1.0</version>
</dependency>
```

### Usage

`TradeKing.java`: This is the main entry point to using the API. The other classes you should care about are all in [model](https://github.com/xgp/tradeking/blob/master/tradeking-model/). Usage is pretty straightforward:
```java
TradeKing  tk = new  TradeKing(new  TradeKingForeman());

// get the market clock https://developers.tradeking.com/documentation/market-clock-get
ClockResponse  c = tk.clock();

//options search is more complex because of query https://www.ally.com/api/invest/documentation/market-options-search-get-post/
String[] symbols = { "BAC", "AAPL", "AMAT", "F", "M", "MSFT", "CRM", "FSLR" };
Arrays.sort(symbols);
String[] fids = { "symbol", "ask", "bid" };
List<String[]> queries = new ArrayList<String[]>();
queries.add(new String[] { "strikeprice", "<", "125" });
queries.add(new String[] { "put_call", "eq", "put" });

//option search use case
String symbol = "NXPI";
QuotesResponse quotesResponse = tk.options(symbol, queries, fids);
for (Quote quote : quotesResponse.getQuotes().getQuote()) {
	System.out.println(quote.getSymbol() + " " + quote.getAsk());
}

// get your account https://developers.tradeking.com/documentation/accounts-get
AccountsResponse  a = tk.accounts();

// get some market quotes https://developers.tradeking.com/documentation/market-ext-quotes-get-post
QuotesResponse  q = tk.quotes("TWTR", "FB");

// stream market quotes https://developers.tradeking.com/documentation/streaming-market-quotes-get-post
Future  f = tk.quotes(new  StreamHandler<Quote>() {
public  void  handle(Quote  quote) {
System.out.println(quote.toString());
}
}, "TWTR", "FB");

// place a non-executing, preview order

FIXMLBuilder  builder = new  FIXMLBuilder()
.id(ForemanConstants.TK_ACCOUNT_NO.toString())
.timeInForce(TimeInForceField.DAY_ORDER)
.symbol("TWTR")
.priceType(PriceType.LIMIT)
.securityType(SecurityType.STOCK)
.quantity(1)
.executionPrice(18.01)
.side(MarketSideField.BUY);

OrderResponse  p = tk.preview(ForemanConstants.TK_ACCOUNT_NO, builder.build().toString());

// place a real order
OrderResponse  o = tk.order(ForemanConstants.TK_ACCOUNT_NO, builder.build().toString());

// check the status of your orders
OrdersResponse  os = tk.orders(ForemanConstants.TK_ACCOUNT_NO);
```
## Warnings
This assumes the most simplest of Accounts. Don't use if you're doing complex shit. I also haven't implemented options functionality yet. Use at your own risk.
  
## License, Attribution, etc
This is licensed under the Apache License, version 2. It is in no way associated with Ally or TradeKing Group, Inc.

Please read Ally's documentation carefully! Use only as they suggest.  


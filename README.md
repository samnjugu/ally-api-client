
  
## Maven
 
```xml
<!-- https://mvnrepository.com/artifact/com.github.khoanguyen0791/tradeking-api -->
<dependency>
	<groupId>com.github.khoanguyen0791</groupId>
	<artifactId>tradeking-api</artifactId>
	<version>1.3.1</version>
</dependency>
<dependency>
	<groupId>com.github.khoanguyen0791</groupId>
	<artifactId>ally-api-client</artifactId>
	<version>1.3.1</version>
	<type>pom</type>
</dependency>
<dependency>
	<groupId>com.github.khoanguyen0791</groupId>
	<artifactId>tradeking-model</artifactId>
	<version>1.3.1</version>
</dependency>
```

## NOTICE
I improved on top of xgp's project. Fork of Ccook's [Conniption](https://github.com/Ccook/conniption) project. This is now significantly departed. We now use xjc to automatically build Java classes for the model with [XSD](https://github.com/xgp/tradeking/blob/master/tradeking-model/src/main/xsd/) built by hand from TradeKing's XML examples. Full FIXML parsing is also now supported using Java classes built from the [FIX Protocol](http://www.fixprotocol.org/) [FIXML](https://github.com/xgp/fixml) schemas.

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
```java
Map<String, String> env1 = System.getenv();
TreeMap<String,String> env = new TreeMap<String,String>(env1);
for (String envName : env.keySet()) {
System.out.format("%s=%s%n", envName, env.get(envName));
}
```
### Building from source

- Use the maven clean install directive `mvn clean install -T 4`to compile and make sure everything works. I highly recommend you do not use `-DskipTests`.
- The tests will not work if you do not set environment variables. The tests will check your connection to Ally.
- An example use case ( `main()` function) is in `TradeKing.java`. Run it inside `tradeking-api` folder using:
`mvn exec:java -Dexec.mainClass="com.celexus.conniption.api.TradeKing"`
- You can also run the shell scripts in `tradeking` folder and `tradeking-api` folder
-  **Streaming will not work after market closes because there is nothing to stream.**

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
List<String[]> queries = new  ArrayList<String[]>();
queries.add(new  String[] { "strikeprice", "<", "125" });
queries.add(new  String[] { "put_call", "eq", "put" });

//option search use case
String  symbol = "NXPI";
QuotesResponse  quotesResponse = tk.options(symbol, queries, fids);
for (Quote quote :  quotesResponse.getQuotes().getQuote()) {
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

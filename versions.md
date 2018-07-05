
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

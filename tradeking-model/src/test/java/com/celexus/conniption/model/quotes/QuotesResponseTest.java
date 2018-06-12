package com.celexus.conniption.model.quotes;

import com.celexus.conniption.model.XmlParsingTest;
import com.celexus.conniption.model.util.JAXBUtils;
import java.io.FileInputStream;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.xml.sax.InputSource;
import com.celexus.conniption.model.quotes.*;

public class QuotesResponseTest extends XmlParsingTest {

	@Test
	public void quotesParseTest() throws Exception {
		QuotesResponse quotes = parse(source("xml/quotes.xml"), "com.celexus.conniption.model.quotes",
				QuotesResponse.class);
		assertThat(quotes, notNullValue());
		assertThat(quotes.getElapsedtime(), is(0));
		assertThat(quotes.getQuotes(), notNullValue());
		List<Quote> list = quotes.getQuotes().getQuote();
		assertThat(list.size(), is(2));
		for (Quote q : list) {
			if (q.getSymbol().equals("TWTR")) {
				assertThat(q.getBid(), is(18.56));
			}
		}
		assertThat(quotes.getError(), is("Success"));
	}

}
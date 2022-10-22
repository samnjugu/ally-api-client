package com.celexus.conniption.foreman.util.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.celexus.conniption.foreman.enums.ResponseFormat;

public class MarketBuilderTest {

	@Test
	public void getQuotesTest() {

		APIBuilder b = MarketBuilder.getQuotes(ResponseFormat.XML, new String[] { "SIRI" }, null, null);
		assertTrue(!b.getParameters().isEmpty());
		assertNotNull("Required Parameter 'symbols' not found", b.getParameters().get("symbols"));
		assertEquals("Resource URL different", "https://api.tradeking.com/v1/market/ext/quotes.xml",
				b.getResourceURL());
	}

	@Test
	public void getClockTest() {
		APIBuilder b = MarketBuilder.getClock(ResponseFormat.XML);
		assertTrue(b.getParameters().isEmpty());
		assertEquals("Resource URL Different", "https://api.tradeking.com/v1/market/clock.xml", b.getResourceURL());
	}
}

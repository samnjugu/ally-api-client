package com.celexus.conniption.model.orders;

import com.celexus.conniption.model.XmlParsingTest;
import com.celexus.conniption.model.order.OrderResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateOrderResponseTest extends XmlParsingTest {

    @Test
    public void createOrderResponseParseTest() throws Exception {
        OrderResponse resp = parse(source("xml/OrderCreateResponse.xml"),
                "com.celexus.conniption.model.orders",
                OrderResponse.class);
        assertThat(resp, notNullValue());
        assertThat(resp.getEstcommission(),is(0.0));
        assertThat(resp.getMarginrequirement(),is(0));
        assertThat(resp.getNetamt(),is(25.85));
        assertThat(resp.getPrincipal(),is(20.9));
        assertThat(resp.getSecfee(),is(0));
        assertThat(resp.getWarning().getWarningcode(),is(4));
        assertThat(resp.getWarning().getWarningtext(),is("This may be a duplicate order. Please go to the Order Status screen and review your open orders before continuing."));
        assertThat(resp.getQuotes(), notNullValue());
        assertThat(resp.getQuotes().getInstrumentquote().getInstrument().getSym(), is("GE"));
        assertThat(resp.getQuotes().getInstrumentquote().getQuote().getAskprice(),is(20.91));
    }
}

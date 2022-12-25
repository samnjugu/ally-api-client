package com.celexus.conniption.model.accounts;

import com.celexus.conniption.model.XmlParsingTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class HistoryResponseTest extends XmlParsingTest {
    @Test
    public void historyParseTest() throws Exception {
        HistoryResponse resp = parse(source("xml/History.xml"),
                "com.celexus.conniption.model.accounts",
                HistoryResponse.class);
        assertThat(resp, notNullValue());
        assertThat(resp.getId(), is("77cf30da:12df25c7074:-7e97"));
        assertThat(resp.getTransactions().getTransaction().get(0).getActivity(),is("Trade"));
        assertThat(resp.getTransactions().getTransaction().get(0).getAmount(),is(1682.05));
        assertThat(resp.getTransactions().getTransaction().get(0).getDesc(),is("TEST EQUITY"));
        assertThat(resp.getTransactions().getTransaction().get(0).getSymbol(),is("ZVZZT"));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getAccounttype(),is(2));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getCommission(),is(0.0));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getDescription(),is("TEST EQUITY"));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getPrice(),is(1687.04));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getSource(),is("XCH"));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getSide(),is(2));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getTransactionid(),is(7));
        assertThat(resp.getTransactions().getTransaction().get(0).getTransaction().getSecurity().getSectyp(),is("CS"));
        assertThat(resp.getTransactions().getTransaction().get(1).getActivity(),is("Dividend"));
        assertThat(resp.getTransactions().getTransaction().get(2).getActivity(),is("Bookkeeping"));
    }
}

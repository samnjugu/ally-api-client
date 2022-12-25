package com.celexus.conniption.api;

import com.celexus.conniption.foreman.ForemanException;
import com.celexus.conniption.foreman.TradeKingForeman;
import com.celexus.conniption.foreman.enums.AccountHistoryField;
import com.celexus.conniption.foreman.util.properties.AllyProperties;
import com.celexus.conniption.model.accounts.AccountHoldingsResponse;
import com.celexus.conniption.model.accounts.HistoryResponse;
import com.celexus.conniption.model.clock.ClockResponse;
import com.celexus.conniption.model.orders.OrdersResponse;
import com.celexus.conniption.model.quotes.QuotesResponse;
import org.junit.Test;

public class TradekingTest {

    /* Test we can un/marshal accounts Object using our models**/
    @Test
    public void accountsTest() throws ForemanException {
        TradeKing tk = new TradeKing(new TradeKingForeman());
        System.out.println(tk.accounts().getAccounts().toString());

        ClockResponse c = tk.clock();
        System.out.println(c.getMessage());

        QuotesResponse q = tk.quotes(new  String[] {"TWTR", "FB"});
        System.out.println(q.getQuotes().toString());

        OrdersResponse ors = tk.orders(AllyProperties.ACCOUNT_NO);
        System.out.println(ors);

        HistoryResponse h = tk.history(AllyProperties.ACCOUNT_NO);
        System.out.println(h);

        HistoryResponse h2 = tk.history(AllyProperties.ACCOUNT_NO, AccountHistoryField.RANGE_ALL,AccountHistoryField.TRANSACTIONS_TYPE_TRADE);
        System.out.println(h2);

        AccountHoldingsResponse h3 = tk.holdings(AllyProperties.ACCOUNT_NO);
        System.out.println(h3);

    }
}

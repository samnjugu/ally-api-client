package com.celexus.conniption.foreman;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.celexus.conniption.foreman.util.ResponseFormat;
import com.celexus.conniption.foreman.util.builder.AccountsBuilder;
import com.celexus.conniption.foreman.util.builder.MarketBuilder;

public class TradekingForemanTest {

    @Test
    public void connectionTest() {
        TradeKingForeman foreman = new TradeKingForeman();

        assertTrue("Foreman does not have OAuth Service", !foreman.hasOAuth());
        assertTrue("Foreman does not have Access Token", !foreman.hasAccessToken());
    }

    @Test
    public void apiCallTest() throws ForemanException, ExecutionException, IOException, InterruptedException{
        TradeKingForeman foreman = new TradeKingForeman();
        assertTrue(
                "Foreman didn't recognize API reponse",
                foreman.makeAPICall(MarketBuilder.getClock(ResponseFormat.XML))
                        .toString()
                        .contains("<message>"));
    }

    //	@Test
    public void accountsTest() throws Exception {
        TradeKingForeman forman = new TradeKingForeman();
        // System.out.println(forman.makeAPICall(AccountsBuilder.getAccountBalances(ResponseFormat.XML)));
        System.out.println(
                forman.makeAPICall(AccountsBuilder.getAccount("38580744", ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(AccountsBuilder.getAccountBalance("38580744",ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(OrdersBuilder.getOrders("38580744", ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(MarketBuilder.getClock(ResponseFormat.XML)));
        //
        // System.out.println(forman.makeAPICall(MarketBuilder.getQuotes(ResponseFormat.XML,"APPL","MSFT")));

        // System.out.println(forman.makeAPICall(b))
    }
}

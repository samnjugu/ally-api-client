package com.celexus.conniption.foreman;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.celexus.conniption.foreman.util.properties.AllyProperties;
import com.celexus.conniption.model.accounts.Accounts;
import org.junit.Test;

import com.celexus.conniption.foreman.enums.ResponseFormat;
import com.celexus.conniption.foreman.util.builder.AccountsBuilder;
import com.celexus.conniption.foreman.util.builder.MarketBuilder;

public class TradekingForemanTest {

    @Test
    public void connectionTest() throws ForemanException {
        TradeKingForeman foreman = new TradeKingForeman();

        assertTrue("Foreman does have OAuth Service", foreman.hasOAuth());
        assertTrue("Foreman does have Access Token", foreman.hasAccessToken());
    }

    @Test
    public void connectionTest2() throws ForemanException {
        TradeKingForeman foreman = new TradeKingForeman(AllyProperties.CONSUMER_KEY,AllyProperties.CONSUMER_SECRET,
                AllyProperties.OAUTH_TOKEN,AllyProperties.OAUTH_TOKEN_SECRET);

        assertTrue("Foreman does have OAuth Service", foreman.hasOAuth());
        assertTrue("Foreman does have Access Token", foreman.hasAccessToken());
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

    @Test
    public void accountsTest() throws Exception {
        TradeKingForeman foreman = new TradeKingForeman();
        //System.out.println(foreman.makeAPICall(MarketBuilder.getClock(ResponseFormat.XML)));

        TKResponse acc = foreman.makeAPICall(AccountsBuilder.getAccounts(ResponseFormat.XML));
        System.out.println(acc);
        // System.out.println(forman.makeAPICall(AccountsBuilder.getAccountBalances(ResponseFormat.XML)));
        //System.out.println(foreman.makeAPICall(AccountsBuilder.getAccount("38580744", ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(AccountsBuilder.getAccountBalance("38580744",ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(OrdersBuilder.getOrders("38580744", ResponseFormat.XML)));
        // System.out.println(forman.makeAPICall(MarketBuilder.getClock(ResponseFormat.XML)));
        //
        // System.out.println(forman.makeAPICall(MarketBuilder.getQuotes(ResponseFormat.XML,"APPL","MSFT")));

        // System.out.println(forman.makeAPICall(b))
    }
}

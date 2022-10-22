package com.celexus.conniption.api;

import com.celexus.conniption.foreman.TradeKingForeman;
import org.junit.Test;

public class TradekingTest {

    /* Test we can un/marshal accounts Object using our models**/
    @Test
    public void accountsTest(){
        TradeKing tk = new TradeKing(new TradeKingForeman());
        System.out.println(tk.accounts().getAccounts().toString());
    }
}

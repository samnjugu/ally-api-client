package com.celexus.conniption.foreman.util.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.celexus.conniption.foreman.util.ResponseFormat;

public class AccountsBuilderTest {

    @Test
    public void getAccountTest() {
        APIBuilder b = AccountsBuilder.getAccount("XXX", ResponseFormat.XML);
        assertTrue(b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/XXX.xml",
                b.getResourceURL());
    }

    @Test
    public void getAccountsTest() {
        APIBuilder b = AccountsBuilder.getAccounts(ResponseFormat.XML);
        assertTrue(b.getParameters().isEmpty());
        assertEquals("", "https://api.tradeking.com/v1/accounts.xml", b.getResourceURL());
    }

    @Test
    public void getBalanceTest() {
        APIBuilder b = AccountsBuilder.getAccountBalance("XXX", ResponseFormat.XML);
        assertTrue(b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/XXX/balances.xml",
                b.getResourceURL());
    }

    @Test
    public void getBalancesTest() {
        APIBuilder b = AccountsBuilder.getAccountBalances(ResponseFormat.XML);
        assertTrue(b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/balances.xml",
                b.getResourceURL());
    }
}

package com.celexus.conniption.foreman.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.celexus.conniption.foreman.util.APICall.TopList;

public class APICallsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void accountsTest() {
        String accounts = APICall.getAccounts(ResponseFormat.XML);
        String balances = APICall.getAccountBalances(ResponseFormat.XML);
        String byID = APICall.getAccountByID(ResponseFormat.XML, "myId");

        assertEquals(accounts, "https://api.tradeking.com/v1/accounts.xml");
        assertEquals(balances, "https://api.tradeking.com/v1/accounts/balances.xml");
        assertEquals(byID, "https://api.tradeking.com/v1/accounts/myId.xml");
    }

    @Test
    public void ordersTest() {
        String orderByID = APICall.getOrderByAccountID(ResponseFormat.XML, "myId");
        String postOrder = APICall.postOrderByAccountID(ResponseFormat.XML, "myId");
        String preview = APICall.postOrderByAccountIDPreview(ResponseFormat.XML, "myId");

        assertEquals(orderByID, "https://api.tradeking.com/v1/accounts/myId/orders.xml");
        assertEquals(postOrder, "https://api.tradeking.com/v1/accounts/myId/orders.xml");
        assertEquals(preview, "https://api.tradeking.com/v1/accounts/myId/orders/preview.xml");
    }

    @Test
    public void marketTest() {
        String clock = APICall.getMarketClock(ResponseFormat.XML);
        String quote = APICall.getQuote(ResponseFormat.XML);
        String newsSearch = APICall.searchNews(ResponseFormat.XML);
        String news = APICall.getNews(ResponseFormat.XML, "234899d5fd2ee9a501a8349a0f571f6f");
        String optionsSearch = APICall.searchOptions(ResponseFormat.XML);
        String optionStrikes = APICall.getOptionStrikes(ResponseFormat.XML);
        String optionExpirations = APICall.getOptionExpirations(ResponseFormat.XML);
        String timeSales = APICall.getTimeSales(ResponseFormat.XML);
        String gainers = APICall.getTopList(TopList.GAINERS_ACTIVE, ResponseFormat.XML);

        assertEquals(clock, "https://api.tradeking.com/v1/market/clock.xml");
        assertEquals(quote, "https://api.tradeking.com/v1/market/ext/quotes.xml");
        assertEquals(newsSearch, "https://api.tradeking.com/v1/market/news/search.xml");
        assertEquals(
                news,
                "https://api.tradeking.com/v1/market/news/234899d5fd2ee9a501a8349a0f571f6f.xml");
        assertEquals(optionsSearch, "https://api.tradeking.com/v1/market/options/search.xml");
        assertEquals(optionStrikes, "https://api.tradeking.com/v1/market/options/strikes.xml");
        assertEquals(
                optionExpirations, "https://api.tradeking.com/v1/market/options/expirations.xml");
        assertEquals(timeSales, "https://api.tradeking.com/v1/market/timesales.xml");
        assertEquals(
                gainers,
                "https://api.tradeking.com/v1/market/toplists/topactivegainersbydollarvalue.xml");
    }

    @Test
    public void memberTest() {
        String member = APICall.getMemberProfile(ResponseFormat.XML);
        assertEquals(member, "https://api.tradeking.com/v1/member/profile.xml");
    }

    @Test
    public void utilityTest() {
        String status = APICall.getTKStatus(ResponseFormat.XML);
        String version = APICall.getTKVersion(ResponseFormat.XML);
        assertEquals(status, "https://api.tradeking.com/v1/utility/status.xml");
        assertEquals(version, "https://api.tradeking.com/v1/utility/version.xml");
    }

    @Test
    public void watchlistTest() {
        String watchlists = APICall.getWatchlists(ResponseFormat.XML);
        String postWatchlists = APICall.postWatchlists(ResponseFormat.XML);
        String getWatchlistsId = APICall.getWatchlistsById("myId", ResponseFormat.XML);
        String deleteWatchlistsId = APICall.deleteWatchlistsById("myId", ResponseFormat.XML);
        String getWatchlistsSymbol = APICall.postWatchlistsBySymbol("myId", ResponseFormat.XML);
        String deleteSymbolWatchList =
                APICall.deleteSymbolFromWatchList("myId", "sym", ResponseFormat.XML);

        assertEquals(watchlists, "https://api.tradeking.com/v1/watchlists.xml");
        assertEquals(postWatchlists, "https://api.tradeking.com/v1/watchlists.xml");
        assertEquals(getWatchlistsId, "https://api.tradeking.com/v1/watchlists/myId.xml");
        assertEquals(deleteWatchlistsId, "https://api.tradeking.com/v1/watchlists/myId.xml");
        assertEquals(
                getWatchlistsSymbol, "https://api.tradeking.com/v1/watchlist/myId/symbols.xml");
        assertEquals(deleteSymbolWatchList, "https://api.tradeking.com/v1/watchlists/myId/sym.xml");
    }
}

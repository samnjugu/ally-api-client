package com.celexus.conniption.foreman.util;

import static org.junit.Assert.assertEquals;

import com.celexus.conniption.foreman.enums.ResponseFormat;
import com.celexus.conniption.foreman.enums.TopList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


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

        assertEquals(accounts, "https://devapi.invest.ally.com/v1/accounts.xml");
        assertEquals(balances, "https://devapi.invest.ally.com/v1/accounts/balances.xml");
        assertEquals(byID, "https://devapi.invest.ally.com/v1/accounts/myId.xml");
    }

    @Test
    public void ordersTest() {
        String orderByID = APICall.getOrderByAccountID(ResponseFormat.XML, "myId");
        String postOrder = APICall.postOrderByAccountID(ResponseFormat.XML, "myId");
        String preview = APICall.postOrderByAccountIDPreview(ResponseFormat.XML, "myId");

        assertEquals(orderByID, "https://devapi.invest.ally.com/v1/accounts/myId/orders.xml");
        assertEquals(postOrder, "https://devapi.invest.ally.com/v1/accounts/myId/orders.xml");
        assertEquals(preview, "https://devapi.invest.ally.com/v1/accounts/myId/orders/preview.xml");
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
        String stream = APICall.getStreamingQuote(ResponseFormat.XML);

        assertEquals(clock, "https://devapi.invest.ally.com/v1/market/clock.xml");
        assertEquals(quote, "https://devapi.invest.ally.com/v1/market/ext/quotes.xml");
        assertEquals(newsSearch, "https://devapi.invest.ally.com/v1/market/news/search.xml");
        assertEquals(
                news,
                "https://devapi.invest.ally.com/v1/market/news/234899d5fd2ee9a501a8349a0f571f6f.xml");
        assertEquals(optionsSearch, "https://devapi.invest.ally.com/v1/market/options/search.xml");
        assertEquals(optionStrikes, "https://devapi.invest.ally.com/v1/market/options/strikes.xml");
        assertEquals(
                optionExpirations, "https://devapi.invest.ally.com/v1/market/options/expirations.xml");
        assertEquals(timeSales, "https://devapi.invest.ally.com/v1/market/timesales.xml");
        assertEquals(
                gainers,
                "https://devapi.invest.ally.com/v1/market/toplists/topactivegainersbydollarvalue.xml");
        assertEquals(stream, "https://devapi-stream.invest.ally.com/v1/market/quotes.xml");
    }

    @Test
    public void memberTest() {
        String member = APICall.getMemberProfile(ResponseFormat.XML);
        assertEquals(member, "https://devapi.invest.ally.com/v1/member/profile.xml");
    }

    @Test
    public void utilityTest() {
        String status = APICall.getTKStatus(ResponseFormat.XML);
        String version = APICall.getTKVersion(ResponseFormat.XML);
        assertEquals(status, "https://devapi.invest.ally.com/v1/utility/status.xml");
        assertEquals(version, "https://devapi.invest.ally.com/v1/utility/version.xml");
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

        assertEquals(watchlists, "https://devapi.invest.ally.com/v1/watchlists.xml");
        assertEquals(postWatchlists, "https://devapi.invest.ally.com/v1/watchlists.xml");
        assertEquals(getWatchlistsId, "https://devapi.invest.ally.com/v1/watchlists/myId.xml");
        assertEquals(deleteWatchlistsId, "https://devapi.invest.ally.com/v1/watchlists/myId.xml");
        assertEquals(
                getWatchlistsSymbol, "https://devapi.invest.ally.com/v1/watchlists/myId/symbols.xml");
        assertEquals(deleteSymbolWatchList, "https://devapi.invest.ally.com/v1/watchlists/myId/sym.xml");
    }
}

package com.celexus.conniption.foreman.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

public class TradekingAPITest {

    @Test
    public void validEndpointsTest() {
        TradekingAPI api = new TradekingAPI();
        assertTrue("Request Token Endpoint is not valid", isValid(api.getRequestTokenEndpoint()));
        assertTrue("Access Token Endpoint is not valid", isValid(api.getAccessTokenEndpoint()));
    }

    public boolean isValid(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return false;
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
            conn.connect();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}

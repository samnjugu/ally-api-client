package com.celexus.conniption.foreman;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ForemanConstantsTest {

    @Test
    public void test() {
        assertTrue(!ForemanConstants.CONSUMER_KEY.toString().isEmpty());
        assertTrue(!ForemanConstants.CONSUMER_SECRET.toString().isEmpty());
        assertTrue(!ForemanConstants.OAUTH_TOKEN.toString().isEmpty());
        assertTrue(!ForemanConstants.OAUTH_TOKEN_SECRET.toString().isEmpty());
    }
}

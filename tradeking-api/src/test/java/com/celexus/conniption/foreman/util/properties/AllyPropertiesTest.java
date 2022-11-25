package com.celexus.conniption.foreman.util.properties;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AllyPropertiesTest {
    @Test
    public void testAllyProperties(){
        assertTrue(!AllyProperties.ACCOUNT_NO.isEmpty());
        assertTrue(!AllyProperties.CONSUMER_KEY.isEmpty());
        assertTrue(!AllyProperties.CONSUMER_SECRET.isEmpty());
        assertTrue(!AllyProperties.OAUTH_TOKEN.isEmpty());
        assertTrue(!AllyProperties.OAUTH_TOKEN_SECRET.isEmpty());
        assertTrue(!AllyProperties.ALLY_URL.isEmpty());
    }
}

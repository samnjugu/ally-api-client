package com.celexus.conniption.foreman.util.properties;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
        assertTrue(!AllyProperties.ALLY_STREAM_URL.isEmpty());
    }

    //Prints out test resources should have ally.default.properties,ally.properties
    //mvn test-compile copies resources to /target/test-classes
    @Test
    public void testResourceLoaded() throws Exception {
        ClassLoader classLoader = AllyPropertiesTest.class.getClassLoader();
        URL hsURL= classLoader.getResource("logback-test.xml");
        System.out.println(hsURL.toString());
        try (
            InputStream in = classLoader.getResourceAsStream(".");
            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;

                while ((resource = br.readLine()) != null) {
                    System.out.println(resource);
                }
        };
    }
}

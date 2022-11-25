package com.celexus.conniption.foreman.util.properties;

import static com.celexus.conniption.foreman.util.properties.PropertyUtil.getProperty;
public class AllyProperties {
    private static final String ALLY_PROPERTIES_FILE = "ally.properties";
    private static final String ALLY_DEFAULT_PROPERTIES_FILE = "ally.default.properties";

    private static final String ACCOUNT_NO_KEY = "account_no";
    private static final String CONSUMER_KEY_KEY = "consumer_key";
    private static final String CONSUMER_SECRET_KEY = "consumer_secret";
    private static final String OAUTH_TOKEN_KEY = "oauth_token";
    private static final String OAUTH_TOKEN_SECRET_KEY = "oauth_token_secret";
    private static final String ALLY_URL_KEY = "ally_url";

    /**
     * Ally Account Number
     */
    public static final String ACCOUNT_NO = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            ACCOUNT_NO_KEY);
    /**
     * Ally OAuth API Key
     */
    public static final String CONSUMER_KEY = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            CONSUMER_KEY_KEY);
    /**
     * Ally OAuth API Secret Key
     */
     public static final String CONSUMER_SECRET = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            CONSUMER_SECRET_KEY);
    /**
     * Ally OAuth Access Token Key
     */
    public static final String OAUTH_TOKEN = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            OAUTH_TOKEN_KEY);
    /**
     * Ally OAuth Access Token Secret Key
     */
    public static final String OAUTH_TOKEN_SECRET = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            OAUTH_TOKEN_SECRET_KEY);
    /**
     * Ally base url
     */
    public static final String ALLY_URL = getProperty(ALLY_PROPERTIES_FILE, ALLY_DEFAULT_PROPERTIES_FILE,
            ALLY_URL_KEY);

}

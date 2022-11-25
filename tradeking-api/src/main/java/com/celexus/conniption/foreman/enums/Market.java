package com.celexus.conniption.foreman.enums;

import com.celexus.conniption.foreman.util.properties.AllyProperties;
import com.github.scribejava.core.model.Verb;

public enum Market {
    CLOCK(baseUrl.url+"/clock", "."),
    EXT_QUOTES(baseUrl.url+"/ext/quotes", "."),
    STREAM_EXT_QUOTES(baseUrl.url+"/quotes", "."),
    NEWS_SEARCH(baseUrl.url+"/news/search", "."),
    NEWS_ID(baseUrl.url+"/news/", "", "."),
    OPTIONS_SEARCH(baseUrl.url+"/options/search", "."),
    OPTIONS_STRIKES(baseUrl.url+"/options/strikes", "."),
    OPTIONS_EXPIRATIONS(baseUrl.url+"/options/expirations", "."),
    TIMESALES(baseUrl.url+"/timesales", "."),
    TOPLISTS_VOLUME(baseUrl.url+"/toplists/topvolume", "."),
    TOPLISTS_LOSERS_DOLLAR(baseUrl.url+"/toplists/toplosers", "."),
    TOPLISTS_LOSERS_PERCENTAGE(baseUrl.url+"/toplists/toppctlosers", "."),
    TOPLISTS_ACTIVE(baseUrl.url+"/toplists/topactive", "."),
    TOPLISTS_GAINERS_DOLLAR_AMT(baseUrl.url+"/toplists/topgainers", "."),
    TOPLISTS_GAINERS_PERCENTAGE(baseUrl.url+"/toplists/toppctgainers", "."),
    TOPLISTS_GAINERS_ACTIVE_DOLLAR_AMT(baseUrl.url+"/toplists/topactivegainersbydollarvalue", ".");

    private String[] urlStrings;

    Market(String... urlStrings) {
        this.urlStrings = urlStrings;
    }

    /**
     * Concatenate urlString array with params string array.
     * @param params
     * @return
     */
    public String resolveString(String... params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urlStrings.length; i++) {
            sb.append(urlStrings[i]);
            if (params.length > i) {
                sb.append(params[i]);
            }
        }
        return sb.toString();
    }

    public Verb getVerb() {
        return Verb.GET;
    }

    private static class baseUrl{
        private static String url =  AllyProperties.ALLY_URL+"market";
    }
}

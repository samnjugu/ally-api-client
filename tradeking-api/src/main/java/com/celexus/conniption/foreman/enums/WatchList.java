package com.celexus.conniption.foreman.enums;

import com.github.scribejava.core.model.Verb;

public enum WatchList {
    GET_WATCHLISTS(Verb.GET, baseUrl.url, "."),
    POST_WATCHLISTS(Verb.POST, baseUrl.url, "."),
    GET_WATCHLIST_ID(Verb.GET, baseUrl.url+"/", "", "."),
    DELETE_WATCHLISTS_ID(Verb.DELETE, baseUrl.url+"/", "", "."),
    POST_SYMBOL_WATCHLIST_ID(
            Verb.POST, baseUrl.url+"/", "/symbols", "."),
    DELETE_SYMBOL_WATCHLIST(
            Verb.DELETE, baseUrl.url+"/", "/", ".", "");

    private String[] urlStrings;
    private Verb verb;

    WatchList(Verb verb, String... urlStrings) {
        this.urlStrings = urlStrings;
        this.verb = verb;
    }

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
        return verb;
    }

    private static class baseUrl{
        private static String url = "https://devapi.invest.ally.com/v1/watchlists";
    }
}

package com.celexus.conniption.foreman.enums;

import com.github.scribejava.core.model.Verb;

public enum OrderTrades {
    GET_ID_ORDERS(Verb.GET, baseUrl.url, "/orders", "."),
    POST_ID_ORDERS(Verb.POST, baseUrl.url, "/orders", "."),
    POST_ID_ORDERS_PREVIEW(Verb.POST, baseUrl.url, "/orders/preview", ".");

    private Verb v;
    private String[] urlStrings;

    OrderTrades(Verb v, String... urlStrings) {
        this.v = v;
        this.urlStrings = urlStrings;
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
        return v;
    }

    private static class baseUrl{
        private static String url = "https://devapi.invest.ally.com/v1/accounts/";
    }
}

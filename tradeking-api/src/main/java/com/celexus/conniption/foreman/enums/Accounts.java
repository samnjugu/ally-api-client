package com.celexus.conniption.foreman.enums;

import com.celexus.conniption.foreman.util.properties.AllyProperties;
import com.github.scribejava.core.model.Verb;

public enum Accounts {
    ACCOUNTS(baseUrl.url, "."),
    ACCOUNTS_BALANCES(baseUrl.url+"/balances",  "."),
    ID(baseUrl.url+"/", "."),
    ID_BALANCES(baseUrl.url+"/", "/balances", "", "", ""),
    ID_HISTORY(baseUrl.url+"/", "/history", ""),
    ID_HOLDINGS(baseUrl.url+"/", "/holdings", "");

    private String[] urlStrings;

    Accounts(String... urlStrings) {
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
        return Verb.GET;
    }

    //Prefer to have url in a properties file moving it below for now
    private static class baseUrl{
        private static String url = AllyProperties.ALLY_URL+"accounts";
    }
}

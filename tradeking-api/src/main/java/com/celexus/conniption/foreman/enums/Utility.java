package com.celexus.conniption.foreman.enums;

import com.celexus.conniption.foreman.util.properties.AllyProperties;
import com.github.scribejava.core.model.Verb;

public enum Utility {
    STATUS(baseUrl.url+"/status", "."),
    VERSION(baseUrl.url+"/version", ".");

    private String[] urlStrings;

    Utility(String... urlStrings) {
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
        private static String url =  AllyProperties.ALLY_URL+"utility";
    }
}

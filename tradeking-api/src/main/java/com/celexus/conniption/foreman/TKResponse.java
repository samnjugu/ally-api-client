/**
 * Copyright 2013 Cameron Cook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.celexus.conniption.foreman;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;

/**
 * A holder for all the bits that come back through each REST call.<br>
 * The important parts are the rate limits, which help you control how many
 * calls of that type you can use again.
 *
 * @author cam
 *
 */
public class TKResponse {

    private static final long serialVersionUID = 1626169989303557890L;
    private String response = "";
    private int rateLimitUsed = 0;
    private long rateLimitExpire = 0;
    private int rateLimitTotal = 0;
    private int rateLimitRemaining = 0;

    public TKResponse(Response response) {
        String limitUsed = response.getHeader("X-RateLimit-Used");
        String limitExpire = response.getHeader("X-RateLimit-Expire");
        String limitTotal = response.getHeader("X-RateLimit-Limit");
        String limitRemain = response.getHeader("X-RateLimit-Remaining");
        if (limitUsed != null) {
            rateLimitUsed = Integer.parseInt(limitUsed);
        }
        if (limitExpire != null) {
            // TK is now sending decimal strings (e.g. "1461721950.38133")
            if (limitExpire.contains(".")) {
                limitExpire = limitExpire.substring(0, limitExpire.indexOf("."));
            }
            rateLimitExpire = Long.parseLong(limitExpire);
        }
        if (limitTotal != null) {
            rateLimitTotal = Integer.parseInt(limitTotal);
        }
        if (limitRemain != null) {
            rateLimitRemaining = Integer.parseInt(limitRemain);
        }
        this.response = getBody(response);
    }

    public TKResponse(String req, Integer... limits) {
        this.response = req;
    }

    public TKResponse() {}

    public int getCallsUsed() {
        return rateLimitUsed;
    }

    public long getRateLimitExpiration() {
        return rateLimitExpire;
    }

    public int getTotalCallsAllowed() {
        return rateLimitTotal;
    }

    public int getCallsRemaining() {
        return rateLimitRemaining;
    }

    @Override
    public String toString() {
        return response;
    }

    static private void printResponse(Response resp) {
        System.err.println("" + resp.getCode());
        Map<String, String> headers = resp.getHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.err.println(String.format("  - %s : %s", header.getKey(), header.getValue()));
        }
        System.err.println(getBody(resp));
    }

    static private void printRequest(OAuthRequest req) {
        System.err.println(req.toString());
        Map<String, String> headers = req.getHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.err.println(String.format("  - %s : %s", header.getKey(), header.getValue()));
        }
    }

    static public String getBody(Response resp) {
        try {
            return resp.getBody();
        } catch (IOException e) {
            return null;
        }
    }
}

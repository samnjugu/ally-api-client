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

/**
 * Allys Foreman's Constants. These can be set either as system enviroment
 * variables or as system properties. System properties take precendence over
 * environment variables.
 *
 * @author cam, garth
 *
 */
public enum ForemanConstants {

    /**
     * Ally Account Number
     */
    ACCOUNT_NO("ACCOUNT_NO"),
    /**
     * Ally OAuth API Key
     */
    CONSUMER_KEY("CONSUMER_KEY"),
    /**
     * Ally OAuth API Secret Key
     */
    CONSUMER_SECRET("CONSUMER_SECRET"),
    /**
     * Ally OAuth Access Token Key
     */
    OAUTH_TOKEN("OAUTH_TOKEN"),
    /**
     * Ally OAuth Access Token Secret Key
     */
    OAUTH_TOKEN_SECRET("OAUTH_TOKEN_SECRET");

    private String key;

    ForemanConstants(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        if (System.getProperty(key) != null) {
            return System.getProperty(key);
        } else {
            return System.getenv(key);
        }
    }
}

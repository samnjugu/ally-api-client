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
package com.celexus.conniption.foreman.util.builder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.github.scribejava.core.model.Verb;

/**
 * The abstract backend to all APIBuilders. Contains the base URL from APICall 
 * and a map of all params (stock symbols and filters), if it is used for streaming 
 * and GET POST method..
 *
 * @author cam
 *
 */
public abstract class APIBuilder implements Serializable {

    private static final long serialVersionUID = 5364934694744835663L;
    protected Verb verb;
    protected Map<String, String> params = new HashMap<String, String>();
    protected String resourceURL;
    protected String body;
    protected boolean streaming;

    public Verb getVerb() {
        return verb;
    }

    public Map<String, String> getParameters() {
        return params;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    /**
     * Only used in OrdersBuilder (buy/sell order), usually not used in making Account and Market inquiry.
     */
    public String getBody() {
        return body;
    }

    /**
     * If this APIBuilder is used for streaming.
     * @return
     */
    public boolean isStreaming() {
        return streaming;
    }
}

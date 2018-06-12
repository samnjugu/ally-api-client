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
package com.celexus.conniption.model.fixml;

import java.io.Serializable;

public enum PriceType implements Serializable {
    /*
	 * Price Type as "1" ‐ Market, "2" ‐ Limit", "3" ‐ Stop, "4" Stop Limit, or "P" for trailing stop. 
     */
    MARKET("1"),
    LIMIT("2"),
    STOP("3"),
    STOP_LIMIT("4"),
    TRAILING_STOP("P");

    private String value;

    PriceType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

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

import com.celexus.conniption.foreman.util.APICall;
import com.celexus.conniption.foreman.util.ResponseFormat;
import com.github.scribejava.core.model.Verb;

/**
 * An APIBuilder that will build REST calls relating to TradeKing Accounts
 *
 * @author cam
 *
 */
public class AccountsBuilder extends APIBuilder implements Serializable {

    private static final long serialVersionUID = 7982141126897931034L;

    private AccountsBuilder(Verb v) {
        verb = v;
    }

    public static AccountsBuilder getAccounts(ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccounts(format);
        return toReturn;
    }

    public static APIBuilder getAccount(String id, ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccountByID(format, id);
        return toReturn;
    }

    public static AccountsBuilder getAccountBalances(ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccountBalances(format);
        return toReturn;
    }

    public static APIBuilder getAccountBalance(String id, ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccountBalanceByID(format, id);
        return toReturn;
    }

    public static APIBuilder getAccountHoldings(String id, ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccountHoldingsByID(format, id);
        return toReturn;
    }

    public static APIBuilder getAccountHistory(String id, ResponseFormat format) {
        AccountsBuilder toReturn = new AccountsBuilder(Verb.GET);
        toReturn.resourceURL = APICall.getAccountHistoryByID(format, id);
        return toReturn;
    }
}

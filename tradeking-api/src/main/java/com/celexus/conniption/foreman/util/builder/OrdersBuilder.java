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

import com.celexus.conniption.foreman.util.APICall;
import com.celexus.conniption.foreman.enums.ResponseFormat;
import com.github.scribejava.core.model.Verb;

/**
 * An APIBuilder to handle TradeKing Orders
 *
 * @author cam
 *
 */
public class OrdersBuilder extends APIBuilder {

    private static final long serialVersionUID = 4913476469409758292L;

    private OrdersBuilder(Verb v) {
        verb = v;
    }

    public static OrdersBuilder getOrders(String id, ResponseFormat format) {
        OrdersBuilder b = new OrdersBuilder(Verb.GET);
        b.resourceURL = APICall.getOrderByAccountID(format, id);
        return b;
    }

    public static OrdersBuilder postOrder(String id, String fixml, ResponseFormat format) {
        OrdersBuilder b = new OrdersBuilder(Verb.GET);
        b.resourceURL = APICall.postOrderByAccountID(format, id);
        b.body = fixml;
        return b;
    }

    public static OrdersBuilder preview(String id, String fixml, ResponseFormat format) {
        OrdersBuilder b = new OrdersBuilder(Verb.POST);
        b.resourceURL = APICall.postOrderByAccountIDPreview(format, id);
        b.body = fixml;
        return b;
    }
}

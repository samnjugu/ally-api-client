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

public enum FIXMLField implements Serializable {
    /**
     * Acct Account number needs to be passed with all order requests.
     */
    ACCOUNT_ID("Acct"),
    /**
     * AcctTyp Only used for closing short positions, "Buy to Cover" orders
     * should include this attribute as AcctTyp="5".
     */
    ACCOUNT_TYPE("AcctTyp"),
    /**
     * CFI Abbreviation for "classification of financial instrument", used for
     * options to distinguish "OC" for call option or "OP" for put option.
     */
    CLASSIFICATION_OF_FINANCIAL_INSTRUMENT("CFI"),
    /**
     * Mat Represents the expiration date of a option. Needs to be in the format
     * of "YYYY‐MM‐ DDT00:00:00.000‐05:00". For single leg orders, this
     * attribute tag changes from Mat to MatDt.
     */
    OPTION_EXPIRATION_SINGLE_LEG("Mat"),
    /**
     * MatDt Represents the expiration date of a option. Needs to be in the
     * format of "YYYY‐MM‐ DDT00:00:00.000‐05:00". For multiple leg orders, this
     * attribute tag changes from MatDt to Mat.
     */
    OPTION_EXPIRATION_MULTI_LEG("MatDt"),
    /**
     * MMY Expiration of the option in the format of YYYYMM.
     */
    OPTION_EXPIRATION("MMY"),
    /**
     * OrigID Order ID that needs to be passed for any change or cancel
     * requests. Note: for Multi‐leg orders, use tag OrigClOrdID instead of
     * OrigID.
     */
    ORDER_ID("OrigID"),
    /**
     * PosEfct Used for options, option legs require and attribute of "O" for
     * opening or "C" for closing.
     */
    POS_EFCT("PosEfct"),
    /**
     * Px Price for price type if needed. This attribute would be required for
     * limits (Typ = "2") or stop limits (Typ="4").
     */
    PRICE_FOR_PRICE_TYPE("Px"),
    /**
     * SecTyp Security type attribute is needed. "CS" for common stock or "OPT"
     * for option.
     */
    SECURITY_TYPE("SecTyp"),
    /**
     * Side Side of market as "1" ‐ Buy, "2" ‐ Sell, "5" ‐ Sell Short. Buy to
     * cover orders are attributed as buy orders with Side="1".
     */
    SIDE_OF_MARKET("Side"),
    /**
     * Strk Strike price of option contract. This tag changes from Strk to
     * StrkPx for single leg orders.
     */
    STRIKE_PRICE_SINGLE_LEG("Strk"),
    /**
     * StrkPx Strike price of option contract. This tag changes from StrkPx to
     * Strk for multi‐leg orders.
     */
    STRIKE_PRICE_MULTI_LEG("StrkPx"),
    /**
     * Sym Ticker symbol of underlying security. This is utilized for stock,
     * option, & multi‐leg orders.
     */
    SYMBOL("Sym"),
    /**
     * TmInForce Time in force, possible values include "0" ‐ Day Order, "1" ‐
     * GTC Order, "2" ‐ Market on Close. Not applicable when Typ="1" (market
     * order).
     */
    TIME_IN_FORCE("TmInForce"),
    /**
     * Typ Price Type as "1" ‐ Market, "2" ‐ Limit", "3" ‐ Stop, "4" Stop Limit,
     * or "P" for trailing stop.
     */
    PRICE_TYPE("Typ"),
    /**
     * ExecInst Used for trailing stop orders. Value of ExecInst="a" needs to be
     * passed.
     */
    EXEC_INST("ExecInst"),
    /**
     * OfstTyp Used for trailing stop orders. Value of OfstTyp="0" needs to be
     * passed. The offset value of "0" denotes a "price" offset from the
     * PegPxTyp field below. The offset value of "1" denotes a "basis point"
     * offset from the PegPxTyp field below (used as a percentage offset).
     */
    OFST_TYPE("OfstType"),
    /**
     * PegPxTyp Used for trailing stop orders defining type of peg (price used)
     * for trailing. In this case, PegPxTyp="1" references "last price" of
     * security.
     */
    PEG_PX_TYPE("PegPxTyp"),
    /**
     * OfstVal Used for trailing stop orders. Signed value needs to be passed
     * for amount of offset value combined with the PegPxTyp & OfstTyp fields.
     * Negative values are normally used for sell trailing stops so the trigger
     * trails below current price. Positive values are normally used for buy
     * trailing stops so the trigger trails above the current price. For
     * example, assuming an OfstTyp ="0", a sell order with a OfstVal of ‐.50
     * will trigger if the current price falls by more than .50 of its last
     * highest value since the order was placed. OfstType="1" would require the
     * signed value for a percentage. For example, OfstVal="5" would represent a
     * 5% increase in price before a buy trailing stop is triggered.
     */
    OFST_VAL("OfstVal");
    private String tag;

    FIXMLField(String tag) {

    }

    @Override
    public String toString() {
        return tag;
    }
}

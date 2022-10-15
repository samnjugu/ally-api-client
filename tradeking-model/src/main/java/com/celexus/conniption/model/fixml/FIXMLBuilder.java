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

/**
 * A builder to generate FIXML
 *
 * @author cam
 *
 */
public class FIXMLBuilder {

    private String id;
    private String symbol;
    private int quantity = 0;
    private TimeInForceField timeInForce;
    private PriceType priceType;
    private SecurityType securityType;
    private MarketSideField side;
    private double executionPrice = 0;
    private double offset = 0;
    private Percentage percentage;
    private double stop = 0;

    public FIXMLBuilder() {}

    public FIXMLBuilder id(String id) {
        this.id = id;
        return this;
    }

    public FIXMLBuilder symbol(String sym) {
        symbol = sym;
        return this;
    }

    public FIXMLBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public FIXMLBuilder timeInForce(TimeInForceField f) {
        timeInForce = f;
        return this;
    }

    public FIXMLBuilder priceType(PriceType type) {
        priceType = type;
        return this;
    }

    public FIXMLBuilder securityType(SecurityType type) {
        securityType = type;
        return this;
    }

    public FIXMLBuilder side(MarketSideField f) {
        side = f;
        return this;
    }

    public FIXMLBuilder executionPrice(double d) {
        executionPrice = d;
        return this;
    }

    public FIXMLBuilder executionPrice(double stop, double limit) {
        this.stop = stop;
        executionPrice = limit;
        return this;
    }

    public FIXMLBuilder offset(double d) {
        offset = d;
        return this;
    }

    public FIXMLBuilder offset(Percentage percentage) {
        this.percentage = percentage;
        return this;
    }

    public String build() {
        verify(side);
        verify(timeInForce);
        verify(symbol);
        verify(priceType);
        verify(quantity);
        verify(securityType);
        switch (side) {
            case BUY:
                if (executionPrice == 0 && offset == 0 && percentage == null) {
                    return buy(id, symbol, quantity, timeInForce, priceType, securityType);
                } else if (offset != 0) {
                    verify(offset);
                    return buyAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, offset);
                } else if (percentage != null) {
                    verify(percentage);
                    return buyAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, percentage);
                } else {
                    verify(executionPrice);
                    return buyAtPrice(id, symbol, quantity, timeInForce, priceType, securityType, executionPrice, stop);
                }

            case SELL:
                if (executionPrice == 0 && offset == 0 && percentage == null) {
                    return sell(id, symbol, quantity, timeInForce, priceType, securityType);
                } else if (offset != 0) {
                    verify(offset);
                    return sellAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, offset);
                } else if (percentage != null) {
                    verify(percentage);
                    return sellAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, percentage);
                } else {
                    verify(executionPrice);
                    return sellAtPrice(id, symbol, quantity, timeInForce, priceType, securityType, executionPrice, stop);
                }
            case BUY_TO_COVER:
                if (executionPrice == 0 && offset == 0 && percentage == null) {
                    return buyToCover(id, symbol, quantity, timeInForce, priceType, securityType);
                } else if (offset != 0) {
                    verify(offset);
                    return buyToCoverAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, offset);
                } else if (percentage != null) {
                    verify(percentage);
                    return buyToCoverAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, percentage);
                } else {
                    verify(executionPrice);
                    return buyToCoverAtPrice(id, symbol, quantity, timeInForce, priceType, securityType, executionPrice, stop);
                }
            case SELL_SHORT:
                if (executionPrice == 0 && offset == 0 && percentage == null) {
                    return sellShort(id, symbol, quantity, timeInForce, priceType, securityType);
                } else if (offset != 0) {
                    verify(offset);
                    return sellShortAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, offset);
                } else if (percentage != null) {
                    verify(percentage);
                    return sellShortAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, percentage);
                } else {
                    verify(executionPrice);
                    return sellShortAtPrice(id, symbol, quantity, timeInForce, priceType, securityType, executionPrice, stop);
                }
            default:
                break;
        }
        return null;
    }

    private void verify(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Undefined part");
        }

    }

    private void verify(int integer) {
        if (integer <= 0) {
            throw new IllegalArgumentException("Quantity invalid." + integer);
        }
    }

    private void verify(double d) {
        if (d == 0) {
            throw new IllegalArgumentException("Price invalid." + d);
        }
    }

    public static String buy(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype) {
        return FIXMLBuilder.createSimpleOrder(id, sym, quantity, tinf, type, sectype, MarketSideField.BUY);
    }

    private String buyAtPrice(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype, double executionPrice, double stop) {
        return FIXMLBuilder.createOrderAtExecution(id, sym, quantity, tinf, type, sectype, MarketSideField.BUY, executionPrice, stop);
    }

    private String buyAtOffset(String id2, String symbol2, int quantity2, TimeInForceField timeInForce2, PriceType priceType2, SecurityType securityType2, Percentage offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.BUY, offset);
    }

    private String buyAtOffset(String id2, String symbol2, int quantity2, TimeInForceField timeInForce2, PriceType priceType2, SecurityType securityType2, double offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.BUY, offset);
    }

    public static String sell(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype) {
        return FIXMLBuilder.createSimpleOrder(id, sym, quantity, tinf, type, sectype, MarketSideField.SELL);
    }

    private String sellAtPrice(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype, double executionPrice, double stop) {
        return FIXMLBuilder.createOrderAtExecution(id, sym, quantity, tinf, type, sectype, MarketSideField.SELL, executionPrice, stop);
    }

    private String sellAtOffset(String id, String symbol, int quantity, TimeInForceField timeInForce, PriceType priceType, SecurityType securityType, Percentage offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.SELL, offset);
    }

    private String sellAtOffset(String id, String symbol, int quantity, TimeInForceField timeInForce, PriceType priceType, SecurityType securityType, double offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.SELL, offset);
    }

    public static String sellShort(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype) {
        return FIXMLBuilder.createSimpleOrder(id, sym, quantity, tinf, type, sectype, MarketSideField.SELL_SHORT);
    }

    private String sellShortAtPrice(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype, double executionPrice, double stop) {
        return FIXMLBuilder.createOrderAtExecution(id, sym, quantity, tinf, type, sectype, MarketSideField.SELL_SHORT, executionPrice, stop);
    }

    private String sellShortAtOffset(String id, String symbol, int quantity, TimeInForceField timeInForce, PriceType priceType, SecurityType securityType, double offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.SELL_SHORT, offset);
    }

    private String sellShortAtOffset(String id, String symbol, int quantity, TimeInForceField timeInForce, PriceType priceType, SecurityType securityType, Percentage offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.SELL_SHORT, offset);
    }

    public static String buyToCover(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype) {
        return FIXMLBuilder.createSimpleOrder(id, sym, quantity, tinf, type, sectype, MarketSideField.SELL_SHORT);
    }

    private String buyToCoverAtPrice(String id, String sym, int quantity, TimeInForceField tinf, PriceType type, SecurityType sectype, double executionPrice, double stop) {
        return FIXMLBuilder.createOrderAtExecution(id, sym, quantity, tinf, type, sectype, MarketSideField.BUY_TO_COVER, executionPrice, stop);
    }

    private String buyToCoverAtOffset(String id2, String symbol2, int quantity2, TimeInForceField timeInForce2, PriceType priceType2, SecurityType securityType2, double offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.BUY_TO_COVER, offset);
    }

    private String buyToCoverAtOffset(String id2, String symbol2, int quantity2, TimeInForceField timeInForce2, PriceType priceType2, SecurityType securityType2, Percentage offset) {
        return FIXMLBuilder.createOrderAtOffset(id, symbol, quantity, timeInForce, priceType, securityType, MarketSideField.BUY_TO_COVER, offset);
    }

    public static void changeOrder() {
        throw new UnsupportedOperationException();
    }

    public static void cancelOrder() {
        throw new UnsupportedOperationException();
    }

    private static String createSimpleOrder(String acct, String symbol, int quant, TimeInForceField timeinforce, PriceType type, SecurityType sectype, MarketSideField side) {
        return createOrderAtExecution(acct, symbol, quant, timeinforce, type, sectype, side, 0, 0);
    }

    private static String createOrderAtOffset(String acct, String symbol, int quant, TimeInForceField timeinforce, PriceType type, SecurityType sectype, MarketSideField side, Percentage offset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n");
        sb.append("\t" + getOrderLine(timeinforce, type, side, acct, 0, 0, offset.toDouble()) + "\n");
        sb.append("\t\t" + "<PegInstr OfstTyp=\"1\" PegPxTyp=\"1\" OfstVal=\"" + offset.toDouble() + "\"/>\n");
        sb.append("\t\t" + "<Instrmt SecTyp=\"" + sectype + "\" Sym=\"" + symbol + "\"/>\n");
        sb.append("\t\t" + "<OrdQty Qty=\"" + quant + "\"/>\n");
        sb.append("\t" + "</Order>\n");
        sb.append("" + "</FIXML>\n");

        return sb.toString();
    }

    private static String createOrderAtExecution(String acct, String symbol, int quant, TimeInForceField timeinforce, PriceType type, SecurityType sectype, MarketSideField side, double executionPrice, double stop) {
        StringBuilder sb = new StringBuilder();
        sb.append("<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n");
        sb.append("\t" + getOrderLine(timeinforce, type, side, acct, executionPrice, stop, 0) + "\n");
        sb.append("\t\t" + "<Instrmt SecTyp=\"" + sectype + "\" Sym=\"" + symbol + "\"/>\n");
        sb.append("\t\t" + "<OrdQty Qty=\"" + quant + "\"/>\n");
        sb.append("\t" + "</Order>\n");
        sb.append("" + "</FIXML>\n");

        return sb.toString();
    }

    private static Object getOrderLine(TimeInForceField timeinforce, PriceType type, MarketSideField side, String acct, double price, double stop, double offset) {
        if (side.equals(MarketSideField.BUY_TO_COVER)) {
            if (price > 0) {
                if (type.equals(PriceType.STOP)) {
                    return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" AcctTyp=\"5\" StopPx=\"" + price + "\" Acct=\"" + acct + "\">";
                } else if (type.equals(PriceType.STOP_LIMIT)) {
                    return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" AcctTyp=\"5\" Px=\"\"" + price + " StopPx=\"" + stop + "\" Acct=\"" + acct + "\">";
                } else {
                    return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" AcctTyp=\"5\" Px=\"" + price + "\" Acct=\"" + acct + "\">";
                }
            } else if (offset != 0) {
                return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" AcctTyp=\"5\"" + "\" Acct=\"" + acct + "\" ExecInst=\"a\">";
            } else {
                return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" AcctTyp=\"5\" Acct=\"" + acct + "\">";
            }
        } else if (price > 0) {
            if (type.equals(PriceType.STOP)) {
                return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" StopPx=\"" + price + "\" Acct=\"" + acct + "\">";

            } else if (type.equals(PriceType.STOP_LIMIT)) {
                return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" Px=\"" + price + "\" StopPx=\"" + stop + "\" Acct=\"" + acct + "\">";
            } else {
                return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" Px=\"" + price + "\" Acct=\"" + acct + "\">";
            }
        } else if (offset != 0) {
            return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" Acct=\"" + acct + "\" ExecInst=\"a\">";
        } else {
            return "<Order TmInForce=\"" + timeinforce + "\" Typ=\"" + type + "\" Side=\"" + side + "\" Acct=\"" + acct + "\">";
        }
    }

    private static String createOrderAtOffset(String acct, String symbol, int quant, TimeInForceField timeinforce, PriceType type, SecurityType sectype, MarketSideField side, double offset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n");
        sb.append("\t" + getOrderLine(timeinforce, type, side, acct, 0, 0, offset) + "\n");
        sb.append("\t\t" + "<PegInstr OfstTyp=\"0\" PegPxTyp=\"1\" OfstVal=\"" + offset + "\"/>\n");
        sb.append("\t\t" + "<Instrmt SecTyp=\"" + sectype + "\" Sym=\"" + symbol + "\"/>\n");
        sb.append("\t\t" + "<OrdQty Qty=\"" + quant + "\"/>\n");
        sb.append("\t" + "</Order>\n");
        sb.append("" + "</FIXML>\n");

        return sb.toString();
    }

    static public FIXMLBuilder buyStockAtMarket(String accountId, String symbol, int quantity) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.MARKET)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.DAY_ORDER);
	return builder;
    }

    static public FIXMLBuilder buyStockAtLimit(String accountId, String symbol, int quantity, double limit) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.LIMIT)
	    .executionPrice(limit)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder buyStockAtStop(String accountId, String symbol, int quantity, double stop) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.STOP)
	    .executionPrice(stop)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder buyStockAtStopLimit(String accountId, String symbol, int quantity, double stop, double limit) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.STOP_LIMIT)
	    .executionPrice(stop, limit)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }


    static public FIXMLBuilder buyStockWithTrailingStopAmount(String accountId, String symbol, int quantity, double offset) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.TRAILING_STOP)
	    .symbol(symbol)
	    .quantity(quantity)
	    .offset(offset)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder buyStockWithTrailingStopPercent(String accountId, String symbol, int quantity, double percent) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.BUY)
	    .priceType(PriceType.TRAILING_STOP)
	    .symbol(symbol)
	    .quantity(quantity)
	    .offset(new Percentage(percent))
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder sellStockAtMarket(String accountId, String symbol, int quantity) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.MARKET)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.DAY_ORDER);
	return builder;
    }

    static public FIXMLBuilder sellStockAtLimit(String accountId, String symbol, int quantity, double limit) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.LIMIT)
	    .executionPrice(limit)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder sellStockAtStop(String accountId, String symbol, int quantity, double stop) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.STOP)
	    .executionPrice(stop)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder sellStockAtStopLimit(String accountId, String symbol, int quantity, double stop, double limit) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.STOP_LIMIT)
	    .executionPrice(stop, limit)
	    .symbol(symbol)
	    .quantity(quantity)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder sellStockWithTrailingStopAmount(String accountId, String symbol, int quantity, double offset) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.TRAILING_STOP)
	    .symbol(symbol)
	    .quantity(quantity)
	    .offset(offset)
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

    static public FIXMLBuilder sellStockWithTrailingStopPercent(String accountId, String symbol, int quantity, double percent) {
	FIXMLBuilder builder = new FIXMLBuilder().id(accountId)
	    .securityType(SecurityType.STOCK)
	    .side(MarketSideField.SELL)
	    .priceType(PriceType.TRAILING_STOP)
	    .symbol(symbol)
	    .quantity(quantity)
	    .offset(new Percentage(percent))
	    .timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
	return builder;
    }

}

package com.celexus.conniption.model.fixml;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.celexus.conniption.model.fixml.FIXMLBuilder;
import com.celexus.conniption.model.fixml.MarketSideField;
import com.celexus.conniption.model.fixml.Percentage;
import com.celexus.conniption.model.fixml.PriceType;
import com.celexus.conniption.model.fixml.SecurityType;
import com.celexus.conniption.model.fixml.TimeInForceField;

public class FIXMLBuilderTest {

    private static String a = null;

    @BeforeClass
    public static void before() throws Exception {
        a = new String("12345678");
    }

    /**
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="1" Side="1" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML> Place
     * a day order to buy 1 share of F at market price on account 12345678.
     */
    @Test
    public void simpleBuyTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Example Place a day order to sell 1 share of F at market price on account
     * 12345678.
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="1" Side="2" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="IBM"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void simpleSellTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.SELL);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"1\" Side=\"2\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";

        assertEquals(expected, builder.build().toString());
    }

    /**
     * Sell Short (opening a short position)
     *
     * Example Place a day order to sell short 1 share of F at $22.00 on account
     * 12345678.
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="2" Side="5" Px="22" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void sellShortAtExecutionPriceTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.LIMIT);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.SELL_SHORT);
        builder.executionPrice(22.0);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"2\" Side=\"5\" Px=\"22.0\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";

        assertEquals(expected, builder.build().toString());
    }

    /**
     * Buy to Cover (closing a short position)
     *
     * Example Place a day order to buy to cover 1 share of F at $13 on account
     * 12345678.
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="2" Side="1" AcctTyp="5" Px="13" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order>
     * </FIXML>
     *
     */
    @Test
    public void buyToCoverAtExecutionPriceTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.LIMIT);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY_TO_COVER);
        builder.executionPrice(13.0);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"2\" Side=\"1\" AcctTyp=\"5\" Px=\"13.0\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";

        assertEquals(expected, builder.build().toString());
    }

    /**
     * Example Place a .50 buy trailing stop on 1 share of IBM (market order
     * will trigger if current price of IBM rises by .50 for current price. If
     * IBM moves down, trigger price will also move down.
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="P" Side="1" Acct="12345678" ExecInst="a">
     * <PegInstr OfstTyp="0" PegPxTyp="1" OfstVal=".50"/> <Instrmt
     * SecTyp="CS" Sym="IBM"/> <OrdQty Qty="1"/> </Order> </FIXML>
     */
    @Test
    public void buyTrailingStopTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("IBM");
        builder.priceType(PriceType.TRAILING_STOP);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);
        builder.offset(.50);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"P\" Side=\"1\" Acct=\"12345678\" ExecInst=\"a\">\n";
        expected += "\t\t<PegInstr OfstTyp=\"0\" PegPxTyp=\"1\" OfstVal=\"0.5\"/>\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"IBM\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";

        assertEquals(expected, builder.build().toString());

    }

    /**
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="1" Typ="P" Side="1" Acct="12345678" ExecInst="a">
     * <PegInstr OfstTyp="1" PegPxTyp="1" OfstVal="5"/> <Instrmt
     * SecTyp="CS" Sym="COST"/> <OrdQty Qty="1"/> </Order> </FIXML> Example
     * Place a 5% buy trailing stop on 1 share of COST (Note: OfstTyp="1" for
     * percentage trailing stop vs. OfstTyp="0" for price value trailing stop).
     */
    @Test
    public void buyTrailingStopPercentageTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
        builder.symbol("COST");
        builder.priceType(PriceType.TRAILING_STOP);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);
        builder.offset(new Percentage(5));

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"1\" Typ=\"P\" Side=\"1\" Acct=\"12345678\" ExecInst=\"a\">\n";
        expected += "\t\t<PegInstr OfstTyp=\"1\" PegPxTyp=\"1\" OfstVal=\"5.0\"/>\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"COST\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Market
     *
     * An order to buy or sell an investment immediately at the best available
     * current price.
     *
     * Example Place a day order to buy 1 share of F at market price on account
     * 12345678
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="1" Side="1" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     */
    @Test
    public void marketTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Example Place a day order to buy 1 share of F at a limit price of $15 on
     * account 12345678
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="2" Side="1" Px="15" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void limitTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.LIMIT);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);
        builder.executionPrice(15.00);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"2\" Side=\"1\" Px=\"15.0\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Example Place a day order to buy 1 share of F with a stop price of $18 on
     * account 12345678
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="3" Side="1" StopPx="18" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void stopTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.STOP);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);
        builder.executionPrice(18.00);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"3\" Side=\"1\" StopPx=\"18.0\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Stop Limit
     *
     * A stop-limit order will be executed at a specified price (or better)
     * after a given stop price has been reached. Once the stop price is
     * reached, the stop-limit order becomes a limit order to buy (or sell) at
     * the limit price or better.
     *
     * Example Place a day order to buy 1 share of F with a stop price of $18
     * and a limit price of $19 on account 12345678.
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="4" Side="1" Px="19" StopPx="18" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order>
     * </FIXML>
     */
    @Test
    public void stopLimitTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.STOP_LIMIT);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);
        builder.executionPrice(18.00, 19.00);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"4\" Side=\"1\" Px=\"19.0\" StopPx=\"18.0\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Day
     *
     * Any order to buy or sell a security that automatically expires if not
     * executed on the day the order is placed.
     *
     * Example Place a day order to buy 1 share of F at market price on account
     * 12345678
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="0" Typ="1" Side="1" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void dayOrderTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.DAY_ORDER);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * Market on Close (SecTyp=CS only)
     *
     * A market order to be executed as near to the end of the exchange day as
     * possible. This is only possible on common stock orders.
     *
     * Example Place market on close order to buy 1 share of F on account
     * 12345678 <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="7" Typ="1" Side="1" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     */
    @Test
    public void marketOnCloseTest() {

        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.MARKET_CLOSE);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"7\" Typ=\"1\" Side=\"1\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

    /**
     * GTC
     *
     * An order to buy or sell a security at a set price that is active until
     * the investor decides to cancel it or the trade is executed.
     *
     * Example Place a good until cancelled order to buy 1 share of F at a limit
     * price of $18 on account 12345678
     *
     * <FIXML xmlns="http://www.fixprotocol.org/FIXML-5-0-SP2">
     * <Order TmInForce="1" Typ="2" Side="1" Px="18" Acct="12345678">
     * <Instrmt SecTyp="CS" Sym="F"/> <OrdQty Qty="1"/> </Order> </FIXML>
     *
     */
    @Test
    public void gtcTest() {
        FIXMLBuilder builder = new FIXMLBuilder();
        builder.id("12345678");
        builder.timeInForce(TimeInForceField.GOOD_TIL_CANCELED);
        builder.symbol("F");
        builder.priceType(PriceType.MARKET);
        builder.securityType(SecurityType.STOCK);
        builder.quantity(1);
        builder.side(MarketSideField.BUY);

        String expected = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">\n";
        expected += "\t<Order TmInForce=\"1\" Typ=\"1\" Side=\"1\" Acct=\"12345678\">\n";
        expected += "\t\t<Instrmt SecTyp=\"CS\" Sym=\"F\"/>\n";
        expected += "\t\t<OrdQty Qty=\"1\"/>\n";
        expected += "\t</Order>\n";
        expected += "</FIXML>\n";
        assertEquals(expected, builder.build().toString());
    }

}

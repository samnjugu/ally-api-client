package com.celexus.conniption.model.fixml;

import java.math.BigDecimal;
import java.util.List;
import org.fixprotocol.fixml.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class FIXMLParserTest {

    @Test public void testParse01() throws Exception {

	FIXML message = FIXMLParser.parse("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\"><ExecRpt AvgPx=\"0\" CumQty=\"0\" LeavesQty=\"1\" TmInForce=\"0\" Px=\"10\" Typ=\"2\" Side=\"1\" AcctTyp=\"2\" Acct=\"12345678\" Stat=\"1\" ID=\"SVI-65454645\" OrdID=\"SVI-456456\"><Instrmt Desc=\"FORD MOTOR COMPANY\" SecTyp=\"CS\" Sym=\"F\"/><OrdQty Qty=\"1\"/><FillsGrp FillQty=\"1\" FillPx=\"0\"/></ExecRpt></FIXML>");

	assertThat(message, is(notNullValue()));
	assertThat(message.getMessage().getValue(), is(instanceOf(ExecutionReportMessageT.class)));

	ExecutionReportMessageT execRpt = (ExecutionReportMessageT)(message.getMessage().getValue());
	assertThat(execRpt.getPx(), is(new BigDecimal(10)));
	assertThat(execRpt.getTmInForce(), is("0"));
	assertThat(execRpt.getTyp(), is("2"));
	assertThat(execRpt.getSide(), is("1"));
	assertThat(execRpt.getOrdID(), is("SVI-456456"));

	InstrumentBlockT instrmt = execRpt.getInstrmt();
	assertThat(instrmt, is(notNullValue()));
	assertThat(instrmt.getDesc(), is("FORD MOTOR COMPANY"));
	assertThat(instrmt.getSecTyp(), is("CS"));
	assertThat(instrmt.getSym(), is("F"));

	OrderQtyDataBlockT ordQty = execRpt.getOrdQty();
        assertThat(ordQty, is(notNullValue()));
	assertThat(ordQty.getQty(), is(new BigDecimal(1)));

	List<FillsGrpBlockT> listFillsGrp = execRpt.getFillsGrp();
	assertThat(listFillsGrp, is(notNullValue()));
	assertThat(listFillsGrp.size(), is(1));
	assertThat(listFillsGrp.get(0).getFillQty(), is(new BigDecimal("1")));
	assertThat(listFillsGrp.get(0).getFillPx(), is(new BigDecimal("0")));

    }

}
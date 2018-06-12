package com.celexus.conniption.model.clock;

import com.celexus.conniption.model.XmlParsingTest;
import com.celexus.conniption.model.util.JAXBUtils;
import java.io.FileInputStream;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.xml.sax.InputSource;

public class ClockResponseTest extends XmlParsingTest {

    @Test public void clockClosedParseTest() throws Exception {
	ClockResponse clock = parse(source("xml/clock_closed.xml"),
				    "com.celexus.conniption.model.clock",
				    ClockResponse.class);
	assertThat(clock, notNullValue());
	assertThat(clock.getElapsedtime(), is(0));
	assertThat(clock.getDate(), is("2016-07-18 18:31:34.2506852-04:00")); //TODO parse this
	assertThat(clock.getStatus(), notNullValue());
	assertThat(clock.getStatus().getCurrent(), is("close")); 
	assertThat(clock.getStatus().getNext(), is("pre")); 
	assertThat(clock.getStatus().getChangeAt(), is("08:00:00")); //TODO parse this
	assertThat(clock.getMessage(), is("Market is open Monday through Friday 9:30AM to 4:00PM EST"));
	assertThat(clock.getUnixtime(), is(1468884694.25069));
	assertThat(clock.getError(), is("Success"));
    }

    /*
    @Test public void clockClosedParseTest() throws Exception {
	ClockResponse clock = parse(source("xml/clock_open.xml"),
				    "com.celexus.conniption.model.clock",
				    ClockResponse.class);
	assertThat(clock, notNullValue());
	assertThat(clock.getElapsedTime(), is(0));
    }
    */

}
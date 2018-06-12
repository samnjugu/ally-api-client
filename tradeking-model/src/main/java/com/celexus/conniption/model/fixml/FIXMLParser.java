package com.celexus.conniption.model.fixml;

import org.fixprotocol.fixml.FIXML;
import com.celexus.conniption.model.util.JAXBUtils;

public class FIXMLParser {

    static public FIXML parse(String fixml) throws Exception {
	return JAXBUtils.getElement("org.fixprotocol.fixml", fixml, null, FIXML.class);
    }

} 
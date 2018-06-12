package com.celexus.conniption.model;

import com.celexus.conniption.model.util.JAXBUtils;
import java.io.File;
import java.io.FileInputStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class XmlParsingTest {
    static private final Logger log = LoggerFactory.getLogger(XmlParsingTest.class);

    static private final File resources = new File("src/test/resources");

    protected InputSource source(String filename) throws Exception {
	File file = new File(resources, filename);
	return new InputSource(new FileInputStream(file));
    }

    protected <T> T parse(InputSource source, String pkg, Class<T> clazz) throws Exception {
	return JAXBUtils.getElement(source, pkg, null, clazz);
    }

}
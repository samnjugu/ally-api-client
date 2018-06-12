package com.celexus.conniption.model.util;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert an XML input to a readable format.
 * 
 * @author cam
 *
 */
public class JAXBUtils {
	static private final Logger log = LoggerFactory.getLogger(JAXBUtils.class);

	/**
	 * Log the the response at trace level. Remove starting non-word characters and
	 * ending non-word characters. Then remove new line, carriage return, tab, form
	 * feed and virtual tab. Then call getElement() method. This step tries to
	 * reduce the chance that JAXB unmarshaller will thow error.
	 * 
	 * Usually return a list of Class<T> that has been unmarshalled into XML format.
	 * <br>
	 * This method is used in getting quote and also in streaming. The response of
	 * streaming is different from quote.
	 * 
	 * @param packageName
	 *            fully qualified package name
	 * @param response
	 *            TKResponse object converted to String.
	 * @param root
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	static public <T> T getElement(String packageName, String response, String root, Class<T> clazz) throws Exception {
		log.trace(response);
		String newResponse = response.replaceFirst("^([\\W]+)<", "<").replaceAll(">([\\W]+)<", "><")
				.replaceAll("\\n|\\r|\\t|\\f|\\v", "");
		// uncomment this line to debug.
		// System.out.println(response);
		return getElement(new InputSource(new StringReader(newResponse)), packageName, root, clazz);
	}

	/**
	 * Parse the XML response into Document, then unmarshal it using an XML format
	 * class and return a JAXBElement<T>. If there is error, the handler will print
	 * the errors.
	 * 
	 * @param <T>
	 * 
	 * @param source
	 *            XML string wrapped in InputSource object.
	 * @param path
	 *            fully qualified package name of clazz
	 * @param root
	 * @param clazz
	 *            a class created using XSD schema. Can be used to get the variable
	 *            "path" if there are multiple objects in the same package.
	 * @return
	 * @throws Exception
	 */
	static public <T> T getElement(InputSource source, String path, String root, Class<T> clazz) throws Exception {
		// these two lines are interchangeable
		// JAXBContext jaxbContext = JAXBContext.newInstance(path);

		JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(source);
		Element element = doc.getDocumentElement();

		// This is not usable at this time because root is always null.
		if (root != null) {
			NodeList nodeList = element.getChildNodes();
			for (int j = 0; j < nodeList.getLength(); j++) {
				System.out.println(j);
				Node childNode = nodeList.item(j);

				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					if (childNode.getNodeName().equals(root)) {
						element = (Element) childNode;

						break;
					}
				}
			}
		}

		// print out the content of all nodes
		// NodeList nodes = element.getChildNodes();
		// for (int i = 0; i < nodes.getLength(); i++) {
		// System.out.println("" + nodes.item(i).getTextContent());
		// }

		// set event handler to print out unmarshalling error.
		jaxbUnmarshaller.setEventHandler(new EmployeeValidationEventHandler());

		JAXBElement<T> t = jaxbUnmarshaller.unmarshal(element, clazz);
		return t.getValue();
	}

	static class EmployeeValidationEventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent event) {
			log.error("\nEVENT");
			log.error("SEVERITY:  " + event.getSeverity());
			log.error("MESSAGE:  " + event.getMessage());
			log.error("LINKED EXCEPTION:  " + event.getLinkedException());
			log.error("LOCATOR");
			log.error("    LINE NUMBER:  " + event.getLocator().getLineNumber());
			log.error("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
			log.error("    OFFSET:  " + event.getLocator().getOffset());
			log.error("    OBJECT:  " + event.getLocator().getObject());
			log.error("    NODE:  " + event.getLocator().getNode());
			log.error("    URL:  " + event.getLocator().getURL());
			return true;
		}
	}

}
package org.springframework.bootstrap.sample;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class XmlSimpleUse {

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource("test-xml-read.xml");
        if (url == null) {
            return;
        }
        InputStream inputStream = url.openStream();
        InputSource inputSource = new InputSource(inputStream);
        DocumentBuilderFactory factory = createDocumentBuilderFactory();

        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        docBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        });
        docBuilder.setErrorHandler(null);
        Document document = docBuilder.parse(inputSource);
        Element root = document.getDocumentElement();
        log.info("root is {}",root);
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                log.info("ele:{}",ele);

            }
        }
        System.out.println(document);
    }



    protected static DocumentBuilderFactory createDocumentBuilderFactory() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        // Enforce namespace aware for XSD...
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);

        return factory;
    }




}

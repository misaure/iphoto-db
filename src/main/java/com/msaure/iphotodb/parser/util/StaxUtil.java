package com.msaure.iphotodb.parser.util;

import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

public class StaxUtil {

	private StaxUtil() {
		
	}

	public static XMLEventReader defaultEventReader(InputStream inputStream) throws XMLStreamException {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
        final XMLEventReader xml = factory.createXMLEventReader(inputStream);
        
        return xml;
	}
}

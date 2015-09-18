package com.msaure.iphotodb.parser.stax;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.msaure.iphotodb.parser.EventType;
import com.msaure.iphotodb.parser.PListReader;
import com.msaure.iphotodb.parser.ParseException;

public class StaxPListReader implements PListReader {

	private final XMLEventReader xml;
	private StaxPListMutableEvent currentEvent;
	private XMLEvent currentXmlEvent;
	private EventType currentEventType;
	private String currentStringValue;
	
	public StaxPListReader(XMLEventReader xml) {
		this.xml = xml;
	}
	
	@Override
	public EventType getEventType() throws ParseException {
		return getEvent().getEventType();
	}

	@Override
	public StaxPListMutableEvent getEvent() throws ParseException {
		if (null == currentEventType) {
			forwardToFirstElementIfAtStart();
		}
		return currentEvent;
	}

	@Override
	public boolean next() throws ParseException {
		if (null == currentEventType) {
			forwardToFirstElementIfAtStart();
		}
		
		if (!xml.hasNext()) {
			return false;
		}
		
		currentEvent = consumeNext();
		
		if (null != currentEvent) {
			currentEventType = currentEvent.getEventType();
		}
		
		return (null != currentEvent);
	}

	protected void forwardToFirstElementIfAtStart() throws ParseException {
		try {
			skipToFirstElement();
			currentEventType = EventType.PLIST_START;
			next();
		}
		catch(XMLStreamException e) {
			throw ParseException.coerce(e);
		}
	}
	
	protected StaxPListMutableEvent consumeNext() throws ParseException {
		try {
			currentXmlEvent = xml.nextEvent();
			while (currentXmlEvent.isCharacters()) {
				currentXmlEvent = xml.nextEvent();
			}
		
			if (currentXmlEvent.isEndDocument()) {
				return null;
				
			} else if (currentXmlEvent.isEndElement()) {
				final EndElement element = currentXmlEvent.asEndElement();
				final String localName = element.getName().getLocalPart();
				
				if ("plist".equals(localName)) {
					return null;
				}
				
				switch(localName) {
				case "dict":
					return StaxPListMutableEvent.createEvent().endDictionary();
				case "array":
					return StaxPListMutableEvent.createEvent().endArray();
				default:
					throw new ParseException("unexpected end element " + localName);	
				}
				
			} else if (currentXmlEvent.isStartElement()) {
				currentStringValue = null;
				
				final StartElement element = currentXmlEvent.asStartElement();
				final String localName = element.getName().getLocalPart();
				
				switch(localName) {
				case "dict":
					return StaxPListMutableEvent.createEvent().startDictionary();
				case "array":
					return StaxPListMutableEvent.createEvent().startArray();
				case "key":
					return StaxPListMutableEvent.createEvent().key(readOptionalStringElement(xml, currentXmlEvent, "key"));
				case "integer":
					return StaxPListMutableEvent.createEvent().integer(readMandatoryStringElement(xml, currentXmlEvent, "integer"));
				case "real":
					return StaxPListMutableEvent.createEvent().real(readMandatoryStringElement(xml, currentXmlEvent, "real"));
				case "string":
					return StaxPListMutableEvent.createEvent().string(readOptionalStringElement(xml, currentXmlEvent, "string"));
				case "true":
					return StaxPListMutableEvent.createEvent().trueLiteral();
				case "false":
					return StaxPListMutableEvent.createEvent().falseLiteral();
				case "date":
					//currentEvent = StaxPListMutableEvent.createEvent().date(readMandatoryStringElement(xml, currentXmlEvent, "date"));
					return null; //FIXME: .date not implemented
				case "data":
					return StaxPListMutableEvent.createEvent().data(readMandatoryStringElement(xml, currentXmlEvent, "data"));
				}
			}
			
			return null;
		}
		catch(XMLStreamException e) {
			throw ParseException.coerce(e);
		}
	}
	
	private String readMandatoryStringElement(XMLEventReader xml, XMLEvent xmlEvent, String localName) throws ParseException {
		try {
			XMLEvent event = xml.nextEvent();
			
			if (event.isEndElement()) {
				throw new ParseException("empty " + localName + " not allowed");
				
			} else if (event.isCharacters()) {
				this.currentStringValue = event.asCharacters().getData();
				matchEndElement(xml.nextEvent(), localName);
			}
			
			return this.currentStringValue;
		}
		catch(XMLStreamException e) {
			throw ParseException.coerce(e);
		}
	}

	protected String readOptionalStringElement(XMLEventReader xml, XMLEvent xmlEvent, String localName) throws ParseException {		
		try {
			XMLEvent event = xml.nextEvent();
			
			if (event.isEndElement()) {
				this.currentStringValue = "";
				
			} else if (event.isCharacters()) {
				this.currentStringValue = event.asCharacters().getData();
				matchEndElement(xml.nextEvent(), localName);
			}
			
			return this.currentStringValue;
		}
		catch(XMLStreamException e) {
			throw ParseException.coerce(e);
		}
	}

	protected void matchEndElement(XMLEvent event, String expectedLocalName) throws ParseException {
		if (!event.isEndElement()) {
			throw new ParseException("expected </" + expectedLocalName + ">, but got event of type " + event.toString());
		}
		
		final EndElement element = event.asEndElement();
		final String actualLocalName = element.getName().getLocalPart();
		
		if (!expectedLocalName.equals(actualLocalName)) {
			throw new ParseException("expected </" + expectedLocalName + ">, but got </" + actualLocalName + ">");
		}
	}

	protected void skipToFirstElement() throws XMLStreamException {
		if (null == currentXmlEvent) {
			currentXmlEvent = xml.nextEvent();
		}
		
		for (;;) {
            if (currentXmlEvent.isStartElement()) {
            	StartElement element = currentXmlEvent.asStartElement();
            	if (!"plist".equals(element.getName().getLocalPart())) {
                    throw new RuntimeException("album file must start with 'plist', was " + element.getName().getLocalPart()); // TODO use proper exception type
                } else {
                	break;
                }
            }
    		
            currentXmlEvent = xml.nextEvent();
		}
	}

}

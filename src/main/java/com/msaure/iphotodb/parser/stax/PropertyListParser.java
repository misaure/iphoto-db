package com.msaure.iphotodb.parser.stax;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class PropertyListParser {

    private static final QName PLIST_ELEMENT = new QName("plist");
    private static final QName KEY_ELEMENT = new QName("key");
    private static final QName STRING_ELEMENT = new QName("string");
    private static final QName INTEGER_ELEMENT = new QName("integer");
    private static final QName REAL_ELEMENT = new QName("real");
    private static final QName DATA_ELEMENT = new QName("data");
    private static final QName ARRAY_ELEMENT = new QName("array");
    private static final QName DICTIONARY_ELEMENT = new QName("dict");
    private static final QName FALSE_ELEMENT = new QName("false");
    private static final QName TRUE_ELEMENT = new QName("true");

    private final XMLEventReader xml;
    private XMLEvent event;
    private Stack<ContainerType> containers;

    protected PropertyListParser(InputStream inputStream) throws XMLStreamException
    {
        XMLInputFactory factory = XMLInputFactory.newInstance();
		this.xml = factory.createXMLEventReader(inputStream);
    }

    public static PropertyListParser forInputStream(InputStream inputStream) throws XMLStreamException
    {
        return new PropertyListParser(inputStream);
    }

    public void parse(PropertyListParserHandler handler) throws IOException, XMLStreamException
    {
        this.containers = new Stack<>();

        matchStartElement(PLIST_ELEMENT);
        while (xml.hasNext()) {
            event = xml.nextEvent();

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();

                if (KEY_ELEMENT.equals(element.getName())) {
                    String keyName = parseKey();
                    System.out.println("key: " + keyName);

                } else if (STRING_ELEMENT.equals(element.getName())) {
                    String stringValue = parseString();
                    System.out.println("string: " + stringValue);

                } else if (INTEGER_ELEMENT.equals(element.getName())) {
                    Integer integerValue = parseInteger();
                    System.out.println("integer: " + integerValue.toString());

                } else if (REAL_ELEMENT.equals(element.getName())) {
                    // TODO

                } else if (DATA_ELEMENT.equals(element.getName())) {
                    // TODO

                } else if (FALSE_ELEMENT.equals(element.getName())) {
                    // TODO

                } else if (TRUE_ELEMENT.equals(element.getName())) {
                    // TODO

                } else if (DICTIONARY_ELEMENT.equals(element.getName())) {
                    containers.push(ContainerType.DICTIONARY);
                    System.out.println("dict begin");

                } else if (ARRAY_ELEMENT.equals(element.getName())) {
                    containers.push(ContainerType.ARRAY);
                    System.out.println("array begin") ;

                }

            } else if (event.isEndElement()) {
                final EndElement element = event.asEndElement();

                if (DICTIONARY_ELEMENT.equals(element.getName())) {
                    containers.pop();
                    System.out.println("dict end");

                } else if (ARRAY_ELEMENT.equals(element.getName())) {
                    containers.pop();
                    System.out.println("array end");
                }
            }
        }
    }

    protected void matchStartElement(QName elementName) throws XMLStreamException {
        if (skipToStartElement()) {
            if (event.isStartElement()) {
                if (!elementName.equals(event.asStartElement().getName())) {
                    throw new RuntimeException("unexpected element " + event.asStartElement().getName().toString());
                }
            } else {
                throw new RuntimeException("expected start element");
            }
        } else {
            throw new RuntimeException("reached end of input while expecting start element " + elementName.toString());
        }
    }

    protected void matchEndElement(QName elementName) throws XMLStreamException {
        if (skipToEndElement()) {
            if (event.isEndElement()) {
                if (!elementName.equals(event.asEndElement().getName())) {
                    throw new RuntimeException("unexpected end element " + event.asEndElement().getName().toString());
                }
            } else {
                throw new RuntimeException("expected end element");
            }
        } else {
            throw new RuntimeException("reached end of input while expected end element " + elementName.toString());
        }
    }

    protected boolean skipToStartElement() throws XMLStreamException {
        if (null == event) {
            event = xml.nextEvent();
        }

        while (xml.hasNext() && !event.isStartElement()) {
            event = xml.nextEvent();
        }

        return event.isStartElement();
    }

    protected boolean skipToEndElement() throws XMLStreamException {
        if (null == event) {
            event = xml.nextEvent();
        }

        while (xml.hasNext() && !event.isEndElement()) {
            event = xml.nextEvent();
        }

        return event.isEndElement();
    }

    protected boolean skipToCharacters() throws XMLStreamException {
        if (null == event) {
            event = xml.nextEvent();
        }

        while (xml.hasNext() && !event.isCharacters()) {
            event = xml.nextEvent();
        }

        return event.isCharacters();
    }

    protected String parseKey() throws XMLStreamException
    {
        String keyName = null;

        matchStartElement(KEY_ELEMENT);
        if (skipToCharacters()) {
            keyName = event.asCharacters().getData();
        }
        matchEndElement(KEY_ELEMENT);

        return keyName;
    }

    protected String parseString() throws XMLStreamException
    {
        String stringValue = null;

        matchStartElement(STRING_ELEMENT);
        if (skipToCharacters()) {
            stringValue = event.asCharacters().getData();
        }
        matchEndElement(STRING_ELEMENT);

        return stringValue;
    }

    protected Integer parseInteger() throws XMLStreamException
    {
        Integer integerValue = null;

        matchStartElement(INTEGER_ELEMENT);
        if (skipToCharacters()) {
            integerValue = Integer.parseInt(event.asCharacters().getData());
        }
        matchEndElement(INTEGER_ELEMENT);

        return integerValue;
    }

    static enum ContainerType {
        DICTIONARY, ARRAY;
    }
}

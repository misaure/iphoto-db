package com.msaure.iphotodb.parser.stax;

import com.msaure.iphotodb.parser.PropertyListEvent;
import com.msaure.iphotodb.parser.PropertyListEventType;
import java.io.File;
import java.io.FileInputStream;

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
    private PropertyListEvent lookahead;
    private State state = State.NEW;

    protected PropertyListParser(InputStream inputStream) throws XMLStreamException
    {
        XMLInputFactory factory = XMLInputFactory.newInstance();
		this.xml = factory.createXMLEventReader(inputStream);
        this.containers = new Stack<>();
    }

    public static PropertyListParser forInputStream(InputStream inputStream) throws XMLStreamException
    {
        return new PropertyListParser(inputStream);
    }

    public PropertyListEvent nextEvent() throws IOException, XMLStreamException {
        if (null == this.lookahead) {
            if (State.AT_END != this.state) {
                PropertyListEvent next = readEvent();
                lookahead = readEvent();
                
                return next;
            } else {
                return null;
            }
        } else {
            PropertyListEvent next = this.lookahead;
            this.lookahead = readEvent();
            
            return next;
        }
    }
    
    protected PropertyListEvent readEvent() throws IOException, XMLStreamException {
        return parse(null);
    }

    public boolean hasNext()
    {
        return State.AT_END != this.state;
    }

    /** @deprecated */
    @Deprecated
    public PropertyListEvent parse(PropertyListParserHandler handler) throws IOException, XMLStreamException
    {
        //this.containers = new Stack<>();

        // matchStartElement(PLIST_ELEMENT);
        while (xml.hasNext()) {
            event = xml.nextEvent();

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();

                if (KEY_ELEMENT.equals(element.getName())) {
                    String keyName = parseKey();
                    //System.out.println("key: " + keyName);
                    return PropertyListEvent.forKey(this, keyName);

                } else if (STRING_ELEMENT.equals(element.getName())) {
                    String stringValue = parseString();
                    //System.out.println("string: " + stringValue);
                    return PropertyListEvent.forString(this, stringValue);

                } else if (INTEGER_ELEMENT.equals(element.getName())) {
                    Integer integerValue = parseInteger();
                    //System.out.println("integer: " + integerValue.toString());
                    return PropertyListEvent.forInteger(this, integerValue);

                } else if (REAL_ELEMENT.equals(element.getName())) {
                    // TODO
                    return null;

                } else if (DATA_ELEMENT.equals(element.getName())) {
                    String dataAsString = parseData();
                    //System.out.println("data: " + dataAsString);
                    return PropertyListEvent.forData(this, dataAsString);

                } else if (FALSE_ELEMENT.equals(element.getName())) {
                    // TODO
                    return null;

                } else if (TRUE_ELEMENT.equals(element.getName())) {
                    // TODO
                    return null;

                } else if (DICTIONARY_ELEMENT.equals(element.getName())) {
                    containers.push(ContainerType.DICTIONARY);
                    //System.out.println("dict begin");
                    return PropertyListEvent.forEventType(this, PropertyListEventType.DICTIONARY_START);

                } else if (ARRAY_ELEMENT.equals(element.getName())) {
                    containers.push(ContainerType.ARRAY);
                    //System.out.println("array begin");
                    return PropertyListEvent.forEventType(this, PropertyListEventType.ARRAY_START);
                }

            } else if (event.isEndElement()) {
                final EndElement element = event.asEndElement();

                if (DICTIONARY_ELEMENT.equals(element.getName())) {
                    containers.pop();
                    //System.out.println("dict end");
                    return PropertyListEvent.forEventType(this, PropertyListEventType.DICTIONARY_END);

                } else if (ARRAY_ELEMENT.equals(element.getName())) {
                    containers.pop();
                    //System.out.println("array end");
                    return PropertyListEvent.forEventType(this, PropertyListEventType.ARRAY_END);
                    
                } else if (PLIST_ELEMENT.equals(element.getName())) {
                    this.state = State.AT_END;
                    return null;
                }
            }
        }

        return null;
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

    protected String parseData() throws XMLStreamException
    {
        String stringValue = null;

        matchStartElement(DATA_ELEMENT);
        if (skipToCharacters()) {
            stringValue = event.asCharacters().getData();
        }
        matchEndElement(DATA_ELEMENT);

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

    static enum State {
        NEW, START_PLIST, INSIDE_PLIST, AT_END;
    }
    
    public static void main(String[] args) {
        for (String fileName: args) {
            try (InputStream input = new FileInputStream(new File(fileName))) {
                PropertyListParser parser = PropertyListParser.forInputStream(input);
                
                while (parser.hasNext()) {
                    System.out.println(parser.nextEvent().toString());
                }
            }
            catch(IOException | XMLStreamException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.msaure.iphotodb.parser;

import com.msaure.iphotodb.parser.stax.PropertyListParser;
import java.util.EventObject;

public class PropertyListEvent extends EventObject {

    private PropertyListEventType eventType;
    private String stringValue;
    private Integer integerValue;

    protected PropertyListEvent(PropertyListParser source, PropertyListEventType eventType, String stringValue) {
        this(source, eventType);
        this.eventType = eventType;
        this.stringValue = stringValue;
    }

    protected PropertyListEvent(PropertyListParser source, PropertyListEventType eventType, Integer integerValue) {
        this(source, eventType);
        this.integerValue = integerValue;
        this.stringValue = Integer.toString(integerValue);
    }

    protected PropertyListEvent(PropertyListParser source, PropertyListEventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    public PropertyListEventType getEventType() {
        return this.eventType;
    }

    public String contentAsString() {
        return stringValue;
    }

    public Integer contentAsInteger() {
        return integerValue;
    }

    public byte[] contentAsByteArray() {
        return null;
    }

    public static PropertyListEvent forKey(PropertyListParser parser, String keyName) {
        return new PropertyListEvent(parser, PropertyListEventType.KEY, keyName);
    }

    public static PropertyListEvent forInteger(PropertyListParser parser, Integer integerValue) {
        return new PropertyListEvent(parser, PropertyListEventType.INTEGER, integerValue);
    }

    public static PropertyListEvent forString(PropertyListParser parser, String stringValue) {
        return new PropertyListEvent(parser, PropertyListEventType.STRING, stringValue);
    }

    public static PropertyListEvent forEventType(PropertyListParser parser, PropertyListEventType eventType) {
        return new PropertyListEvent(parser, eventType);
    }

    public static PropertyListEvent forData(PropertyListParser parser, String dataAsString) {
        return new PropertyListEvent(parser, PropertyListEventType.DATA, dataAsString);
    }
}

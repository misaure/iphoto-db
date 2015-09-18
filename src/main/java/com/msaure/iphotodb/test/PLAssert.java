package com.msaure.iphotodb.test;

import com.msaure.iphotodb.parser.PropertyListEvent;
import com.msaure.iphotodb.parser.PropertyListEventType;
import org.junit.Assert;

public class PLAssert {

    public static void assertEventType(PropertyListEventType eventType, PropertyListEvent event) {
        Assert.assertEquals(eventType, event.getEventType());
    }

    public static void assertEventTypeWithStringValue(PropertyListEventType eventType, String stringValue, PropertyListEvent event) {
        assertEventType(eventType, event);
        Assert.assertEquals(stringValue, event.contentAsString());
    }

    public static void assertKeyEvent(String expectedKeyName, PropertyListEvent event) {
        assertEventTypeWithStringValue(PropertyListEventType.KEY, expectedKeyName, event);
    }

    public static void assertStringEvent(String expectedContent, PropertyListEvent event) {
        assertEventType(PropertyListEventType.STRING, event);
        Assert.assertEquals(expectedContent, event.contentAsString());
    }

    public static void assertIntegerEvent(int expectedValue, PropertyListEvent event) {
        assertEventType(PropertyListEventType.INTEGER, event);
        Assert.assertEquals(Integer.valueOf(expectedValue), event.contentAsInteger());
    }

    public static void assertDataEvent(String expectedStringContent, PropertyListEvent event) {
        assertEventType(PropertyListEventType.DATA, event);
        Assert.assertEquals(expectedStringContent, event.contentAsString().trim());
    }

    public static void assertDataEvent(byte[] expectedByteContent, PropertyListEvent event) {
        assertEventType(PropertyListEventType.DATA, event);
        Assert.assertArrayEquals(expectedByteContent, event.contentAsByteArray());
    }

}

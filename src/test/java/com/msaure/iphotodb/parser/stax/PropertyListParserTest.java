package com.msaure.iphotodb.parser.stax;

import java.io.InputStream;

import com.msaure.iphotodb.parser.PropertyListEventType;
import static com.msaure.iphotodb.test.PLAssert.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PropertyListParserTest {

    @Test
    public void testPlist2() throws Exception
    {
        InputStream sampleFile = getClass().getResourceAsStream("/com/msaure/iphotodb/parser/stax/plist2.xml");
        assertNotNull(sampleFile);

        PropertyListParser parser = PropertyListParser.forInputStream(sampleFile);
        assertNotNull(parser);

        assertTrue(parser.hasNext());

        //assertEventType(PropertyListEventType.DOCUMENT_START, parser.nextEvent());
        assertEventType(PropertyListEventType.DICTIONARY_START, parser.nextEvent());
        assertKeyEvent("Year Of Birth", parser.nextEvent());
        assertIntegerEvent(1965, parser.nextEvent());
        assertKeyEvent("Pets Names", parser.nextEvent());
        assertEventType(PropertyListEventType.ARRAY_START, parser.nextEvent());
        assertEventType(PropertyListEventType.ARRAY_END, parser.nextEvent());
        assertKeyEvent("Picture", parser.nextEvent());
        assertDataEvent("PEKBpYGlmYFCPA==", parser.nextEvent());
        assertKeyEvent("City of Birth", parser.nextEvent());
        assertStringEvent("Springfield", parser.nextEvent());
        assertKeyEvent("Name", parser.nextEvent());
        assertStringEvent("John Doe", parser.nextEvent());
        assertKeyEvent("Kids Names", parser.nextEvent());
        assertEventType(PropertyListEventType.ARRAY_START, parser.nextEvent());
        assertStringEvent("John", parser.nextEvent());
        assertStringEvent("Kyra", parser.nextEvent());
        assertEventType(PropertyListEventType.ARRAY_END, parser.nextEvent());
        assertEventType(PropertyListEventType.DICTIONARY_END, parser.nextEvent());
        //assertEventType(PropertyListEventType.DOCUMENT_END, parser.nextEvent());

        assertFalse(parser.hasNext());
    }
}

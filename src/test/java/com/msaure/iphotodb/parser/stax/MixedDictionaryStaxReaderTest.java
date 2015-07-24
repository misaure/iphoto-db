package com.msaure.iphotodb.parser.stax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.msaure.iphotodb.parser.EventType;
import com.msaure.iphotodb.parser.ParseException;
import com.msaure.iphotodb.parser.util.StaxUtil;

public class MixedDictionaryStaxReaderTest {

	InputStream sampleFile;
	StaxPListReader reader;
	
	@Before
	public void setUp() throws XMLStreamException {
		sampleFile = getClass().getResourceAsStream("/com/msaure/iphotodb/parser/stax/plist2.xml");
		assertNotNull(sampleFile);
		
		reader = new StaxPListReader(StaxUtil.defaultEventReader(sampleFile));
	}
	
	@Test
	public void thatRootElementIsCorrectlyRecognizedAsRootElement() throws ParseException {
		final StaxPListMutableEvent event = reader.getEvent();
		
		assertNotNull(event);
		assertEquals(EventType.DICTIONARY_START, event.getEventType());
		assertEquals(EventType.DICTIONARY_START, reader.getEventType());
		assertTrue(event.isStartElement());
		assertFalse(event.isLiteral());
	}
	
	@Test
	public void thatFirstElementIsRecognizedAsInteger() throws ParseException {
		assertTrue(reader.next());
		//assertTrue(reader.next());
		
		final StaxPListMutableEvent event = reader.getEvent();
		
		assertNotNull(event);
		assertEquals(EventType.KEY, event.getEventType());
		assertEquals("Year Of Birth", event.getStringValue());
		
		assertTrue(reader.next());
		
		final StaxPListMutableEvent event2 = reader.getEvent();
		assertNotNull(event2);
		assertEquals(EventType.INTEGER, event2.getEventType());
		assertNotNull(event2.getIntegerValue());
		assertEquals(Integer.valueOf(1965), event2.getIntegerValue());
	}
}

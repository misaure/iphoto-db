package com.msaure.iphotodb.parser;

import com.msaure.iphotodb.parser.stax.StaxPListMutableEvent;

public interface PListReader {

	EventType getEventType() throws ParseException;
	
	StaxPListMutableEvent getEvent() throws ParseException;
	
	boolean next() throws ParseException;
	
}

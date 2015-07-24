package com.msaure.iphotodb.parser;

public interface PListEvent {

	boolean isStartElement();
	
	boolean isEndElement();
	
	boolean isKey();
	
	boolean isLiteral();
	
	ValueType getValueType();
	
	EventType getEventType();

}

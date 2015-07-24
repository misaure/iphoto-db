package com.msaure.iphotodb.parser;

public enum EventType {

	PLIST_START, PLIST_END, DICTIONARY_START, DICTIONARY_END, ARRAY_START, ARRAY_END,
	KEY,
	STRING, REAL, INTEGER, DATE, TRUE, FALSE, DATA;
	
	
}

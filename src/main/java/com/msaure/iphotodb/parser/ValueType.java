package com.msaure.iphotodb.parser;

@Deprecated
public enum ValueType {
	STRING, REAL, INTEGER, DATE, BOOLEAN, KEY, DATA, ARRAY, DICTIONARY;
	
	public boolean isAtomic(ValueType v) {
		switch (v) {
		case STRING:
		case REAL:
		case INTEGER:
		case DATE:
		case BOOLEAN:
		case DATA:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isKey(ValueType v) {
		return KEY == v;
	}
	
	public boolean isContainer(ValueType v) {
		switch (v) {
		case ARRAY:
		case DICTIONARY:
			return true;
		default:
			return false;
		}
	}
	
}

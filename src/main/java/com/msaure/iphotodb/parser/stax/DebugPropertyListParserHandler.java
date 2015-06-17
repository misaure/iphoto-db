package com.msaure.iphotodb.parser.stax;

public class DebugPropertyListParserHandler implements
		PropertyListParserHandler {

	@Override
	public void keyNamed(PropertyListParser propertyListParser, String keyName) {
		System.out.println("key: " + keyName);
	}

	@Override
	public void stringLiteral(PropertyListParser propertyListParser,
			String stringValue) {
		System.out.println("string: " + stringValue);
	}

	@Override
	public void integerLiteral(PropertyListParser propertyListParser,
			Integer integerValue) {
		System.out.println("integer: " + integerValue.toString());
	}

	@Override
	public void dictionaryStart(PropertyListParser propertyListParser) {
		System.out.println("dict begin");
	}

	@Override
	public void dictionaryEnd(PropertyListParser propertyListParser) {
		System.out.println("dict end");
	}

	@Override
	public void arrayStart(PropertyListParser propertyListParser) {
		System.out.println("array begin") ;
	}

	@Override
	public void arrayEnd(PropertyListParser propertyListParser) {
		System.out.println("array end");
	}

}

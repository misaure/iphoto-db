package com.msaure.iphotodb.parser.stax;

/**
 * @deprecated Migrating from callback-based design to stream-based design
 */
public interface PropertyListParserHandler {

	void keyNamed(PropertyListParser propertyListParser, String keyName);

	void stringLiteral(PropertyListParser propertyListParser, String stringValue);

	void integerLiteral(PropertyListParser propertyListParser,
			Integer integerValue);

	void dictionaryStart(PropertyListParser propertyListParser);

	void dictionaryEnd(PropertyListParser propertyListParser);

	void arrayStart(PropertyListParser propertyListParser);

	void arrayEnd(PropertyListParser propertyListParser);

}

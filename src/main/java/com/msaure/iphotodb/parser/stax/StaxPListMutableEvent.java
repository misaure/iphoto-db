package com.msaure.iphotodb.parser.stax;

import java.io.IOException;

import com.dd.plist.Base64;
import com.msaure.iphotodb.parser.EventType;
import com.msaure.iphotodb.parser.ParseException;

public class StaxPListMutableEvent implements java.io.Serializable {

	private static final long serialVersionUID = 5373693227698769079L;

	private final EventType eventType;
	private Boolean booleanValue;
	private Double realValue;
	private Integer integerValue;
	private byte[] dataValue;
	private String stringValue;
	
	public StaxPListMutableEvent(EventType eventType) {
		this.eventType = eventType;
	}
	
	protected StaxPListMutableEvent(EventType eventType, Boolean booleanValue) {
		this(eventType);
		this.booleanValue = booleanValue;
	}
	
	protected StaxPListMutableEvent(EventType eventType, Double realValue) {
		this(eventType);
		this.realValue = realValue;
	}
	
	protected StaxPListMutableEvent(EventType eventType, Integer integerValue) {
		this(eventType);
		this.integerValue = integerValue;
	}
	
	protected StaxPListMutableEvent(EventType eventType, byte[] dataValue) {
		this(eventType);
		this.dataValue = dataValue;
	}
	
	protected StaxPListMutableEvent(EventType eventType, String stringValue) {
		this(eventType);
		this.stringValue = stringValue;
	}
		
	public StaxPListMutableEvent(StaxPListMutableEvent src) {
		this.eventType = src.eventType;
	}
	
	public boolean isStartElement() {
		return eventType == EventType.ARRAY_START || eventType == EventType.DICTIONARY_START;
	}

	public boolean isEndElement() {
		return eventType == EventType.ARRAY_END || eventType == EventType.DICTIONARY_END;
	}

	public boolean isKey() {
		return EventType.KEY == eventType;
	}

	public boolean isLiteral() {
		return EventType.INTEGER == eventType || EventType.REAL == eventType || EventType.STRING == eventType
				|| EventType.TRUE == eventType || EventType.FALSE == eventType || EventType.DATA == eventType;
	}

	
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public Double getRealValue() {
		return realValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public byte[] getDataValue() {
		return dataValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public EventType getEventType() {
		return eventType;
	}

	public static Builder createEvent() {
		return new Builder();
	}
	
	public static class Builder {
		
		EventType eventType;
		Double doubleValue;
		Integer integerValue;
		Boolean booleanValue;
		byte[] dataValue;
		String stringValue;
		StaxPListMutableEvent event;
		
		public StaxPListMutableEvent real(String valueAsString) {
			return new StaxPListMutableEvent(EventType.REAL, Double.parseDouble(valueAsString));
		}
		
		public StaxPListMutableEvent integer(String valueAsString) {
			return new StaxPListMutableEvent(EventType.INTEGER, Integer.parseInt(valueAsString));
		}
		
		public StaxPListMutableEvent data(String base64Data) throws ParseException {
			this.eventType = EventType.DATA;
			try {
				return new StaxPListMutableEvent(EventType.DATA, Base64.decode(base64Data));
			}
			catch(IOException e) {
				throw ParseException.coerce(e);
			}
		}
		
		public StaxPListMutableEvent key(String keyName) {
			return new StaxPListMutableEvent(EventType.KEY, keyName);
		}
		
		public StaxPListMutableEvent string(String value) {
			return new StaxPListMutableEvent(EventType.STRING, value);
		}
		
		public StaxPListMutableEvent falseLiteral() {
			return new StaxPListMutableEvent(EventType.FALSE, Boolean.FALSE);
		}
		
		public StaxPListMutableEvent trueLiteral() {
			return new StaxPListMutableEvent(EventType.TRUE, Boolean.TRUE);
		}
		
		public StaxPListMutableEvent startArray() {
			return new StaxPListMutableEvent(EventType.ARRAY_START);
		}
		
		public StaxPListMutableEvent endArray() {
			return new StaxPListMutableEvent(EventType.ARRAY_END);
		}
		
		public StaxPListMutableEvent startDictionary() {
			return new StaxPListMutableEvent(EventType.DICTIONARY_START);
		}
		
		public StaxPListMutableEvent endDictionary() {
			return new StaxPListMutableEvent(EventType.DICTIONARY_END);
		}
	}
}

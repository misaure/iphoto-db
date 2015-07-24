package com.msaure.iphotodb.parser;

public class ParseException extends Exception {

	private static final long serialVersionUID = 857815698685228219L;

	public ParseException() {
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public static ParseException coerce(Throwable t) {
		if (t instanceof ParseException) {
			return (ParseException) t;
		}
		return new ParseException(t);
	}
}

package com.muffin.reditclone.exception;

public class SpringRedditException extends RuntimeException {

	public SpringRedditException(String message) {
		super(message);
	}
	
	public SpringRedditException(String message, Exception e) {
		super(message, e);
	}
}

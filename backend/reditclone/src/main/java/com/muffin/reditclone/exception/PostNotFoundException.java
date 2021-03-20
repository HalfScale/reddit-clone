package com.muffin.reditclone.exception;

public class PostNotFoundException extends RuntimeException{

	public PostNotFoundException(String message) {
		super(message);
	}
}

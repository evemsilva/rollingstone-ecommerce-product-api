package com.rollingstone.exceptions;

import java.util.Date;

public class RestAPIExceptionInfo {

    private final Date   timestamp;
    private final String message;
    private final String details;

    public RestAPIExceptionInfo(Date timestamp, String message, String details) {
	super();
	this.timestamp = timestamp;
	this.message = message;
	this.details = details;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public String getMessage() {
	return message;
    }

    public String getDetails() {
	return details;
    }

}

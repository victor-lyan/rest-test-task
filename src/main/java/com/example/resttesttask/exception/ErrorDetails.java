package com.example.resttesttask.exception;

import java.util.Date;

public class ErrorDetails {
    
    private Date timestamp;
    private String message;
    private String details;
    private int errorCode;

    public ErrorDetails(Date timestamp, String message, String details, int errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
}

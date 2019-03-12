package com.example.resttesttask.dto;

public class ResultDto {
    
    public static String STATUS_SUCCESS = "SUCCESS";
    public static String STATUS_ERROR = "ERROR";
    
    private String message;
    private String status;
    
    public ResultDto() {
        
    }

    public ResultDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

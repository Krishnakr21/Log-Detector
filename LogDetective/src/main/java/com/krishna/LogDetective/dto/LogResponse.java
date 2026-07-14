package com.krishna.LogDetective.dto;


public class LogResponse {

    private String result;

    public LogResponse() {}

    public LogResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
package com.spring.reactive.registration.document;

import java.util.Date;

public class ExceptionEntity {

private Date date;
    private  String message;
    private String result;

    public ExceptionEntity(Date date, String message, String result) {
        this.date = date;
        this.message = message;
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

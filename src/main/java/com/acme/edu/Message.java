package com.acme.edu;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String data;
    private LocalDateTime date;

    public Message(String data) {
        this.data = data;
    }

    public Message(String data, LocalDateTime date) {
        this.data = data;
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

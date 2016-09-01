package com.acme.edu;

import java.time.LocalDateTime;

public class Message {
    String data;
    LocalDateTime date;

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

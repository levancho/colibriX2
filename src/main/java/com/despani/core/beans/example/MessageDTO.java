package com.despani.core.beans.example;

import lombok.Data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Date date;
    public String content;
    public String to;
    public String from;

    public MessageDTO() {
        this.date = Calendar.getInstance().getTime();
    }
}
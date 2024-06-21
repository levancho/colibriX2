package com.despani.x2.core.beans.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Greeting {
    private String content;

    public Greeting() {
        log.info("created");
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

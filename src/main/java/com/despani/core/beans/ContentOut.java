package com.despani.core.beans;

import lombok.Getter;

public class ContentOut {

    @Getter
    protected String introtext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,

    @Getter
    protected String fulltext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,

    public ContentOut(String introtext, String fulltext) {
        this.introtext = introtext;
        this.fulltext = fulltext;
    }
}

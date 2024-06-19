package com.despani.core.beans.form;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;


@Data
public class ContentForm extends ABaseForm {

    @NotEmpty(message = "Intro Text is Required Field")
    protected String introtext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,

//    @NotEmpty(message = "Full Text is Required Field")
    protected String fulltext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,

//    @NotNull
    protected String state;  //tinyint(3) NOT NULL DEFAULT 0,
    protected boolean featured;   // tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Set if article is featured.',

    @NotEmpty(message = "Title is Required Field")
    protected String title; // varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
    protected String alias;  // varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
    protected boolean published;

    protected Integer categoryTypeOid=-1;
    protected Integer catoid=-1;
    protected Map<String,String> props = new HashMap<>();

}

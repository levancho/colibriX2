package com.despani.core.beans.domains;

import com.despani.core.beans.base.ADespaniContentObject;
import com.despani.core.beans.enums.DespContentTypes;
import com.despani.core.utils.DespPropertyX2;
import lombok.Data;

import java.util.List;

@Data
public class DespSettings extends ADespaniContentObject {
    protected String introtext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    protected String fulltext; // mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    protected boolean featured;   // tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Set if article is featured.',
    //    protected int catoid;  // int(10) UNSIGNED NOT NULL DEFAULT 0,

    protected DespCategoryItem category;

    protected List<DespPropertyX2> properties;

    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.CONTENT;
    }

}

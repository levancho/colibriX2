package com.despani.x2.core.beans.base;

import lombok.Data;

// TODO [Zaccone] : where are Lombok constructors?

@Data
public abstract class ADespaniContentObject extends ADespaniDisplayObject {

    protected String title;

    protected boolean published;
    protected long hits;
    protected String xlang = "en";

}

package com.despani.core.beans.base;

import lombok.Data;

// TODO [Zaccone] : where are Lombok constructors?

@Data
public abstract class ADespaniContentObject extends ADespaniDisplayObject {

    protected String title;
    protected String alias;
    protected boolean published;
    protected long hits;
    protected String xlang = "en";

}

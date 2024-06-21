package com.despani.x2.core.xmedia.beans.domains;

import com.despani.x2.core.beans.base.ADespaniObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespMedia extends ADespaniObject {

    protected String filename;
    protected String description;
    protected int type;
    protected Boolean primary;
    protected String url;
    protected String format;


}

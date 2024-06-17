package com.despani.core.beans.domains;

import com.despani.core.beans.base.ADespaniObject;
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

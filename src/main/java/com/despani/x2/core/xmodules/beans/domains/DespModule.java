package com.despani.x2.core.xmodules.beans.domains;

import com.despani.x2.core.beans.base.ADespaniDisplayObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespModule extends ADespaniDisplayObject {

    private String title;
    private String internalTitle;
    private String content;
    private String module;
    private String position;
    private int ordering;
    private boolean showTitle;
    private boolean published;



    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.MODULE;
    }

}

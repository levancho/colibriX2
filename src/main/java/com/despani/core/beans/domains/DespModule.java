package com.despani.core.beans.domains;

import com.despani.core.beans.base.ADespaniDisplayObject;
import com.despani.core.beans.base.ADespaniObject;
import com.despani.core.beans.enums.DespContentTypes;
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

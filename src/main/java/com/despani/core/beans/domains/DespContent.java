package com.despani.core.beans.domains;

import com.despani.core.beans.ContentOut;
import com.despani.core.beans.base.ADespaniContentObject;
import com.despani.core.beans.enums.DespContentTypes;
import com.despani.core.utils.DespPropertyX2;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class DespContent extends ADespaniContentObject {

    protected String introtext;
    protected String fulltext;
    protected boolean featured;
    protected DespCategoryItem category;
    protected Integer categoryTypeOid = -1;
    protected Integer catoid = -1;
    protected DespaniUser owner;
    protected List<DespPropertyX2> properties;
    protected boolean legacy;
    protected ContentOut out;

    public DespPropertyX2 getPropertyByName(final String name){
        if(name==null || name.trim().equalsIgnoreCase("")){
            return null;
        }
        Optional <DespPropertyX2> opti =properties.stream().filter(p -> name.trim().equalsIgnoreCase(p.getName())).findAny();

        return opti.get();
    }

    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.CONTENT;
    }

}

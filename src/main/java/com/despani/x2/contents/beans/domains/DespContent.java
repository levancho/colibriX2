package com.despani.x2.contents.beans.domains;

import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.contents.beans.ContentOut;
import com.despani.x2.core.beans.base.ADespaniContentObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.utils.DespPropertyX2;
import lombok.Data;

import java.util.List;
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

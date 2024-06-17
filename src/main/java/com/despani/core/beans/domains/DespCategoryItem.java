package com.despani.core.beans.domains;

import com.despani.core.beans.DespNodeType;
import com.despani.core.beans.base.ADespNodeItem;
import com.despani.core.beans.enums.DespContentTypes;
import lombok.Data;

import java.util.List;

@Data
public class DespCategoryItem extends ADespNodeItem {


    private DespNodeType type;
    protected String desc;
    private String label="Category";


    public DespCategoryItem() {
        super();
    }

    public DespCategoryItem(int oid) {
        this.oid = oid;
    }

    public static DespCategoryItem getRoot() {
        DespCategoryItem ni = new DespCategoryItem();
        ni.setOid(1);
        ni.setParent_oid(0);
        ni.setTitle("root");
        return ni;
    }

    public DespNodeType getCategoryType() {
        return type;
    }

    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.CATEGORY;
    }

}

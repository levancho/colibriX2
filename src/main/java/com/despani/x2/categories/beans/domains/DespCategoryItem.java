package com.despani.x2.categories.beans.domains;

import com.despani.x2.core.beans.DespNodeType;
import com.despani.x2.core.beans.base.ADespNodeItem;
import com.despani.x2.core.beans.enums.DespContentTypes;
import lombok.Data;

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
        ni.setLabel("root");
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

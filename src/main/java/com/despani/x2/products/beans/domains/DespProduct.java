package com.despani.x2.products.beans.domains;

import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.companies.beans.domains.DespCompany;
import com.despani.x2.core.beans.base.ADespaniContentObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import com.despani.x2.core.utils.DespPropertyX2;
import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespProduct extends ADespaniContentObject {

    protected String sku;
    protected DespProduct parent;
    protected DespCategoryItem category;
    protected DespCompany company;
    protected int price;
    protected Map<String, DespPropertyX2> properties;
    protected List<DespMedia> media;
    protected String description;


    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.PRODUCT;
    }

    public DespProduct(int oid){
        this.oid = oid;
    }


}

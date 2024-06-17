package com.despani.core.beans.domains;

import com.despani.core.beans.base.ADespaniContentObject;
import com.despani.core.beans.enums.DespContentTypes;
import com.despani.core.utils.DespPropertyX2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

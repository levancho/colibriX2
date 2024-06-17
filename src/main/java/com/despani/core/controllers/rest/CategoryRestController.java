package com.despani.core.controllers.rest;


import com.despani.core.beans.DespNodeType;
import com.despani.core.beans.base.ADespNodeItem;
import com.despani.core.beans.domains.DespCategoryItem;
import com.despani.core.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CategoryRestController {



    @Autowired
    CategoryServices categoryServices;

    @GetMapping(value = "rest/categories")
    public List<ADespNodeItem> getALLCategories(){
        DespCategoryItem categoryItem = categoryServices.getNodesTree(1);
        return categoryItem.getChildren();
    }


    @GetMapping(value="rest/categories/id/{parentOid}")
    public List<ADespNodeItem> getALLCategoriesByParentOid(@PathVariable("parentOid") int parentOid){
        DespCategoryItem categoryItem = categoryServices.getNodesTree(parentOid);
        return categoryItem.getChildren();
    }




}

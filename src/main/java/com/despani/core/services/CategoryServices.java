package com.despani.core.services;

import com.despani.core.beans.DespNodeType;
import com.despani.core.beans.base.ADespNodeItem;
import com.despani.core.beans.domains.DespCategoryItem;
import com.despani.core.beans.domains.DespMenuItem;
import com.despani.core.mybatis.mappers.ICategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryServices {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ICategoryMapper categoryMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

//    _DespCategoryMapper mapper2;

    public List<DespCategoryItem> getNodeTree(Integer pedited, String plang) {

        if(pedited<=0){
           return  categoryMapper.getTree(plang);
        } else {
          return   categoryMapper.getTree2(pedited,plang);
        }
    }

    public List<DespCategoryItem>  getNodes(Integer parentoid, String plang) {
        return  categoryMapper.getCatNodes(parentoid);
    }


    public DespCategoryItem  getNodesTree(Integer parentoid) {

        List<DespCategoryItem> catNodes = categoryMapper.getCatNodesAllChildrenByOid(parentoid);
        DespCategoryItem root = new DespCategoryItem();
        root.setOid(parentoid);
        buildMenuItem(catNodes,root);
        return root;
    }


    //   newID,title_in,`cattype_in`, alias_in,  desc_in, created_by_in
    public Integer createCategory( Integer pparent_id,
                            String title_in,
                            Integer cattype_in,
                            String alias_in,
                            String desc_in,
                            Integer created_by_in) {

        Map m = new HashMap();
        m.put("pparent_id",pparent_id);
        m.put("title_in",title_in);
        m.put("cattype_in",cattype_in);
        m.put("alias_in",alias_in);
        m.put("desc_in",desc_in);
        m.put("created_by_in",created_by_in);
        m.put("newID",null);
        categoryMapper.createCategoryFull(m);

        return (Integer)m.get("newID");
    }


    public void deleteCategory( Integer nodeid) {
        Map m = new HashMap();
        m.put("pid",nodeid);
        m.put("parentId",0);
        m.put("newid",null);
        categoryMapper.deleteCategory(m);

    }

//
//
    public void moveCategory( Integer nodeid, Integer newParentNode) {
        Map m = new HashMap();
        m.put("typex","move");
        m.put("pid",nodeid);
        m.put("parentId",newParentNode);
        m.put("newid",null);
        categoryMapper.traverseCategory(m);

    }
//
    public void reorderCategory( Integer nodeid, Integer parentNode) {
        Map m = new HashMap();
        m.put("typex","order");
        m.put("pid",nodeid);
        m.put("parentId",parentNode);
        m.put("newid",null);
        categoryMapper.traverseCategory(m);
//
    }

    public DespCategoryItem getNodeByItemOid(int parentOid) {
        return categoryMapper.getNodeByItemOid(parentOid);
    }

    public List<String> _getAllCategoryTypes() {
        return categoryMapper._getAllCategoryTypes();
    }

    public List<DespNodeType> getAllCategoryTypes() {
        return categoryMapper.getAllCategoryTypes();
    }

    public List<DespCategoryItem> getTypeNodes(Integer parentoid, String plang, String catType) {
        return  categoryMapper.getTypeCategoryNodes(parentoid,catType);
    }

//        new
    public List<DespCategoryItem> getTreeByCategoryType(Integer categoryType) {
        List<DespCategoryItem> nodes = categoryMapper.getTreeByCatype("en", categoryType);
        nodes= nodes.stream().filter(a-> {
            return  a.getTitle()!=null && a.getOid()!=1;
        }).collect(Collectors.toList());
        return nodes;
    }

    public DespCategoryItem getCategoryItemById(int itemmOid){
        return categoryMapper.getCatItemById(itemmOid);
    }

    public int updateCategoryName(int nodeOid,int updatedBy, String newName,String alias){
        return categoryMapper.updateCategoryNodeName(nodeOid,updatedBy,newName,alias);
    }

    public String getNodeName(int parentOid) {
        return categoryMapper.getParentName(parentOid);
    }

    public ADespNodeItem getNodeByItemOidLight(int oid) {

        return categoryMapper.getNodeByItemOidLight(oid);
    }

    public void buildMenuItem(List<DespCategoryItem> sourceItems, DespCategoryItem parentItem){
        for(int i=0;i<sourceItems.size();i++){
            DespCategoryItem n = sourceItems.get(i);
            int menuParentOid = n.getParent_oid();
            if(menuParentOid == parentItem.getOid()){
                DespCategoryItem menuItem =n;
                parentItem.addChild(menuItem);
                this.buildMenuItem(sourceItems,menuItem);
            }
        }

    }
//


}

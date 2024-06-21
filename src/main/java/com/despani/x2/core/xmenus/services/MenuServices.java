package com.despani.x2.core.xmenus.services;

import com.despani.x2.core.beans.DespNodeType;
import com.despani.x2.core.beans.base.ADespNodeItem;
import com.despani.x2.core.xmenus.beans.domains.DespMenuItem;
import com.despani.x2.core.xmenus.mybatis.mappers.IMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MenuServices {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    IMenuMapper menuMapper;



    public List<DespMenuItem>  getNodes(Integer parentoid, String plang) {

        return  menuMapper.getMenuNodes(parentoid);
    }

    //menu type
    public List<DespMenuItem>  getTypeNodes(Integer parentoid, String plang, Integer menuTypeX) {

        return  menuMapper.getTypeMenuNodes(parentoid,menuTypeX);
    }

    public List<DespMenuItem> getNodeTreeWithDots(Integer pedited, String plang) {

        if(pedited<=0){
            return  menuMapper.getTree(plang);
        } else {
            return   menuMapper.getTree2(pedited,plang);
        }
    }


    public List<DespMenuItem> getTreeByMenuType(Integer menuType) {
            return  menuMapper.getTreeByMenuType("en",menuType);

    }


    public Integer createMenu( Integer pparent_id,
                            String title_in,
                            Integer menutype_in,
                            String alias_in,
                            String path_in,
                            String link_in,
                            String type_in,
                            Integer xlevel_in,
                            Integer component_id_in,
                            Integer access_in,
                            String img_in,
                            String params_in,
                            String xlang_in) {

        Map m = new HashMap();
        m.put("pparent_id",pparent_id);
        m.put("title_in",title_in);
        m.put("menutype_in",menutype_in);
        m.put("alias_in",alias_in);
        m.put("path_in",path_in);
        m.put("link_in",link_in);
        m.put("type_in",type_in);
        m.put("xlevel_in",xlevel_in);
        m.put("component_id_in",component_id_in);
        m.put("access_in",access_in);
        m.put("img_in",img_in);
        m.put("params_in",params_in);
        m.put("xlang_in",xlang_in);
        m.put("newID",null);
        menuMapper.createMenuFull(m);

        return (Integer)m.get("newID");
    }


    public Integer createMenu(Integer pparent_id,
                              String title_in,
                              Integer menu_type,
                              String link_in,
                              String imageUrl
    ) {


       return  createMenu(pparent_id,title_in,
               menu_type,
                title_in,link_in,link_in,
                "Menu",0,0,0,imageUrl,"{}","en");
    }

    public void deleteMenuNode( Integer nodeid) {
        Map m = new HashMap();
        m.put("pid",nodeid);
        m.put("parentId",0);
        m.put("newid",null);
        menuMapper.deleteMenuItem(m);

    }

    public void moveMenuNode( Integer nodeid, Integer newParentNode) {
        Map m = new HashMap();
        m.put("typex","move");
        m.put("pid",nodeid);
        m.put("parentId",newParentNode);
        m.put("newid",null);
        menuMapper.traverseMenu(m);

    }

    public void reorderMenuNode( Integer nodeid, Integer parentNode) {
        Map m = new HashMap();
        m.put("typex","order");
        m.put("pid",nodeid);
        m.put("parentId",parentNode);
        m.put("newid",null);
        menuMapper.traverseMenu(m);
//
    }

    public DespMenuItem getParentNode(int parentOid) {
        return menuMapper.getParentNode(parentOid);
    }

    public List<DespNodeType> getAllMenuTypes() {
       return menuMapper.getAllMenuTypes();
    }

    public DespMenuItem getMenuItemById(int menuItemOid){
        return menuMapper.getMenuItemById(menuItemOid);
    }

    public int updateNodeMenuName(int nodeOid,int updaeterOid,String newName,String url,String img){
        return menuMapper.updateMenuNodeName(nodeOid,updaeterOid,newName,url,img);
    }

    public String getNodeName(Integer parent_oid) {
        return menuMapper.getNodeName(parent_oid);
    }


    public DespMenuItem getMainMenuContainer(){

        List<DespMenuItem> nodes =   getNodes(0,"en");

        DespMenuItem parentMenuItem =  DespMenuItem.getRoot();
        buildMenuItem(nodes,parentMenuItem);
        return parentMenuItem;

    }



    //menu type
    public DespMenuItem getTypeMenuContainer(Integer menuTypeX){

        List<DespMenuItem> nodes =    getTypeNodes(0,"en",menuTypeX);

        DespMenuItem parentMenuItem =  DespMenuItem.getRoot();
        buildMenuItem(nodes,parentMenuItem);
        return parentMenuItem;

    }



    public DespMenuItem getMainContainer(String menuContainerId){

        return null;

    }


    public void buildMenuItem(List<DespMenuItem> sourceItems, DespMenuItem parentItem){

        for(int i=0;i<sourceItems.size();i++){
            DespMenuItem n = sourceItems.get(i);
            int menuParentOid = n.getParent_oid();
            if(menuParentOid == parentItem.getOid()){
                DespMenuItem menuItem =n;
                parentItem.addChild(menuItem);
                this.buildMenuItem(sourceItems,menuItem);
            }
        }

    }

    public ADespNodeItem getNodeByItemOidLight(Integer parentOid) {
        return menuMapper.getNodeByItemOidLight(parentOid);
    }
}

package com.despani.x2.core.xmenus.controllers.mvc.admin;

import com.despani.x2.core.annotations.Crumb;
import com.despani.x2.core.beans.DespNodeType;
import com.despani.x2.core.beans.base.ADespNodeItem;
import com.despani.x2.core.xmenus.beans.domains.DespMenuItem;
import com.despani.x2.core.xmenus.beans.form.MenuFormItem;
import com.despani.x2.core.xmenus.services.MenuServices;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.controllers.mvc.ABaseController;
import com.despani.x2.core.mapstruct.IDespaniMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/admin/menu")
public class AdminMenuController extends ABaseController {


    public static String MENU="menu";

    public static Integer MAIN_MENU = 1;
    public static Integer ADMIN_MENU = 2;
    public static Integer LEFT_MENU = 3;
    public static Integer RIGHT_MENU = 4;


    @Autowired
    MenuServices menuService;


    @Crumb(label="Admin-Menu", xdesc = "Admin Menu", family="admin", parent = "Admin-main" )
    @GetMapping({"/", "/list"})
    public String menuIndex(Model model){

        DespMenuItem dummy = new DespMenuItem();
        List<DespNodeType> despMenuTypes = menuService.getAllMenuTypes();
        model.addAttribute("menuTypes", despMenuTypes);
        model.addAttribute("context", dummy);

        return admin(model,dummy.getListPage());
    }

    @RequestMapping(value = "/manager/add/save" , method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String menuManagerSave(Model model, @Valid MenuFormItem menuformItem, BindingResult bindingResult ){


        if (bindingResult.hasErrors()) {
            return adminRedirect("menu/type/"+menuformItem.getMenuTypeOid());
        }

        String imageurkl =  menuformItem.getImg();
        if(imageurkl==null || imageurkl.trim().equalsIgnoreCase("")){
            imageurkl=menuformItem.getName()+".png";
        }

        DespMenuItem parentNodeItem = menuService.getParentNode(menuformItem.getParentOid());

        if(parentNodeItem.getOid()==1){
            //if root
            menuService.createMenu(menuformItem.getParentOid(), menuformItem.getName(),menuformItem.getMenuTypeOid(), menuformItem.getUrl(),imageurkl);
        }else{
            menuService.createMenu(menuformItem.getParentOid(), menuformItem.getName(),parentNodeItem.getMenuType().getOid(),  menuformItem.getUrl(), imageurkl);
        }

        if(log.isDebugEnabled()){
            log.debug("Home Page wass Accessed!!!!");
        }
        model.addAttribute("forWhat","menu");
        return adminRedirect("/menu/type/"+menuformItem.getMenuTypeOid());
    }
    // Modal edit
    @RequestMapping(value="/manager/edit/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String menuManagerRename(@AuthenticationPrincipal DespaniPrincipal user,Model model, @Valid MenuFormItem menuformItem, BindingResult bindingResult ){
        if (bindingResult.hasErrors()) {
            return adminRedirect("menu/type/"+menuformItem.getMenuTypeOid());
        }

        //update

//        String title = menuformItem.getName();
//        int toEditOid = menuformItem.getOid();
//        String url = menuformItem.getUrl();
//        String img = menuformItem.getImg();
//
//        int res = menuService.updateNodeMenuName(toEditOid,title,url,img);
//        return adminRedirect("/menu/type/"+menuformItem.getMenuTypeOid());

        return _saveEditMenuItem(user,menuformItem);
    }

    // save in page
    @RequestMapping(value="/save",method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveEdit(@AuthenticationPrincipal DespaniPrincipal user,@Valid MenuFormItem menuformItem,BindingResult bindingResult ) throws IOException {
        if (bindingResult.hasErrors()) {
            return adminRedirect("menu/type/"+menuformItem.getMenuTypeOid());
        }



        //update

//        String title = menuformItem.getName();
//        int toEditOid = menuformItem.getOid();
//        String url = menuformItem.getUrl();
//        String img = menuformItem.getImg();
//
//        int res = menuService.updateNodeMenuName(toEditOid,title,url,img);
//        return adminRedirect("/menu/type/"+menuformItem.getMenuTypeOid());

        return _saveEditMenuItem(user,menuformItem);
    }

    private String _saveEditMenuItem(DespaniPrincipal user,MenuFormItem menuformItem){

        DespMenuItem dmi = IDespaniMapper.MAPPER.toDespaniMenu(menuformItem);

        dmi.setUpdatedOn(new Date());
        dmi.setUpdatedByOid(user.getUser().getOid());

        String title = menuformItem.getName();
        int toEditOid = menuformItem.getOid();
        int updaterOid = user.getUser().getOid();
        String url = menuformItem.getUrl();
        String img = menuformItem.getImg();

        int res = menuService.updateNodeMenuName(toEditOid,updaterOid,title,url,img);


        if (menuformItem.getAction()!=null && menuformItem.getAction().equalsIgnoreCase("apply")){
            return adminRedirect(dmi.getObjectEditLink());
        }
        return adminRedirect("/menu/type/"+menuformItem.getMenuTypeOid());
    }

    @RequestMapping(value="/manager/move",method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String moveNode(Model model, @Valid MenuFormItem menuFormItem, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return adminRedirect("menu/type/"+menuFormItem.getMenuTypeOid());
        }

        ADespNodeItem aNode = menuService.getNodeByItemOidLight(menuFormItem.getOid());
        ADespNodeItem targetParentNode = menuService.getNodeByItemOidLight(menuFormItem.getParentOid());

        if(aNode.getLft()<targetParentNode.getLft() && aNode.getRgt()>targetParentNode.getRgt()){
            throw new RuntimeException("can not move Item as child of its child nr grand child.");
        }

        menuService.moveMenuNode(menuFormItem.getOid(), menuFormItem.getParentOid());
        return adminRedirect("/menu/type/"+menuFormItem.getMenuTypeOid());
    }


    @Crumb(label="Admin-Menu-item", xdesc = "Menu Item", family="admin", parent = "Admin-Menu" )
    @GetMapping({"/type/{oid}" })
    public String editMenu(Model model, @PathVariable("oid") Integer meniuOid){
        List<DespMenuItem> nodes =   menuService.getTreeByMenuType(meniuOid);
        nodes= nodes.stream().filter(a-> {
           return  a.getTitle()!=null && a.getOid()!=1;
        }).collect(Collectors.toList());
        model.addAttribute("nodes",nodes);
        model.addAttribute("menuTypeOid",meniuOid);
        model.addAttribute("menuFormItem",new MenuFormItem());
        model.addAttribute("forWhat","menu");
        return admin(model,"/menu/manager");
    }

    @Crumb(label="Admin-Menu-item",xdesc = "Edit Menu Item", family="admin", parent = "Admin-Menu" )
    @RequestMapping({"/oid/{oid}/edit"})
    public String editContent(@PathVariable("oid")Integer oid, @AuthenticationPrincipal DespaniPrincipal user, Model model, @Valid @ModelAttribute("menuForm") MenuFormItem menuForm , BindingResult bindingResult) throws Exception {
        DespMenuItem content = menuService.getMenuItemById(oid);
        addDefaultDynamicActionBar(model,content);
        model.addAttribute("context",content);
        model.addAttribute("menuTypeOid",content.getMenuType().getOid());
        model.addAttribute("menuFormItem",new MenuFormItem());
        return admin(model, content.getObjectEditPage());
    }


    @RequestMapping(value="/getMenuItem/{oid}",method = RequestMethod.GET )
    @ResponseBody
    public DespMenuItem getMenuItem(@PathVariable("oid") Integer oid){


        DespMenuItem nodeItem = menuService.getMenuItemById(oid);

        String parentName = menuService.getNodeName(nodeItem.getParent_oid());
        DespMenuItem parentNodeItem = new DespMenuItem();
        parentNodeItem.setTitle(parentName);
        nodeItem.setParent(parentNodeItem);

        return nodeItem;
    }

    @GetMapping({"/type/{menuoid}/delete/{itemid}" })
    public String deleteItem(Model model, @PathVariable("menuoid") Integer meniOid,@PathVariable("itemid") Integer itemid){

        // TODO validation,
        menuService.deleteMenuNode(itemid);
        model.addAttribute("forWhat","menu");
        return adminRedirect("/menu/type/"+meniOid);
    }


}

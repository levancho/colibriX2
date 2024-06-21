package com.despani.x2.categories.controllers.mvc.admin;


import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.categories.beans.form.CategoryFormItem;
import com.despani.x2.categories.services.CategoryServices;
import com.despani.x2.contents.mapstruct.IContentMapper;
import com.despani.x2.core.annotations.Crumb;
import com.despani.x2.core.beans.DespNodeType;
import com.despani.x2.core.beans.base.ADespNodeItem;
import com.despani.x2.core.controllers.mvc.ABaseController;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController extends ABaseController {

// TODO add publish toggle in list that will make ajax call to publish and unpublish category.


    @Autowired
    CategoryServices categoryService;



    @Crumb(label="Admin-Category", xdesc = "Category Admin", family="admin", parent = "Admin-main" )
    @GetMapping({"/", "/list"})
    public String categoryIndex(Model model){

        DespCategoryItem dummy = new DespCategoryItem();
        List<DespNodeType> categoryTypes = categoryService.getAllCategoryTypes();
        model.addAttribute("categoryTypes",categoryTypes);
        return admin(model,dummy.getListPage());
    }

    @Crumb(label="Admin-Category-type", xdesc = "Category Type", family="admin", parent = "Admin-Category" )
    @GetMapping({"/type/{categoryTypeOid}" })
    public String editCategory(Model model, @PathVariable("categoryTypeOid") Integer categoryOid){
        List<DespCategoryItem> catNodes = categoryService.getTreeByCategoryType(categoryOid);
//        nodes= nodes.stream().filter(a-> {
//            return  a.getTitle()!=null && a.getOid()!=1;
//        }).collect(Collectors.toList());

        catNodes = catNodes.stream().filter(a-> {
                    return  a.getLabel()!=null && a.getOid()!=1;
                }).collect(Collectors.toList());

        model.addAttribute("nodes",catNodes);
        model.addAttribute("categoryTypeOid",categoryOid);
        model.addAttribute("categoryFormItem",new CategoryFormItem());
        //TODO remove next line
        model.addAttribute("forWhat","category");
        return admin(model,"/category/manager");
    }

    @Crumb(label="Admin-Category-Type-property", xdesc = "Category Type Properties", family="admin", parent = "Admin-Category" )
    @GetMapping({"/props/type/{categoryTypeOid}" })
    public String editCategoryTypeProperty(Model model, @PathVariable("categoryTypeOid") Integer categoryOid){
        List<DespCategoryItem> catNodes = categoryService.getTreeByCategoryType(categoryOid);
//        nodes= nodes.stream().filter(a-> {
//            return  a.getTitle()!=null && a.getOid()!=1;
//        }).collect(Collectors.toList());

        catNodes = catNodes.stream().filter(a-> {
            return  a.getLabel()!=null && a.getOid()!=1;
        }).collect(Collectors.toList());

        model.addAttribute("nodes",catNodes);
        model.addAttribute("categoryTypeOid",categoryOid);
        model.addAttribute("categoryFormItem",new CategoryFormItem());
        //TODO remove next line
        model.addAttribute("forWhat","category");
        return admin(model,"/category/manager");
    }

    @RequestMapping(value="/manager/save", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String categoryManagerSave(Model model, @Valid CategoryFormItem categoryFormItem, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return adminRedirect("category/type/"+categoryFormItem.getCategoryTypeOid());
        }
        String test = "test";

         DespCategoryItem parentNodeItem = categoryService.getNodeByItemOid(categoryFormItem.getParentOid());

        if(parentNodeItem.getOid()==1){
            //if roo
            categoryService.createCategory(
                    categoryFormItem.getParentOid(),
                    categoryFormItem.getName(),
                    categoryFormItem.getCategoryTypeOid(),
                    categoryFormItem.getAlias(),
                    "TODO SHALVA FIX THIS  HACK",
                    1);
        } else {
            categoryService.createCategory(
                    categoryFormItem.getParentOid(),
                    categoryFormItem.getName(),
                    //TODO fix category type in NodeItem class
                    parentNodeItem.getType().getOid(),
                    categoryFormItem.getAlias(),
                     "TODO SHALVA FIX THIS  HACK ->.png",
                    1);
        }


//        return "redirect:/category/";
        model.addAttribute("forWhat","category");
        return adminRedirect("/category/type/"+categoryFormItem.getCategoryTypeOid());
    }


    @RequestMapping(value="/categoryItem/{Oid}",method = RequestMethod.GET)
    @ResponseBody
    public DespCategoryItem getMenuItem(Model model,@PathVariable("Oid") Integer itemOid){

        DespCategoryItem nodeItem = categoryService.getCategoryItemById(itemOid);
        int parentOid = nodeItem.getParent_oid();

        String parentName = categoryService.getNodeName(parentOid);

        DespCategoryItem parentItem = new DespCategoryItem();
        parentItem.setLabel(parentName);
        nodeItem.setParent(parentItem);
        return nodeItem;
    }


    @RequestMapping(value="/manager/edit/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String categoryManagerRename(@AuthenticationPrincipal DespaniPrincipal user,Model model, @Valid CategoryFormItem categoryFormItem, BindingResult bindingResult ){
        if (bindingResult.hasErrors()) {
            return adminRedirect("category/type/"+categoryFormItem.getCategoryTypeOid());
        }

        //update
        String title = categoryFormItem.getName();
        int toEditOid = categoryFormItem.getOid();
        String alias= categoryFormItem.getAlias();
        int updatedByOid = user.getUser().getOid();

        int res = categoryService.updateCategoryName(toEditOid,updatedByOid,title,alias);

        return adminRedirect("/category/type/"+categoryFormItem.getCategoryTypeOid());
    }


    @RequestMapping(value="/manager/move",method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String moveNode(Model model, @Valid CategoryFormItem categoryFormItem, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return adminRedirect("category/type/"+categoryFormItem.getCategoryTypeOid());
        }
        ADespNodeItem aNode = categoryService.getNodeByItemOidLight(categoryFormItem.getOid());
        ADespNodeItem targetParentNode = categoryService.getNodeByItemOidLight(categoryFormItem.getParentOid());

        if(aNode.getLft()<targetParentNode.getLft() && aNode.getRgt()>targetParentNode.getRgt()){
            throw new RuntimeException("can not move Item as child of its child nr grand child.");
        }

        categoryService.moveCategory(categoryFormItem.getOid(), categoryFormItem.getParentOid());
        model.addAttribute("forWhat","category");
        return adminRedirect("/category/type/"+categoryFormItem.getCategoryTypeOid());
    }


        private String _prepContent(Model model, CategoryFormItem categoryForm, DespCategoryItem content) throws Exception {

            // add save, apply , close actions
            addDefaultDynamicActionBar(model,content);
            model.addAttribute("context",content);
            return admin(model, content.getObjectEditPage());
        }


    @Crumb(label="Admin-Edit-Category",xdesc = "Edit Category Item", family="admin", parent = "Admin-Category" )
    @RequestMapping({"/oid/{oid}/edit"})
    public String editContent(@PathVariable("oid")Integer oid, @AuthenticationPrincipal DespaniPrincipal user, Model model, @Valid @ModelAttribute("categoryForm") CategoryFormItem categoryForm , BindingResult bindingResult) throws Exception {
        DespCategoryItem content = categoryService.getCategoryItemById(oid);
        model.addAttribute("categoryTypeOid",content.getCategoryType().getOid());
        model.addAttribute("categoryFormItem",new CategoryFormItem());
        return _prepContent(model,categoryForm,content);
    }

    @RequestMapping({"/save"})
    public String saveContent(@AuthenticationPrincipal DespaniPrincipal user, Model model, @Valid @ModelAttribute("categoryForm") CategoryFormItem categoryForm , BindingResult bindingResult) throws Exception {

        // TODO shalva implement  conditional validation either intro or full text has to be entered.????
        DespCategoryItem dc = IContentMapper.MAPPER.toDespaniCategory( categoryForm );

        if(bindingResult.hasErrors()) {
            model.addAttribute("xerrors",bindingResult.getAllErrors());
            if(dc.getOid()==-1){
                return _prepContent(model,categoryForm,dc);
            } else {
                return  _prepContent(model,categoryForm, dc);
            }
        }
        dc.setUpdatedOn(new Date());
        dc.setUpdatedByOid(user.getUser().getOid());
//        if(dc.getOid()==-1){
//            dc.setCreatedByOid(user.getUser().getOid());
//            contentManager.createContent(user,dc);
//        } else {
//            contentManager.saveContent(dc);
//        }

        categoryService.updateCategoryName(dc.getOid(),dc.getUpdatedByOid(),dc.getLabel(),dc.getAlias());


        if(categoryForm.getAction()!=null && categoryForm.getAction().equalsIgnoreCase("save")){
//            return adminRedirect(dc.getListLink());
            return adminRedirect("/category/type/"+categoryForm.getCategoryTypeOid());
        } else {
            return adminRedirect(dc.getObjectEditLink());
        }
    }

    @GetMapping({"/type/{categoryOid}/delete/{itemid}" })
    public String deleteItem(Model model, @PathVariable("categoryOid") Integer categoryOid,@PathVariable("itemid") Integer itemid){

        // TODO validation,
        categoryService.deleteCategory(itemid);
        model.addAttribute("forWhat","menu");
        return adminRedirect("/category/type/"+categoryOid);
    }


}

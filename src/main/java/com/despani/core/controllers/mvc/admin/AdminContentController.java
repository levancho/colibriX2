package com.despani.core.controllers.mvc.admin;

import com.despani.core.annotations.Crumb;
import com.despani.core.beans.DespNodeType;
import com.despani.core.interfaces.IActionBarItem;
import com.despani.core.beans.Pagination;
import com.despani.core.beans.domains.DespCategoryItem;
import com.despani.core.beans.domains.DespContent;
import com.despani.core.beans.DespGlobalLocalProp;
import com.despani.core.beans.form.ContentForm;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.managers.ContentManager;
import com.despani.core.mapstruct.mappers.IDespaniMapper;
import com.despani.core.platform.interfaces.IContentServices;
import com.despani.core.services.CategoryServices;
import com.despani.core.utils.DespGlobals;
import com.despani.core.utils.DespPropertyX2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.Entry.comparingByKey;

@Controller
@RequestMapping("/admin/content")
public class AdminContentController extends ABaseController {

    private DespContent dummy = new DespContent();

    @Autowired
    public ContentManager contentManager;

    @Autowired
    public IContentServices contentServices;

    @Autowired
    public CategoryServices categoryServices;

    @Crumb(label = "Admin-Content", xdesc = "Content", family = "admin", parent = "Admin-main")
    @RequestMapping({"/", "", "/list"})
    public String adminHome(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limitp) {


        Pagination p = new Pagination(dummy, contentManager.countContent());
        p.calculate(pageNumber, limitp);

        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getActionBarItem(dummy, "new", "new", "New", "primary", "mdi mdi-plus-network-outline"));
        items.add(adminMan.getActionBarItem(dummy, "close", "close", "Close", "danger", "mdi mdi-window-close"));
        model.addAttribute("actionItems", items);

        model.addAttribute("p", p);
        model.addAttribute("context", dummy);
        List<DespContent> contetnToShow = contentManager.getAllContentLimited(p.getLimite(), p.getOffsetMultiplier(), false);
        model.addAttribute("allcontent", contetnToShow);

        return admin(model, dummy.getListPage());
    }

    @Crumb(label = "Admin-Edit-Content", xdesc = "Edit Content", family = "admin", parent = "Admin-Content")
    @RequestMapping({"/new"})
    public String addContent(@AuthenticationPrincipal DespaniPrincipal user, Model model, @Valid @ModelAttribute("contentForm") ContentForm contentForm, BindingResult bindingResult) throws Exception {

        DespContent dc = IDespaniMapper.MAPPER.toDespaniContent(contentForm);
        String r = _prepContent(model, contentForm, dc);
        return r;
    }


    /**
     * EDIT CONTENT STARTS HERE
     */
    @Crumb(label = "Admin-Edit-Content", xdesc = "Edit Content", family = "admin", parent = "Admin-Content")
    @RequestMapping({"/oid/{oid}/edit"})
    public String editContent(@PathVariable("oid") Integer oid,
                              @AuthenticationPrincipal DespaniPrincipal user, Model model,
                              @Valid @ModelAttribute("contentForm") ContentForm contentForm,
                              BindingResult bindingResult) throws Exception {

        DespContent content = contentManager.getContentByOid(oid, false);
        return _prepContent(model, contentForm, content);
    }


    private String _prepContent(Model model,
                                ContentForm contentForm,
                                DespContent content) throws Exception {

        List<DespPropertyX2> despGlobalContentProps = new ArrayList<>();
        // TODO TOFIX
//        IntStream.range(1, 4).forEach(i -> despGlobalContentProps.addAll(DespGlobals.getListPropertyAsProperty("content." + i)));

        List<DespGlobalLocalProp> despGlobalLocalProps = contentServices.getGlobalLocalProps(despGlobalContentProps, contentForm.getOid());

        List<DespNodeType> allCategoryTypes = categoryServices.getAllCategoryTypes();

        model.addAttribute("catTypes", allCategoryTypes);
        model.addAttribute("globalPropsX", despGlobalLocalProps);
        addDefaultDynamicActionBar(model, content);
        model.addAttribute("site.wysiwyg-editor", DespGlobals.getProperty("site.wysiwyg-editor"));
        model.addAttribute("context", content);
        return admin(model, content.getObjectEditPage());
    }


    @RequestMapping({"/save"})
    public String editContent(@AuthenticationPrincipal DespaniPrincipal user, Model model,
                              @Valid @ModelAttribute("contentForm") ContentForm contentForm,
                              BindingResult bindingResult) throws Exception {

        DespContent dc = IDespaniMapper.MAPPER.toDespaniContent(contentForm);

        if (bindingResult.hasErrors()) {
            model.addAttribute("xerrors", bindingResult.getAllErrors());
            if (dc.getOid() == -1) {
                return _prepContent(model, contentForm, dc);
            } else {
                return _prepContent(model, contentForm, dc);
            }
        }
        dc.setUpdatedOn(new Date());
        dc.setUpdatedByOid(user.getUser().getOid());
        if (dc.getOid() == -1) {
            dc.setCreatedByOid(user.getUser().getOid());
            contentManager.createContent(user, dc, contentForm.getProps());
        } else {
            dc.setCreatedByOid(contentManager.getContentByOid(dc.getOid(), false).getCreatedByOid());
            contentManager.updateContent(dc, contentForm.getProps());
        }

        if (contentForm.getAction() != null && contentForm.getAction().equalsIgnoreCase("save")) {
            return adminRedirect(dc.getListLink());
        } else {
            return adminRedirect(dc.getObjectEditLink());
        }
    }

    @RequestMapping("/{userOid}/publish/{publish}")
    public @ResponseBody
    String doPublish(@PathVariable("userOid") Integer userOid, @PathVariable("publish") Boolean publish) {
        contentManager.publishContent(userOid, publish);
        return "ok";
    }

    @RequestMapping("/load/categories/type/{typeoid}")
    public @ResponseBody
    List<DespCategoryItem> loadCategoriesForType(@PathVariable("typeoid") Integer typeoid) {
        if (typeoid == null || typeoid == -1) {
            return Collections.emptyList();
        }
        return categoryServices.getTreeByCategoryType(typeoid);
    }

    @RequestMapping("/{userOid}/feature/{featured}")
    public @ResponseBody
    String doFeature(@PathVariable("userOid") Integer userOid, @PathVariable("featured") Boolean featured) {
        contentManager.featuredContent(userOid, featured);
        return "ok";
    }

    @RequestMapping("/{userOid}/access/{access}")
    public @ResponseBody
    String doFeature(@PathVariable("userOid") Integer userOid, @PathVariable("access") Integer access) {
        contentManager.updateContentAccessLevel(userOid, access);
        return "ok";
    }

    @RequestMapping("/test/acl/{oid}")
    public @ResponseBody
    String dotest(@PathVariable("oid") Integer oid) throws Exception {
        contentManager.getContentByOid(oid);
        return "ok";
    }

}

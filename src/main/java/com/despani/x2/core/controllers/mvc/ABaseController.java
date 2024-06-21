package com.despani.x2.core.controllers.mvc;

import com.despani.x2.core.xmenus.beans.domains.DespMenuItem;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.interfaces.IActionBarItem;
import com.despani.x2.core.beans.base.ADespaniDisplayObject;
import com.despani.x2.core.config.beans.DespaniConfigProperties;
import com.despani.x2.core.managers.AdminManager;
import com.despani.x2.core.managers.ApplicationManager;
import com.despani.x2.core.interfaces.ILinkable;
import com.despani.x2.core.managers.base.IDespSecurityManager;
import com.despani.x2.core.xmenus.services.MenuServices;
import com.despani.x2.core.xusers.interfaces.IUserService;
import com.despani.x2.core.utils.DespGlobals;
import com.despani.x2.core.utils.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ABaseController  {


    @Autowired
    protected DespaniConfigProperties props;

    @Autowired
    protected PagingUtil pagingUtil;

//    @Resource(name = "applicationScopedBean")
//    SessionRegistry sessionRegistry;


    @Autowired
    protected IUserService userService;

    @Autowired
    protected DespaniConfigProperties despaniConfigProperties;

    @Autowired
    protected MenuServices menuService;


    @Autowired
    protected ApplicationManager appMan;


    @Autowired
    protected AdminManager adminMan;


    @Autowired
    IDespSecurityManager despSecurityManager;

    public static Integer MAIN_MENU = 1;




    /* ------------------ SITE  --------------------*/


    protected String site (HttpServletRequest req, Model model, String pageName) {

        DespaniPrincipal currentUser = despSecurityManager.getCurrentUser();
        String xlayout = DespGlobals.getPropertyValue("xlayout");
        DespMenuItem mi = menuService.getTypeMenuContainer(MAIN_MENU);
        mi.setLabel("Main Menu");

        model.addAttribute("currentUser",currentUser);
        model.addAttribute("page",pageName);
        model.addAttribute("main_menu",mi);
        model.addAttribute("xlayout",xlayout);
        model.addAttribute("manager",appMan);

//        model.addAttribute("context",sc);
        return "index-f";
    }

    //
    protected String site (HttpServletRequest req, Model model) {
        return site(req,model, "main");
    }


    protected String siteRedirect(String s) {
        return "redirect:/app/"+s;
    }

    protected String siteRedirect(ILinkable s) {
        return "redirect:"+appMan.getLink(s);
    }


    /* ------------------ ADMIN  --------------------*/

    protected String adminRedirect(String s) {
        return "redirect:/admin"+s;
    }


    protected String adminRedirect(ILinkable s) {
        return "redirect:"+adminMan.getLink(s);
    }


    protected String admin (Model model, String pageName) {


        DespaniPrincipal currentUser = despSecurityManager.getCurrentUser();
        String theme = DespGlobals.getPropertyValue("admin-theme");
        String xlayout = DespGlobals.getPropertyValue("admin-menu-xlayout");
        if(StringUtils.isEmpty(xlayout)) xlayout = "top";
        DespMenuItem mi = menuService.getTypeMenuContainer(2);
        mi.setLabel("Admin Menu");

        model.addAttribute("sitelangs",DespGlobals.sitelangs);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("page",pageName);
        model.addAttribute("admin_menu",mi);
        model.addAttribute("xlayout",xlayout);
        model.addAttribute("xtheme",theme);
        model.addAttribute("manager",adminMan);
        return "/admin";
    }

    protected void addDefaultDynamicActionBar(Model model, ADespaniDisplayObject content) {
        List<IActionBarItem> items = new ArrayList<>();
            items.add( adminMan.getActionBarItem(content,"save","save","Save","primary", ""));
            items.add( adminMan.getActionBarItem(content,"save","apply","Apply","success","mdi mdi-check-bold"));
            items.add( adminMan.getActionBarItem(content,"list","close","Close","danger","mdi mdi-window-close"));
        model.addAttribute("actionItems",items);
    }

    protected String admin (Model model) {
        return admin(model, "/main");
    }


    /* ------------------ ERROR  --------------------*/

    protected String generic(Model model, String pageName) {

        String theme = DespGlobals.getPropertyValue("admin-theme");

        model.addAttribute("page",pageName);
        model.addAttribute("xtheme",theme);
        return "generic-f";
    }




    protected String generic(Model model) {
        return generic(model, "error");
    }
}


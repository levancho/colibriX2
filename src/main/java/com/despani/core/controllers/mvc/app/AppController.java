package com.despani.core.controllers.mvc.app;


import com.despani.core.beans.Pagination;
import com.despani.core.beans.domains.DespContent;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.managers.ApplicationManager;
import com.despani.core.managers.ContentManager;
import com.despani.core.managers.base.IDespSecurityManager;
import com.despani.core.services.MenuServices;
import com.despani.core.services.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/app")
public class AppController extends ABaseController {


    @Autowired
    MenuServices menuService;

    @Autowired
    public ContentManager contentManager;

    @Autowired
    public ApplicationManager appManager;

    @Autowired
    public ModuleService moduleService;

    @Autowired
    public IDespSecurityManager despSecurityManager;

    @RequestMapping({"/",""})
    public String Home(HttpServletRequest req, Model model, @RequestParam(value = "page",required=false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "limit",required=false, defaultValue = "10") Integer limitp){

        DespaniPrincipal currentUser = despSecurityManager.getCurrentUser();
        DespContent dummy = new DespContent();
        Pagination p = new Pagination(dummy,contentManager.countContent());
        p.calculate(pageNumber,limitp);
        List<DespContent> contetnToShow =  contentManager.getPublishedContentList(p.getLimite(),p.getOffsetMultiplier());
        List<DespContent> featured =  contentManager.getFeaturedContent(p.getLimite(),p.getOffsetMultiplier());

//        DespModule despModule = moduleService.getModuleByOid(163);\



        model.addAttribute("currentUser",currentUser);
        model.addAttribute("p",p);
        model.addAttribute("allcontent",contetnToShow);
        model.addAttribute("featuredcontent",featured);


        return site(req,model);
    }


}

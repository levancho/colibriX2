package com.despani.x2.core.controllers.mvc.admin;


import com.despani.x2.core.annotations.Crumb;
import com.despani.x2.core.beans.DespReferenceData;
import com.despani.x2.core.beans.enums.DespPropertyType;
import com.despani.x2.core.beans.form.PropertyDataForm;
import com.despani.x2.core.controllers.mvc.ABaseController;
import com.despani.x2.core.interfaces.IActionBarItem;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.managers.AdminManager;
import com.despani.x2.core.mapstruct.IDespaniMapper;
import com.despani.x2.core.services.AdminServices;
import com.despani.x2.core.services.PropertiesServices;
import com.despani.x2.core.services.RefDataServices;
import com.despani.x2.core.utils.DespGlobals;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends ABaseController {

    @Autowired
    public AdminServices adminService;

    @Autowired
    public RefDataServices refDataServices;

    @Autowired
    MessageSource messageSource;

    @Autowired
    public PropertiesServices propertiesServices;

    @Autowired
    public AdminManager adminManager;

    @Crumb(label = "Admin-main", xdesc = "Admin Panel", family = "admin", parent = "")
    @RequestMapping({"/main", "/", ""})
    public String adminHome(Model model) {
        return admin(model);
    }


    @Crumb(label = "Admin-settings", xdesc = "Admin Settings", family = "admin", parent = "Admin-main")
    @RequestMapping("/settings")
    public String adminSettings(Model model, Locale locale) {
        DespReferenceData refData = adminMan.getRefData("global.selected-tab");


        List<String> places = refData.getValues().stream().map(d -> d.getValue()).collect(Collectors.toList());
        places.stream().forEach(
                s -> {
                    model.addAttribute(  "Texts", DespGlobals.getPropertiesByType(DespPropertyType.TEXT));
                    model.addAttribute(  "Radios", DespGlobals.getPropertiesByType(DespPropertyType.BOOL));
                    model.addAttribute( "Selects", DespGlobals.getPropertiesByType(DespPropertyType.SELECT));
                }
        );
        // add save, apply , close actions
        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/settings/new", "add", "Add", "warning", ""));
        items.add(adminMan.getStaticActionBarItem("/settings/save", "save", "Save", "primary", ""));
        items.add(adminMan.getStaticActionBarItem("/settings/save", "apply", "Apply", "success", ""));
        items.add(adminMan.getStaticActionBarItem("/admin/settings", "close", "Close", "danger", "mdi mdi-window-close"));

        model.addAttribute("actionItems", items);
        model.addAttribute("global.3.selected-tab", DespGlobals.getProperty("global.3.selected-tab"));
        model.addAttribute("tabs", places);
        return admin(model, "/settings/main");
    }


    @Crumb(label = "Admin-acl", xdesc = "Admin Access Control", family = "admin", parent = "Admin-main")
    @RequestMapping("/acl")
    public String adminACL(Model model, Locale locale) {
        return admin(model, "/acl");
    }


    /*   ==============  SAVE SELECT Settings   ==============                 */

    @RequestMapping(value = "/settings/save/select", method = RequestMethod.POST)
    public String saveSelect(@AuthenticationPrincipal DespaniPrincipal user, Model model,
                               @ModelAttribute("propForm") PropertyDataForm propForm,
                               BindingResult bindingResult)  {

        DespReferenceData despReferenceData = IDespaniMapper.MAPPER.toDespReferenceData(propForm);

        adminService.updateProperty(propForm);

        if (model.getAttribute("actionItems") != null && model.getAttribute("actionItems").toString().equalsIgnoreCase("save")) {
            return adminRedirect("/main");
        } else {
            return adminRedirect("/settings/oid/"+despReferenceData.getOid()+"/edit");
        }
    }



    /*   ==============  SAVE  Settings   ==============                 */

    @RequestMapping(value = "/settings/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String savesettings(@AuthenticationPrincipal DespaniPrincipal user, Model model,
                               @RequestParam Map<String, String> allRequestParams,
                               @ModelAttribute("propForm") PropertyDataForm propForm,
                               BindingResult bindingResult)  {


        BooleanUtils.toBoolean("on");
        adminService.saveSettings(allRequestParams);


        if (allRequestParams.get("action") != null && allRequestParams.get("action").equalsIgnoreCase("save")) {
            return adminRedirect("/main");
        } else {
            return adminRedirect("/settings");
        }
    }

    @RequestMapping(value = "settings/new", method = RequestMethod.GET )
    public String newSettings(Model model) {
        DespReferenceData refData = adminMan.getRefData("global.3.selected-tab");

        List<String> places = refData.getValues().stream().map(d -> d.getValue()).collect(Collectors.toList());
        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/settings", "back", "Back", "primary", ""));
        items.add(adminMan.getStaticActionBarItem("/settings/new/save", "add", "Save", "success", ""));

        model.addAttribute("propForm", new PropertyDataForm());
        model.addAttribute("actionItems", items);
        model.addAttribute("tabs", places);
        return admin(model, "/settings/add");
    }



    /*   ==============  Edit Settings   ==============                 */


    @RequestMapping(value = "/settings/oid/{oid}/edit", method=RequestMethod.GET)
    public String editSettings (@PathVariable("oid") Integer oid, @AuthenticationPrincipal DespaniPrincipal user,
                                Model model) {

//        props.getProps().getSystemProps();


        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/settings/save/select", "save", "Save", "primary", ""));
        items.add(adminMan.getStaticActionBarItem("/settings/save/select", "apply", "Apply", "success", ""));
        items.add(adminMan.getStaticActionBarItem("/settings", "close", "Close", "danger", "mdi mdi-window-close"));



        DespReferenceData refData = adminMan.getRefData("global.3.selected-tab");
        List<String> places = refData.getValues().stream().map(d -> d.getValue()).collect(Collectors.toList());



        model.addAttribute("oid", oid);
        model.addAttribute("actionItems", items);
        model.addAttribute("tabs", places);
        return admin(model, "/settings/edit");

    }



    @RequestMapping(value = "/settings/new/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateSettings(Model model, @ModelAttribute("propForm") PropertyDataForm propForm) {

        propForm.setName(propForm.toString());
        int type = propForm.getType();
        if(type == 3 && adminMan.hasRefData(propForm.getName()) || type != 3 && DespGlobals.hasProperty(propForm.getName())){
            model.addAttribute("errorMessage", propForm.getErrorMessage());
            model.addAttribute("target", propForm.getType());
            return newSettings(model);
        }else{
            adminService.setProperty(propForm);
            return adminRedirect("/settings");
        }

    }

    @RequestMapping(value = "/settings/delete",method = RequestMethod.GET)
    public @ResponseBody String deleteProperty(Model model, @RequestParam("name") String name,
                                 @RequestParam("type") String type){
            adminService.deleteProperty(name, type);
            return "result";
    }

    @RequestMapping(value = "/settings/reset")
    public String resetsettings(Model model){

        adminService.resetSettings();
        adminService.resetRefData();
        adminService.resetModulesData();

        return adminRedirect("/settings");
    }

    @RequestMapping(value = "/settings/initacl")
    public String initacl(@AuthenticationPrincipal DespaniPrincipal desp, Model model){

//     TODO   adminService.deleteacl();
//        adminService.initacl(desp);
        return adminRedirect("/settings");
    }

    @RequestMapping(value = "/settings/deleteacl")
    public String deleteacl( Model model){

//     TODOz   adminService.deleteacl();
        return adminRedirect("/settings/");
    }


    @RequestMapping(value = "/settings/all")
    public String resetAll(Model model, HttpServletRequest req){

        adminService.resetSettings();
        adminService.resetBreadCrumbs(req);
        return adminRedirect("/settings");

    }

    @RequestMapping(value = "/settings/rebuild/breadcrumb")
    public String rebuildBreadCrumb(Model model, HttpServletRequest req){
        adminService.resetBreadCrumbs(req);
        return adminRedirect("/main");
    }



}

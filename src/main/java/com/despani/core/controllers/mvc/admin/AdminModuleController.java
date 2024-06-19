package com.despani.core.controllers.mvc.admin;

import com.despani.core.annotations.Crumb;
import com.despani.core.beans.DespResponse;
import com.despani.core.beans.domains.DespModule;
import com.despani.core.beans.form.ModuleForm;
import com.despani.core.beans.form.ModulePositionForm;
import com.despani.core.beans.oauth.DespaniPrincipal;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.interfaces.IActionBarItem;
import com.despani.core.managers.ContentManager;
import com.despani.core.mapstruct.mappers.IDespaniMapper;
import com.despani.core.services.ModuleService;
import com.despani.core.services.RefDataServices;
import com.despani.core.utils.DespGlobals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/module")
public class AdminModuleController extends ABaseController {

    private DespModule dummy = new DespModule();

    @Autowired
    public ContentManager contentManager;

    @Autowired
    ModuleService moduleService;

    @Autowired
    public RefDataServices refDataServices;

    @Crumb(label = "Admin-Module", xdesc = "Admin Module", family = "admin", parent = "Admin-main")
    @GetMapping({"/", "/list"})
    public String menuIndex(Model model) {

        // ADD and CLOSE actions
        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/module/new", "add", "Add", "success", ""));
        items.add(adminMan.getStaticActionBarItem("/module", "close", "Close", "danger", "mdi mdi-window-close"));

        model.addAttribute("actionItems", items);

        return admin(model, dummy.getListPage());
    }



    @RequestMapping("{moduleOid}/publish/{publish}")
    public @ResponseBody
    String doPublish(@PathVariable("moduleOid") Integer moduleOid, @PathVariable("publish") Boolean publish) {
        moduleService.publishModule(moduleOid, publish);
        return "result";
    }

    @RequestMapping("{moduleOid}/showtitle/{showTitle}")
    public @ResponseBody
    String doShowTitle(@PathVariable("moduleOid") Integer moduleOid, @PathVariable("showTitle") Boolean showTitle) {
        moduleService.showTitle(moduleOid, showTitle);
        return "result";
    }

    @RequestMapping("/load/modules")
    public @ResponseBody
    HashMap<String, List<Object>> loadModules() {
        HashMap modules = new HashMap();
        modules.put("moduleList", moduleService.getModules());
        modules.put("postitionList", moduleService.getPositions("site.3.module-position"));
        return modules;
    }

    @RequestMapping(value = "/update/module/positions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<DespResponse> updatePositions(@RequestBody HashMap<String, List<ModulePositionForm>> positions) {
        moduleService.updateModulePositions(positions.get("positionsList"));
        DespResponse resp = new DespResponse("ok");
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r = new ResponseEntity<>(resp, HttpStatus.OK);
        return r;
    }

    /*   ==============  Add Module   ==============                 */

    @RequestMapping({"/new"})
    public String addModule(@AuthenticationPrincipal DespaniPrincipal user,
                            Model model) throws Exception {
        DespModule context = new DespModule();
        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/module/save", "save", "Save", "primary", ""));
        items.add(adminMan.getStaticActionBarItem("/module/save", "apply", "Apply", "success", ""));
        items.add(adminMan.getStaticActionBarItem("/module", "close", "Close", "danger", "mdi mdi-window-close"));

        model.addAttribute("context", context);
        model.addAttribute("actionItems", items);
        model.addAttribute("site.wysiwyg-editor", DespGlobals.getProperty("site.wysiwyg-editor"));


        return admin(model, "/module/add");

    }

    /*   ==============  Edit Module  ==============                 */

    @RequestMapping({"/oid/{oid}/edit"})
    public String editModule(@PathVariable("oid") Integer oid, @AuthenticationPrincipal DespaniPrincipal user,
                             Model model) throws Exception {

        DespModule context = moduleService.getModuleByOid(oid);

        List<IActionBarItem> items = new ArrayList<>();
        items.add(adminMan.getStaticActionBarItem("/module/save", "save", "Save", "primary", ""));
        items.add(adminMan.getStaticActionBarItem("/module/save", "apply", "Apply", "success", ""));
        items.add(adminMan.getStaticActionBarItem("/module", "close", "Close", "danger", "mdi mdi-window-close"));
        model.addAttribute("ModuleForm", new ModuleForm());
        model.addAttribute("actionItems", items);
        model.addAttribute("context", context);
        model.addAttribute("site.wysiwyg-editor", DespGlobals.getProperty("site.wysiwyg-editor"));

        return admin(model, context.getObjectEditPage());

    }


    /*   ==============  SAVE and APPLY   ==============                 */

    @RequestMapping({"/save"})
    public String saveModule(@AuthenticationPrincipal DespaniPrincipal user,
                             Model model, @Valid @ModelAttribute("ModuleForm") ModuleForm module,
                             BindingResult bindingResult) {

        DespModule despModule = IDespaniMapper.MAPPER.toDespaniModule(module);

        if (despModule.getOid() == -1) {
            Integer moduleMaxOrder = moduleService.getModuleMaxOrderingByPosition(despModule.getPosition());
            despModule.setOrdering(moduleMaxOrder + 1);
            moduleService.insertModule(despModule);
        } else {
            moduleService.updateModuleByOid(despModule);
        }

        if (module.getAction() != null && module.getAction().equalsIgnoreCase("save")) {
            return adminRedirect(despModule.getListLink());
        } else {
            return adminRedirect(despModule.getObjectEditLink());
        }
    }

    /*   ==============  DELETE Module   ==============                 */

    @RequestMapping("/oid/{oid}/delete")
    public String deleteModule(@PathVariable("oid") Integer oid, @AuthenticationPrincipal DespaniPrincipal user,
                               Model model) {
        moduleService.deleteModule(oid);
        return adminRedirect(dummy.getListLink());
    }


    //    @GetMapping({"/manager"})
    public String showMenuManager(Model model) {
        return admin(model, "menu/manager");
    }


}

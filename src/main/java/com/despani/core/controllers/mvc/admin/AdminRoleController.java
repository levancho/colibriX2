package com.despani.core.controllers.mvc.admin;

import com.despani.core.annotations.Crumb;
import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.form.RoleForm;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.interfaces.IActionBarItem;
import com.despani.core.mapstruct.mappers.IDespaniMapper;
import com.despani.core.platform.interfaces.IUserService;
import com.despani.core.services.RoleService;
import com.despani.core.services.rest.RestOauthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController extends ABaseController {

    @Autowired
    RoleService roleService;

    @Autowired
    public IUserService restUserService;

    private DespRole despRoleObj = new DespRole();

    @Crumb(label="Admin Roles", xdesc = "Admin Role", family="admin", parent = "Admin-main" )
    @RequestMapping({"/","/list"})
    public String getAllLocalRoles(Model model){
        List<DespRole> allLocalRoles = roleService.getAllRoles();

        List<DespRole> allGlobalRoles = restUserService.getAllRoles();


        model.addAttribute("localRoles",allLocalRoles);
        model.addAttribute("globalRoles",allGlobalRoles);
        model.addAttribute("roleForm", new RoleForm());

        if(!model.containsAttribute("localEdit") && !model.containsAttribute("globalEdit")){
            model.addAttribute("localEdit",true);
        }
        model.addAttribute("roleForm", new RoleForm());

        model.addAttribute("roleObj", despRoleObj);

        String r = despRoleObj.getObjectSaveLink();
        String r1 = despRoleObj.getObjectEditPage();
        String r2 = despRoleObj.getObjectEditLink();

        return admin(model, despRoleObj.getListPage());
    }

    // We can discuss this
    @RequestMapping( value="/list/edit/save",method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveEditRole(Model model,RedirectAttributes redirectAttributes, RoleForm roleForm){

        DespRole dr = IDespaniMapper.MAPPER.toDespRole(roleForm);
        dr.setUpdatedOn(new Date());
        dr.setRole("ROLE_"+dr.getRole());
        if(roleForm.getAction() != null && roleForm.getAction().equalsIgnoreCase("localEdit")){
            redirectAttributes.addFlashAttribute(roleForm.getAction(), true);
            roleService.updateRole(dr.getOid(),dr.getRole(),dr.getWeight(),dr.isExternal(),dr.getRoleDescription());
        }else if(roleForm.getAction() != null && roleForm.getAction().equalsIgnoreCase("globalEdit")){
            try {
                redirectAttributes.addFlashAttribute(roleForm.getAction(), false);
                restUserService.updateRole(dr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminRedirect("/role/");
    }

    @RequestMapping( value="/list/create",method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createRole(Model model, RoleForm roleForm)  {

        if(roleForm.getAction() != null && roleForm.getAction().equalsIgnoreCase("localCreate")){
            DespRole dr = IDespaniMapper.MAPPER.toDespRole(roleForm);
            dr.setUpdatedOn(new Date());
            dr.setRole("ROLE_"+dr.getRole());
            //TODO handle not updated
            if(roleService.createRole(dr.getRole(),dr.getWeight(),dr.getRoleDescription()) != 1){
                model.addAttribute("errorMsg", "Error while creating new local role");
            }else{
                model.addAttribute("successMsg", "New role successfully created.");
            }

        }else if(roleForm.getAction() != null && roleForm.getAction().equalsIgnoreCase("globalCreate")){
            DespRole dr = IDespaniMapper.MAPPER.toDespRole(roleForm);
            dr.setOid(-1);
            dr.setUpdatedOn(new Date());
            dr.setRole("ROLE_"+dr.getRole());
            //TODO handle
            if(restUserService.createRole(dr) == null){
                model.addAttribute("errorMsg", "Error while creating new global role");
            }else{
                model.addAttribute("successMsg", "New role successfully created.");
            }

        }


        return adminRedirect("/role/");
    }

}

package com.despani.core.controllers.mvc.admin;
import com.despani.core.annotations.Crumb;
import com.despani.core.beans.Pagination;
import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.form.UserForm;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.managers.UserManager;
import com.despani.core.managers.base.IDespSecurityManager;
import com.despani.core.mapstruct.mappers.IDespaniMapper;
import com.despani.core.platform.interfaces.IUserService;
import com.despani.core.services.UserServices;
import com.despani.core.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController extends ABaseController {

//    @Autowired
//    public IUserService userService;
    @Autowired
    public UserManager userManager;

    @Autowired
    public IUserService restUserService;

    @Autowired
    IDespSecurityManager despSecurityManager;

    @Autowired
    PagingUtil pagingUtil;

    private DespaniUser dummy = new DespaniUser();

//    @Autowired
//    private SessionRegistry sessionRegistry;
//
//    @RequestMapping(value = "/logedin/users")
//    @Crumb(label="Admin-All-users", xdesc = "Admin Users", family="admin", parent = "Admin-main" )
//    public String getUsersFromSessionRegistry(Model model) {
//        final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
//
//        final List<DespaniUser> users = allPrincipals.stream().map(p-> {
//            if (p instanceof DespaniPrincipal) {
//                final DespaniPrincipal pu = (DespaniPrincipal) p;
//
//                // Do something with user
//                System.out.println(pu);
//                return pu.getUser();
//            }
//            return null;
//        }).collect(Collectors.toList());
//        model.addAttribute("allcontent",users);
//        return admin(model, dummy.getObjectName()+"/list-light");
//    }

    @Crumb(label="Admin-All-users", xdesc = "Admin Users", family="admin", parent = "Admin-main" )
    @RequestMapping({"/",""})
    public String adminHome(Model model,@RequestParam(value = "page",required=false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "limit",required=false, defaultValue = "10") Integer limitp){

    Pagination p = new Pagination(dummy,userService.countUsers());
        p.setPageRanges(Arrays.asList(new Integer[] { 5, 10, 15, 25 }));
        p.calculate(pageNumber,limitp);
        model.addAttribute("p",p);

        // Local users (initial code)
//     List<DespaniUser> contentToShow =  userService.getAllUsers(p.getLimite(),p.getOffsetMultiplier());

     // REST API service for getting pagination users from auth server
     List<DespaniUser> contentToShow =  restUserService.getPagedUsers(p.getLimite(),p.getOffsetMultiplier());

        model.addAttribute("allcontent",contentToShow);
        model.addAttribute("context",dummy);
        return admin(model, dummy.getListPage());
    }


    @Crumb(label="Admin-view-users", xdesc = "Admin View User", family="admin", parent = "Admin-All-users" )
    @RequestMapping({"/oid/{oid}","/oid/{oid}/view"})
    public String viewUser(Model model,@PathVariable("oid")Integer oid) throws Exception {


        // Next line is commented because of switch to rest servicegit
        // DespaniUser localUser = userService.getUserByUserOid(oid);

        // Global user
        DespaniUser globalUser = restUserService.getUserByOid(oid);

        // Local user
        DespaniUser localUser = userService.getDespaniUser(globalUser.getEmail());

        model.addAttribute("globalUser",globalUser);
        model.addAttribute("localUser",localUser);
        model.addAttribute("userForm", new UserForm());
        return admin(model, globalUser.getObjectViewPage());
    }


//    @Crumb(label="Admin-edit-users", xdesc = "Admin edit User", family="admin", parent = "Admin-view-users" )
    @RequestMapping({"/oid/{oid}/edit"})
    public String editUser(Model model,@PathVariable("oid")Integer oid) throws Exception {

        // Next 2 lines commented because of switch to rest service
//        DespaniUser content = userService.getUserByUserOid(oid);
//        List<DespRole> allRoles = userService.getAllRoles();

        // Global user
//        DespaniUser globalUser = restUserService.getUserByOid(oid);
//        // Local user
//        DespaniUser localUser = userService.getDespaniUser(globalUser.getEmail());

        DespaniUser globalUser  = userManager.getUserByOid(oid);
        List<DespRole> allRoles = userManager.getAllRoles();

//        DespaniUser content1 = despSecurityManager.getCurrentUser().getUser();
//        // Global roles
//        List<DespRole> allRoles = restUserService.getAllRoles();
//        // App specific roles
//        List<DespRole> localRoles = userService.getAllRoles();

        model.addAttribute("user",globalUser);
        model.addAttribute("roles",allRoles);
        model.addAttribute("localUser",globalUser);
//        model.addAttribute("localRoles",localRoles);

        // add save, apply , close actions
        addDefaultDynamicActionBar(model,globalUser);

        model.addAttribute("userForm", new UserForm());
        return admin(model, globalUser.getObjectEditPage());
    }


    @RequestMapping({"/save"})
    public String saveEditUser(Model model, UserForm form, @AuthenticationPrincipal Principal principal) throws URISyntaxException {

        DespaniUser userIn = IDespaniMapper.MAPPER.toDespaniUser(form);

        // Update local user
        int localOid =form.getOid();
        userManager.updateRolesForOid(localOid,form.getRoles(),form.getLocalRoles());

        // Delete all local user roles, we can optimize this
        // userService.deleteUserAllRoles(localOid);
        // Update local user roles
        // userService.saveUserRoles(localOid,form.getLocalRoles());

        // Delete and and save new roles for local user


        // Update global user
        Integer retOid =  userManager.editUser(userIn);



       if(retOid != null  ){
           userIn = restUserService.getUserByOid(localOid);
       }


//        dc.setUpdatedOn(new Date());
//        dc.setUpdatedByOid(user.getUser().getOid());
//        contentService.saveContent(dc);

        return adminRedirect(userIn);
    }


    @RequestMapping("/{userOid}/lock/{lock}")
    public @ResponseBody
    String doLock(@PathVariable("userOid") Integer userOid, @PathVariable("lock") Boolean lock){
        userService.lockUser(userOid,lock);
        return "ok";
    }

    @RequestMapping("/{userOid}/enable/{enable}")
    public @ResponseBody
    String doEnable(@PathVariable("userOid") Integer userOid, @PathVariable("enable") Boolean enable){
        userService.enableUser(userOid,enable);
        return "ok";
    }

}

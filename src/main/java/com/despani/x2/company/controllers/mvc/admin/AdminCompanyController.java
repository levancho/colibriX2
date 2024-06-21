package com.despani.x2.company.controllers.mvc.admin;


import com.despani.x2.company.beans.domains.DespCompany;
import com.despani.x2.company.beans.form.DespCompanyForm;
import com.despani.x2.company.services.DespCompanyService;
import com.despani.x2.core.beans.DespResponse;
import com.despani.x2.core.xusers.beans.domains.DespAddress;
import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.controllers.mvc.ABaseController;
import com.despani.x2.core.xusers.mybatis.mappers.IUserMapper;
import com.despani.x2.core.xusers.services.DespAddressService;
import com.despani.x2.products.beans.domains.DespProduct;
import com.despani.x2.products.services.DespProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/company")
public class AdminCompanyController extends ABaseController {

    @Autowired
    DespCompanyService despCompanyService;

    @Autowired
    DespProductService despProductService;



    @Autowired
    DespAddressService despAddressService;

    @Autowired
    public IUserMapper userMapper;

    @Autowired
    com.despani.x2.core.services.AWSFileManagementService AWSFileManagementService;

//    @Autowired
//    DespMediaService despMediaService;


    @RequestMapping({"/list"})
    public String getCompaniesList(Model model) throws Exception {
        List<DespCompany> despCompanyList = despCompanyService.getAllDespCompanyAsList();
        model.addAttribute("despCompanyList", despCompanyList);
        return admin(model, "/company/list");
    }


    @RequestMapping({"/add"})
    public String addCompany(Model model) throws Exception {
        return admin(model, "/company/add");
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveCompany(
            @RequestParam(value = "companyTitle", required = false) String companyTitle,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyType", required = false) String companyType,
            @RequestParam(value = "companyRepresentative", required = false) String companyRepresentative,
            @RequestParam(value = "companyDescription", required = false) String companyDescription,
            @RequestParam(value = "companyAddress", required = false) String companyAddress,
            @RequestParam(value = "companyCity", required = false) String companyCity,
            @RequestParam(value = "companyState", required = false) String companyState,
            @RequestParam(value = "companyZipCode", required = false) String companyZipCode,
            @RequestParam(value = "companyAddressName", required = false) String companyAddressName,
            @RequestParam(value = "companyStatus", required = false) String companyStatus,
            Model model) throws Exception {

        DespCompany despCompany = new DespCompany();
        DespaniUser despaniUser = userMapper.getUserByOid(69);
        DespAddress despAddress = new DespAddress("Billing address", companyAddress, companyCity, companyState,
                Integer.parseInt(companyZipCode));
        despAddressService.createDespAddress(despAddress);

        despCompany.setTitle(companyTitle);
        despCompany.setName(companyName);
        despCompany.setType(companyType);
        despCompany.setPublished(false);
        despCompany.setAddress(despAddress);
        despCompany.setRepresentative(despaniUser);
        despCompany.setDescription(companyDescription);
        despCompany.setAlias(companyName);
        despCompany.setXlang("en");
        Set <String> my = new LinkedHashSet<>();

        despCompanyService.insertDespCompany(despCompany);
        return "redirect:/company/list";
    }


    @RequestMapping(value = "/oid/{oid}/update", method = RequestMethod.POST)
    public String saveCompany(
            @PathVariable("oid") Integer oid,
            @RequestParam(value = "companyTitle", required = false) String companyTitle,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyType", required = false) String companyType,
            @RequestParam(value = "companyRepresentative", required = false) String companyRepresentative,
            @RequestParam(value = "companyDescription", required = false) String companyDescription,
            @RequestParam(value = "companyAddress", required = false) String companyAddress,
            @RequestParam(value = "companyCity", required = false) String companyCity,
            @RequestParam(value = "companyState", required = false) String companyState,
            @RequestParam(value = "companyZipCode", required = false) String companyZipCode,
            @RequestParam(value = "companyAddressName", required = false) String companyAddressName,
            @RequestParam(value = "companyStatus", required = false) String companyStatus,
            Model model) throws Exception {

//  get company
        DespCompany company = despCompanyService.getDespCompanyByOid(oid);
//  get address by address oid and update address
        DespAddress address = despAddressService.getDespAddressByOid(company.getAddress().getOid());
        address.setName(companyAddressName);
        address.setAddress(companyAddress);
        address.setCity(companyCity);
        address.setState(companyState);
        address.setZipCode(Integer.parseInt(companyZipCode));
        despAddressService.updateDespAddress(address);
// set default user
        DespaniUser despaniUser = userMapper.getUserByOid(69);
//  update company
        company.setTitle(companyTitle);
        company.setName(companyName);
        company.setType(companyType);
        company.setPublished(false); // TODO : fix. should be published actual value
        company.setAddress(address);
        company.setRepresentative(despaniUser);
        company.setDescription(companyDescription);
        company.setAlias(companyName);
        company.setXlang("en");
        despCompanyService.updateDespCompany(company);
        return "redirect:/company/list";
    }


    @RequestMapping({"/oid/{oid}/view"})
    public String viewCompany(@PathVariable("oid") Integer oid, Model model) throws Exception {
        DespCompany despCompany = despCompanyService.getDespCompanyByOid(oid);
        model.addAttribute("despCompany", despCompany);
        return admin(model, despCompany.getObjectViewPage());
    }

    @RequestMapping({"/oid/{oid}/edit"})
    public String editCompany(@PathVariable("oid") Integer oid, Model model) throws Exception {
        DespCompany despCompany = despCompanyService.getDespCompanyByOid(oid);
        model.addAttribute("despCompany",despCompany);
        return admin(model, despCompany.getObjectEditPage());
    }


    @RequestMapping(value = "/oid/{oid}/addMedia", method = RequestMethod.POST)
    public String addMedia(@RequestParam("files") MultipartFile[] files, @PathVariable("oid") Integer oid, Model model) throws IOException {

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                InputStream initialStream = file.getInputStream();
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                if (file != null) {
                    despCompanyService.uploadCompanyFile(new ByteArrayInputStream(buffer), file.getOriginalFilename(), oid);
                }
            }
            DespCompany despCompany = despCompanyService.getDespCompanyByOid(oid);
            model.addAttribute("despCompany", despCompany);
            return admin(model, despCompany.getObjectViewPage());
        }

        return admin(model, "/company/error");
    }


//    @RequestMapping(value = "/deleteMedia/{mediaOid}", method = RequestMethod.POST)
//    @ResponseBody
//    @Transactional
//    public String deleteCompanyMedia( Model model, @PathVariable Integer mediaOid) throws IOException {
//        DespMedia media = despMediaService.getDespMediaByOid(mediaOid);
//        AWSFileManagementService.deleteFileFromAWS("zaconne", media.getFilename());
//        despMediaService.deleteDespMediaByOid(mediaOid);
//        return admin(model, "/company/view");
//    }


    @RequestMapping({"/oid/{oid}/delete"})
    public String deleteCompany(@PathVariable("oid") Integer oid, Model model) throws Exception {
        return admin(model, "/company/delete");
    }


    @RequestMapping("/oid/{oid}/enable/{action}")
    public @ResponseBody
    String publishCompany(@PathVariable("oid") Integer userOid, @PathVariable("action") Boolean enable) {
        return "ok";
    }

    @Transactional
    @RequestMapping(value = "add/company", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<DespResponse> saveCompany(@RequestBody DespCompanyForm companyForm ){
        DespCompany despCompany = new DespCompany();
        DespAddress despAddress = new DespAddress("Billing address", companyForm.getAddress(), companyForm.getCity(), companyForm.getState(), Integer.parseInt(companyForm.getZipCode()));
        despAddressService.createDespAddress(despAddress);
        DespaniUser despaniUser = userMapper.getUserByOid(69);
        despCompany.setName(companyForm.getName());
        despCompany.setType(companyForm.getType());
        despCompany.setPublished(companyForm.isPublished());
        despCompany.setAddress(despAddress);
        despCompany.setRepresentative(despaniUser);
        despCompany.setDescription(companyForm.getCompanyDescription());
        despCompanyService.insertDespCompany(despCompany);

        DespResponse resp = new DespResponse("ok");
        resp.setMessage("success");
        resp.setSuccess(true);
        resp.setErrorCode("0");
        ResponseEntity<DespResponse> r = new ResponseEntity<>(resp, HttpStatus.OK);
        return r;
    }


    @RequestMapping(value = {"/list/company/oid/{oid}"})
    public String getProductListByCompany(@PathVariable(required = false) Integer oid, Model model) throws Exception {
        List<DespProduct> products;
        if(oid != null){
            products = despProductService.getParentProductsByCompanyOid(oid);
            DespCompany company = despCompanyService.getDespCompanyByOid(oid);
            model.addAttribute("company", company);
        }else{
            products = despProductService.getAllParentDespProducts();
        }
        model.addAttribute("products", products);
        return admin(model, "/product/list");
    }



}

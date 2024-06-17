package com.despani.core.controllers.mvc.admin;
import com.despani.core.beans.domains.*;
import com.despani.core.beans.form.ProductForm;
import com.despani.core.controllers.mvc.ABaseController;
import com.despani.core.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/product")
public class AdminProductController extends ABaseController {

    @Autowired
    AWSFileManagementService awsService;

    @Autowired
    DespProductService despProductService;

    @Autowired
    DespCompanyService despCompanyService;

    @Autowired
    DespMediaService despMediaService;

    @Autowired
    CategoryServices categoryServices;




    @PostMapping("/upload/file")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        awsService.uploadProductFile(file.getInputStream(),file.getOriginalFilename(), 1);
       return ResponseEntity.ok("File Uploaded Succesfully");
    }


    @RequestMapping({"/oid/{oid}/view"})
    public String getAllProductList(@PathVariable("oid") Integer oid, Model model) throws Exception {
        DespProduct product = despProductService.getProductWithParentByOid(oid);
        if(product.getXParent() == null){
            List<DespProduct> childProducts = despProductService.getChildProductsByParentOid(product.getOid());
            model.addAttribute("children" , childProducts);
        }
        model.addAttribute("product", product);
        return admin(model, "/product/view");
    }


    @RequestMapping(value = {"/list","/list/company/oid/{oid}"})
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

    @RequestMapping(value = {"/new", "/new/parentOid/{oid}"})
    public String addProduct(@PathVariable(required = false) Integer oid, Model model) throws Exception {

            if(oid != null){
                DespProduct product = despProductService.getDespProductByOid(oid);
                model.addAttribute("parent", product );
            }
            List<DespCompany> companies = despCompanyService.getAllDespCompanys();
            List<DespCategoryItem> categories = categoryServices.getNodes(1, "tashi");
            model.addAttribute("product", new ProductForm());
            model.addAttribute("companies", companies);
            model.addAttribute("categories", categories);
            return admin(model, "/product/add");
    }


    @RequestMapping({"/oid/{oid}/edit"})
    public String editProduct(@PathVariable("oid") Integer oid, Model model) throws Exception {
        List<DespCompany> companies = despCompanyService.getAllDespCompanys();
        List<DespCategoryItem> categories = categoryServices.getNodes(1, "tashi");
        DespProduct product = despProductService.getDespProductByOid(oid);
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("product", product);
        model.addAttribute("companies", companies);
        model.addAttribute("categories", categories);
        return admin(model, "/product/edit");
    }

    @RequestMapping({"/save"})
    public String saveProduct(@ModelAttribute("product") ProductForm productForm, Model model) throws Exception {
        DespProduct newProduct  = despProductService.createProduct(productForm);
        despProductService.insertDespProduct(newProduct);
        int productOid = -1;
        if(productForm.getParentOid() != -1){
            productOid = productForm.getParentOid();
            return "redirect:/product/oid/"+productForm.getParentOid()+"/view";
        }else{
            productOid = newProduct.getOid();
        }
        return "redirect:/product/oid/"+productOid+"/view";
    }

    @RequestMapping({"update"})
    public String updateProduct(@ModelAttribute("product") ProductForm productForm, Model model) throws Exception {

        DespProduct product  = despProductService.createProduct(productForm);
        despProductService.updateDespProduct(product);
        DespProduct fullProduct = despProductService.getProductWithParentByOid(product.getOid());
        if(fullProduct.getParent() != null){
           fullProduct =  fullProduct.getParent();
        }
        List<DespProduct> products = despProductService.getChildProductsByParentOid(productForm.getParentOid());
        model.addAttribute("children", products);
        model.addAttribute("product", fullProduct);
        return admin(model, "/product/view");
    }



    @RequestMapping("deleteProduct/oid/{productOid}")
    @Transactional
    public @ResponseBody
    String deleteProduct(@PathVariable("productOid") Integer productOid,
                               Model model) throws IOException {
        List<DespProduct> children = despProductService.getChildProductsByParentOid(productOid);
        List<DespMedia> mediaList = despMediaService.getDespMediaByProductOid(productOid);

        if(!children.isEmpty()){
            children.forEach(child->{
                mediaList.addAll(despMediaService.getDespMediaByProductOid(child.getOid()));
            });
            despProductService.deleteChildProducts(productOid);
        }

        if(!mediaList.isEmpty()){
            mediaList.forEach(media -> {
                awsService.deleteFileFromAWS("zaconne", media.getFilename());
                despMediaService.deleteDespMediaByOid(media.getOid());
            });
        }
        despProductService.deleteDespProduct(productOid);

        return "result";
    }


    @RequestMapping("publish/{productOid}/{publish}")
    public @ResponseBody
    String publishProduct(@PathVariable("productOid") Integer productOid, @PathVariable("publish") Boolean publish) {
        despProductService.publishProduct(productOid, publish);
        return "result";
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
                    awsService.uploadProductFile(new ByteArrayInputStream(buffer), file.getOriginalFilename(), oid);
                }
            }
            return "redirect:/product/oid/"+oid+"/view";
        }

        return admin(model, "/product/error");
    }

    @RequestMapping("{productOid}/media/{mediaOid}/setPrimary/{primary}")
    @Transactional
    public @ResponseBody
    String makePrimary(@PathVariable("productOid") Integer productOid, @PathVariable("mediaOid") Integer mediaOid, @PathVariable("primary") Boolean primary) {
        despMediaService.resetDespMediaPrimary(productOid);
        if(primary) {
            despMediaService.makeDespMediaPrimary(mediaOid, primary);
        }
        return "result";
    }


    @RequestMapping(value = "media/delete", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody String deleteProductMedia(Model model, @RequestParam("mediaOid") Integer mediaOid) throws IOException {
        DespMedia media = despMediaService.getDespMediaByOid(mediaOid);
        awsService.deleteFileFromAWS("zaconne", media.getFilename());
        despMediaService.deleteDespMediaByOid(mediaOid);
        return "result";
    }




    }


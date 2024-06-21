package com.despani.x2.products.services;


import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.company.beans.domains.DespCompany;
import com.despani.x2.core.services.AWSFileManagementService;
import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import com.despani.x2.core.xmedia.services.DespMediaService;
import com.despani.x2.products.beans.domains.DespProduct;
import com.despani.x2.products.beans.form.ProductForm;
import com.despani.x2.products.mybatis.mappers.IDespMediaProductMapper;
import com.despani.x2.products.mybatis.mappers.IDespProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DespProductService {

    @Autowired
    IDespProductMapper iDespProductMapper;



    @Autowired
    AWSFileManagementService awsService;

    @Autowired
    DespMediaService despMediaService;

    @Autowired
    IDespMediaProductMapper despMediaProductMapper;



    public void insertDespProduct(DespProduct despProduct){
        iDespProductMapper.insertDespProduct(despProduct);
    }

    public void updateDespProduct(DespProduct despProduct){
        iDespProductMapper.updateProduct(despProduct);
    }

    public  DespProduct getDespProductByOid(Integer oid){
        return  iDespProductMapper.selectProductByOid(oid);
    }

    public  DespProduct getProductWithParentByOid(Integer oid){
        return  iDespProductMapper.selectProductWithParentByOid(oid);
    }



    public List<DespProduct> getAllParentDespProducts(){
        return iDespProductMapper.selectParentProducts();
    }


    public List<DespProduct> getChildProductsByParentOid(int parentOid) {
        return iDespProductMapper.selectProductsByParentOid(parentOid);
    }

    public List<DespProduct> getParentProductsByCompanyOid(int companyOid) {
        return iDespProductMapper.selectParentProductsByCompanyOid(companyOid);
    }

    public DespProduct createProduct(ProductForm productForm){
        DespProduct product = new DespProduct();
        product.setOid(productForm.getOid());
        product.setTitle(productForm.getTitle().trim());
        product.setDescription(productForm.getDescription().trim());
        product.setSku(productForm.getSku().trim());
        product.setPrice(productForm.getPrice());
        product.setPublished(productForm.getPublished());
        if(productForm.getParentOid()!= -1){
            product.setParent(new DespProduct(productForm.getParentOid()));
        }
        product.setCategory(new DespCategoryItem(productForm.getCategoryOid()));
        product.setCompany(new DespCompany(productForm.getCompanyOid()));
        return product;
    }



    public void deleteDespProduct(Integer oid){
        iDespProductMapper.deleteProduct(oid);
    }

    public void deleteChildProducts(Integer oid){
        iDespProductMapper.deleteChildProducts(oid);
    }


    public void publishProduct(int oid, Boolean published){
        iDespProductMapper.publishProduct(oid, published);
    }


    /** DESP-MEDIA-PRODUCT **/
    public void createMediaForProduct(int mediaOid, int productOid){
        despMediaProductMapper.insertDespMediaProduct(mediaOid, productOid);
    }

    public void updateMediaForProduct(int oid, int mediaOid, int productOid){
        despMediaProductMapper.updateDespMediaProduct(oid, mediaOid, productOid);
    }

    public void deleteMediaPForProduct(int oid){
        despMediaProductMapper.deleteDespMediaProductByOid(oid);
    }


    public void uploadProductFile(InputStream input, String filename, int productOid) throws IOException {
        DespMedia media = awsService.generateMedia(input, filename, productOid, "product");
        int id = media.getOid();
        createMediaForProduct(id, productOid);
    }




}

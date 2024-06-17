package com.despani.core.services;


import com.despani.core.beans.domains.DespCategoryItem;
import com.despani.core.beans.domains.DespCompany;
import com.despani.core.beans.domains.DespProduct;
import com.despani.core.beans.form.ProductForm;
import com.despani.core.mybatis.mappers.IDespProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DespProductService {

    @Autowired
    IDespProductMapper iDespProductMapper;


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


}

package com.despani.x2.companies.services;

import com.despani.x2.companies.beans.domains.DespCompany;
import com.despani.x2.companies.mybatis.mappers.IDespCompanyMapper;
import com.despani.x2.companies.mybatis.mappers.IDespMediaCompanyMapper;
import com.despani.x2.core.services.AWSFileManagementService;
import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import com.despani.x2.core.xmedia.services.DespMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DespCompanyService {

    @Autowired
    IDespCompanyMapper iDespCompanyMapper;

    @Autowired
    AWSFileManagementService awsService;

    @Autowired
    DespMediaService despMediaService;

    @Autowired
    IDespMediaCompanyMapper despMediaComapnyMapper;


    public void insertDespCompany(DespCompany despCompany){
        iDespCompanyMapper.createDespCompany(despCompany);
    }

    public DespCompany getDespCompanyByOid(Integer oid) {

        return iDespCompanyMapper.getDespCompanyByOid(oid);
    }

    public DespCompany getDespCompanyByName(String name) {
        return iDespCompanyMapper.getDespCompanyByName(name);
    }

    public List<DespCompany> getDespCompanyByType(String type) {
        return iDespCompanyMapper.getDespCompanyByType(type);
    }

    public List<DespCompany> getAllDespCompanys() {
        return iDespCompanyMapper.getALLDespCompanys();
    }

    public void deleteDespCompany(Integer oid) {
        iDespCompanyMapper.deleteDespCompany(oid);
    }

    public List<DespCompany> getAllDespCompanyAsList() {
        return iDespCompanyMapper.getAllDespCompanyAsList();
    };

    public void updateDespCompany(DespCompany despCompany){
        iDespCompanyMapper.updateDespCompany(despCompany);
    }



    /** DESP-MEDIA-COMPANY **/
    public void createMediaForCompany(int mediaOid, int companyOid) {
        despMediaComapnyMapper.insertDespMediaCompany(mediaOid,companyOid);
    }

    public void updateMediaForCompany(int oid, int mediaOid, int productOid){
        despMediaComapnyMapper.updateDespMediaCompany(oid, mediaOid, productOid);
    }

    public void deleteMediaPForCompany(int oid){
        despMediaComapnyMapper.deleteDespMediaCompanyByOid(oid);
    }


    public void uploadCompanyFile(InputStream input, String filename, int companyOid) throws IOException {
        DespMedia media = awsService.generateMedia(input, filename, companyOid, "company");
        int id = media.getOid();
        createMediaForCompany(id, companyOid);
    }

}

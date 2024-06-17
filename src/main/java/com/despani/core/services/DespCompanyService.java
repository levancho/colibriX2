package com.despani.core.services;

import com.despani.core.beans.domains.DespCompany;
import com.despani.core.mybatis.mappers.IDespCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespCompanyService {

    @Autowired
    IDespCompanyMapper iDespCompanyMapper;


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

}

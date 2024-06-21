package com.despani.x2.core.services;

import com.despani.x2.contents.interfaces.IContentServices;
import com.despani.x2.core.beans.DespReferenceData;
import com.despani.x2.core.beans.enums.DespPropertyType;
import com.despani.x2.core.beans.form.PropertyDataForm;
import com.despani.x2.core.beans.form.ReferenceValueForm;
import com.despani.x2.core.xmodules.services.ModuleService;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.mapstruct.IDespaniMapper;
import com.despani.x2.core.utils.DespGlobals;
import com.despani.x2.core.utils.DespPropertyX2;
import com.despani.x2.core.xusers.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public class AdminServices {


    @Autowired
    private RefDataServices refData;

    @Autowired
    private ModuleService modService;

    @Autowired
    public IContentServices contentService;
    @Autowired
    public IUserService userService;

    @Autowired
    public ACLService aclService;

   @Autowired
    public JdbcMutableAclService JdbcAcl;

    @Autowired
    private RefDataServices refDataService;

    // TODO protected method.
    public void saveSettings (Map<String, String> nodeItemForm) {
        DespGlobals.setProperties(nodeItemForm);
    }

    public void resetSettings() {
        DespGlobals.reset();

    }

    public void resetBreadCrumbs(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if(session!=null){
            session.removeAttribute("breadCrumb");
        }

    }




    public void resetRefData() {

        refData.reset();
    }

    public void resetModulesData() {

        modService.reset();
    }



    @Transactional(rollbackFor = Exception.class)
    public void setProperty(PropertyDataForm prop){
        if(prop.getType() == DespPropertyType.SELECT.getCode()) {
            DespReferenceData refData = IDespaniMapper.MAPPER.toDespReferenceData(prop);
            refDataService.insertReferenceData(refData);
            int id = refData.getOid();

            ReferenceValueForm refdefault = prop.getRefValues().stream().filter(v -> v.isDefaultVal()).findAny().orElse(null);
            if (refdefault!=null) {
                DespGlobals.setProperty(refData.getTypeKey(), refdefault.getName());
            }

            /** Building reference values objects from propForm refValues Map */
            refData.getValues().stream().forEach(value -> {
                refDataService.insertRefValue(value, id);

            });
            resetRefData();
        }else{
            DespPropertyX2 property = IDespaniMapper.MAPPER.toDespPropertyX2(prop);
            DespGlobals.setProperty(property);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateProperty(PropertyDataForm prop){
        if(prop.getType() == 3 ) {



            //TODO : WTF !!!
            DespReferenceData refData = IDespaniMapper.MAPPER.toDespReferenceData(prop);

            refDataService.updateReferenceData(refData);
            int id = refData.getOid();

            /** Building reference values objects from propForm refValues Map */
            refDataService.deleteAllReferenceDataValues(id);
            refData.getValues().stream().forEach(value -> {
              //  if (value.isDefaultVal()) {
               //     DespGlobals.setProperty(refData.getTypeKey(), value.getName());
               // }

                refDataService.insertRefValue(value, id);

            });
            resetRefData();
        }
    }








    @Transactional(rollbackFor = Exception.class)
    public void deleteProperty(String key, String type) {
        if (type.equalsIgnoreCase("prop")) {
            DespGlobals.deleteProperty(key);
        } else{
            DespReferenceData referenceData = refData.getReferenceData(key);
        int id = referenceData.getOid();
        refDataService.deleteReferenceData(id);
        refDataService.deleteAllReferenceDataValues(id);
        DespGlobals.deleteProperty(key);
        resetRefData();
        }
    }


}

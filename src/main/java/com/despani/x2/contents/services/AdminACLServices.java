package com.despani.x2.contents.services;

import com.despani.x2.contents.beans.domains.DespContent;
import com.despani.x2.contents.interfaces.IContentServices;
import com.despani.x2.core.services.ACLService;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.xusers.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminACLServices {



    @Autowired
    public IContentServices contentService;
    @Autowired
    public IUserService userService;

    @Autowired
    public ACLService aclService;

   @Autowired
    public JdbcMutableAclService JdbcAcl;




    @Transactional
    public void initacl(DespaniPrincipal desp) {
        List<DespContent> allContent = contentService.getAllContent();
        for (DespContent despContent : allContent) {
            aclService.applyACL(despContent);
        }
    }


    @Transactional
    public void deleteacl() {

        List<DespContent> allContent = contentService.getAllContent();


        for (DespContent despContent : allContent) {
            ObjectIdentity oi = new ObjectIdentityImpl(DespContent.class,despContent.getOid());
            JdbcAcl.deleteAcl(oi, true);
        }



// Now grant some permissions via an access control entry (ACE)
//        acl.insertAce(acl.getEntries().length, p, sid, true);
//        aclService.updateAcl(acl);

    }







}

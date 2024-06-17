package com.despani.core.services;

import com.despani.core.beans.base.ADespaniObject;
import com.despani.core.config.beans.AclAuditLogger;
import com.despani.core.utils.IRoles;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.*;

    @Service
    public class ACLService {

        private final PermissionGrantingStrategy permissionGrantingStrategy;
        private JdbcMutableAclService aclService;
        private AclAuditLogger aclAuditLogger = new AclAuditLogger();

        public ACLService(JdbcMutableAclService aclService, PermissionGrantingStrategy permissionGrantingStrategy) {
            this.aclService = aclService;
            this.permissionGrantingStrategy = permissionGrantingStrategy;
        }

        public AuditableAcl removePermissionFromUser(String type, Long id, String principal, Permission[] permissions) {
            return removePermissions(type, id, new PrincipalSid(principal), permissions);
        }

        private AuditableAcl removePermissions(String type, Long id, PrincipalSid principalSid, Permission[] permissions) {
            Assert.notEmpty(permissions, "Permission must be not empty");
            AuditableAcl acl = get(type, id);
            int index = 0;
            for (AccessControlEntry entry : acl.getEntries()) {
                boolean deletedEntry = false;
                for (Permission permission : permissions) {
                    if (entry.isGranting()
                            && entry.getSid().equals(principalSid)
                            && entry.getPermission().equals(permission)) {
                        acl.deleteAce(index);
                        deletedEntry = true;
                    }
                }
                index = deletedEntry ? index : index + 1;
                if (deletedEntry) {
                    aclAuditLogger.logRemovePermission(entry);
                }
            }
            return (AuditableAcl) aclService.updateAcl(acl);
        }


        public AuditableAcl grantPermissionsToUser(String type, Long id, String principal, Permission[] permissions) {
            return grantPermissions(type, id, new PrincipalSid(principal), permissions);
        }

        public AuditableAcl grantPermissionsToRole(String type, Long id, String sidName, Permission[] permissions) {
            return grantPermissions(type, id, new GrantedAuthoritySid(sidName), permissions);
        }

        private AuditableAcl grantPermissions(String type, Long id, Sid sid, Permission[] permissions) {
            Assert.notEmpty(permissions, "Permission must be not empty");
            AuditableAcl acl = getOrCreate(type, id);
            Set<Integer> indices = new HashSet<>();
            for (Permission permission : permissions) {
                int index = acl.getEntries().size();
                boolean granting = true;
                acl.insertAce(index, permission, sid, granting);
                indices.add(index);
            }

            for (Integer index : indices) {
                acl.updateAuditing(index, true, true);
            }

            acl = (AuditableAcl) aclService.updateAcl(acl);

            for (Integer index : indices) {
                aclAuditLogger.logGrantPermission(acl.getEntries().get(index));
            }
            return acl;
        }

        public void setParent(String type, Long id, String parentType, Serializable parentId) {
            MutableAcl acl = get(type, id);
            Assert.notNull(acl, "Acl (type =" + type + ", id =" + id + ") could not be retrieved");

            MutableAcl aclParent = get(parentType, parentId);
            Assert.notNull(aclParent, "Acl of parent (type =" + parentType + ", id =" + parentId + ") could not be retrieved");

            acl.setParent(aclParent);
            aclService.updateAcl(acl);
        }

        private AuditableAcl getOrCreate(String type, Long id) {
            AuditableAcl acl = get(type, id);
            if (acl == null) {
                acl = create(type, id);
            }
            return acl;
        }

        private AuditableAcl create(String type, Long id) {
            AuditableAcl acl = (AuditableAcl) aclService.createAcl(new ObjectIdentityImpl(type, id));
            Assert.notNull(acl, "Acl could not be retrieved or created");
            return acl;
        }

        private AuditableAcl get(String type, Serializable id) {
            try {
                return (AuditableAcl) aclService.readAclById(new ObjectIdentityImpl(type, id));
            } catch (NotFoundException exception) {
                return null;
            }
        }

        public boolean hasUserPermission(String type, Long id, String principal, Permission[] permissions) {
            try {
                Acl acl = aclService.readAclById(new ObjectIdentityImpl(type, id));
                if (acl != null) {
                    return permissionGrantingStrategy
                            .isGranted(
                                    acl,
                                    Arrays.asList(permissions),
                                    Collections.singletonList(new PrincipalSid(principal)),
                                    true);
                } else {
                    return false;
                }
            } catch (NotFoundException exception) {
                return false;
            }
        }

        @Transactional
        public void applyACL(ADespaniObject desp) {

            String name=desp.getClass().getName();
            Long oid = new Long(desp.getOid());

             grantPermissionsToRole(name,oid, IRoles.ROLE_USER,
                    new Permission[]{
                            BasePermission.READ
                    }
            );

             grantPermissionsToRole(name,oid,IRoles.ROLE_EDITOR,
                    new Permission[]{
                            BasePermission.READ,
                            BasePermission.WRITE,
                            BasePermission.CREATE
                    }
             );

             grantPermissionsToRole(name,oid,IRoles.ROLE_MANAGER,
                    new Permission[]{
                            BasePermission.READ,
                            BasePermission.WRITE,
                            BasePermission.CREATE,
                            BasePermission.DELETE
                    }
            );

             grantPermissionsToRole(name,oid,IRoles.ROLE_ADMIN,
                    new Permission[]{
                            BasePermission.READ,
                            BasePermission.WRITE,
                            BasePermission.CREATE,
                            BasePermission.DELETE,
                            BasePermission.ADMINISTRATION
                    }
            );

            grantPermissionsToRole(name,oid,IRoles.ROLE_SUPER_ADMIN,
                    new Permission[]{
                            BasePermission.READ,
                            BasePermission.WRITE,
                            BasePermission.CREATE,
                            BasePermission.DELETE,
                            BasePermission.ADMINISTRATION
                    }
            );

        }



}

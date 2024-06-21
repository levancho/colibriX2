package com.despani.x2.core.config.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.AuditableAccessControlEntry;
import org.springframework.util.Assert;

@Slf4j
public class AclAuditLogger implements AuditLogger {

    @Override
    public void logIfNeeded(boolean isGranted, AccessControlEntry ace) {
        Assert.notNull(ace, "AccessControlEntry required");

        if (ace instanceof AuditableAccessControlEntry) {
            AuditableAccessControlEntry auditableAce = (AuditableAccessControlEntry) ace;

            // log only in case ACE configures auditSuccess = true
            if (isGranted && auditableAce.isAuditSuccess()) {
                log.info("GRANTED due to ACE: " + ace);
            }
            // log only in case ACE configures auditFailure = true
            if (!isGranted && auditableAce.isAuditFailure()) {
                log.warn("DENIED due to ACE: " + ace);
            }
        }
    }

    public void logGrantPermission(AccessControlEntry ace) {
        Assert.notNull(ace, "AccessControlEntry required");
        log.info("CREATED ACE: " + ace);
    }

    public void logRemovePermission(AccessControlEntry ace) {
        Assert.notNull(ace, "AccessControlEntry required");
        log.info("REMOVED ACE: " + ace);
    }
}

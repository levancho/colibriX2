package com.despani.core.config;

import com.despani.core.config.beans.AclAuditLogger;
import com.despani.core.utils.AppUtils;
import com.despani.core.utils.IRoles;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.security.Principal;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    DataSource dataSource ;

    private ApplicationContext context;
//    private PermissionEvaluator permissionEvaluator;


    AclMethodSecurityConfiguration(ApplicationContext context,DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
//        this.permissionEvaluator = permissionEvaluator;
    }


    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new AclAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public MethodSecurityExpressionHandler myMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =

        new DefaultMethodSecurityExpressionHandler() {

            @SneakyThrows
            @Override
            public StandardEvaluationContext createEvaluationContextInternal(final Authentication auth, final MethodInvocation mi) {

                StandardEvaluationContext evaluationContext = super.createEvaluationContextInternal(auth, mi);
                evaluationContext.registerFunction("roleWeight",
                        AppUtils.class.getDeclaredMethod("roleWeight", new Class[] { Principal.class }));

                return evaluationContext;
            }
        };
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService(dataSource));

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setRoleHierarchy(roleHierarchy());
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService(dataSource)));
        return expressionHandler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return this.myMethodSecurityExpressionHandler();
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(IRoles.ROLE_HIERARCHY);
        return roleHierarchy;
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }


    @Bean
    public LookupStrategy lookupStrategy( DataSource ds) {
        return new BasicLookupStrategy(ds, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService( DataSource ds) {
        JdbcMutableAclService j=  new JdbcMutableAclService(dataSource, lookupStrategy(ds), aclCache());
        j.setClassIdentityQuery( "SELECT LAST_INSERT_ID() ");
        j.setSidIdentityQuery("SELECT LAST_INSERT_ID() ");
        return j;
    }




}

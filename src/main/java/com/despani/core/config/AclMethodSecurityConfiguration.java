package com.despani.core.config;

import com.despani.core.config.beans.AclAuditLogger;
import com.despani.core.utils.AppUtils;
import com.despani.core.utils.IRoles;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
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
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.sql.DataSource;
import java.security.Principal;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class AclMethodSecurityConfiguration   {

    DataSource dataSource ;

    private ApplicationContext context;
//    private PermissionEvaluator permissionEvaluator;


    AclMethodSecurityConfiguration(ApplicationContext context,DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
//        this.permissionEvaluator = permissionEvaluator;
    }


//    @Bean
//    public SpringCacheBasedAclCache aclCache(AclCache defaultAclCache) {
//        return new SpringCacheBasedAclCache(defaultAclCache, permissionGrantingStrategy(), aclAuthorizationStrategy());
//    }

//    @Bean
//    public JCacheManagerFactoryBean aclEhCacheFactoryBean(JCacheCacheManager aclCacheManager) {
//        JCacheManagerFactoryBean ehCacheFactoryBean = new JCacheManagerFactoryBean();
//        ehCacheFactoryBean.(aclCacheManager);
//        ehCacheFactoryBean.setCacheName("aclCache");
//        return ehCacheFactoryBean;
//    }

    @Bean(name = { "defaultAclCache", "aclCache" })
    protected AclCache defaultAclCache(org.springframework.cache.CacheManager springCacheManager) {
        org.springframework.cache.Cache cache =
                springCacheManager.getCache("acl_cache");
        return new SpringCacheBasedAclCache(cache,
                permissionGrantingStrategy(),
                aclAuthorizationStrategy());
    }

//    // Depending on your configuration, you may not even need this
//    @Bean
//    public JCacheCacheManager springCacheManager(javax.cache.CacheManager cacheManager) {
//        return new JCacheCacheManager(cacheManager);
//    }

    @Bean
    public JCacheCacheManager aclCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager();
        return new JCacheCacheManager(cacheManager);
    }

//    @Bean
//    public EhCacheManagerFactoryBean aclCacheManager() {
//        return new EhCacheManagerFactoryBean();
//    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new AclAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public static MethodSecurityExpressionHandler myMethodSecurityExpressionHandler(AclService service,RoleHierarchy roleHierarchy) {
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
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(service);

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setRoleHierarchy(roleHierarchy);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(service));
        return expressionHandler;
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
    public LookupStrategy lookupStrategy(AclCache  aclCache,DataSource ds) {
        return new BasicLookupStrategy(ds,aclCache, aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService( LookupStrategy lookupStrategy,AclCache  aclCache) {
        JdbcMutableAclService j=  new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        j.setClassIdentityQuery( "SELECT LAST_INSERT_ID() ");
        j.setSidIdentityQuery("SELECT LAST_INSERT_ID() ");
        return j;
    }




}

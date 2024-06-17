package com.despani.core;

import com.despani.core.config.AclMethodSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
//        (exclude = { RedisReactiveAutoConfiguration.class,RedisAutoConfiguration.class,
//        JooqAutoConfiguration.class, OAuth2AutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class
//})
//@EnableTransactionManagement
public class ColibriBootApplication {


    public static void main(String[] args) {
        SpringApplication.run(new Class[] {ColibriBootApplication.class, AclMethodSecurityConfiguration.class}, args);
    }


}

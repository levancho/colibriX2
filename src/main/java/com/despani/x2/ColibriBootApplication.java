package com.despani.x2;

import com.despani.x2.core.config.AclMethodSecurityConfiguration;
import org.springframework.boot.SpringApplication;

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

package com.despani.x2;

import com.despani.x2.core.config.AclMethodSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
        (exclude = {
        JooqAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableTransactionManagement

public class ColibriBootApplication {


    public static void main(String[] args) {
        SpringApplication.run(new Class[] {ColibriBootApplication.class, AclMethodSecurityConfiguration.class}, args);
    }


}

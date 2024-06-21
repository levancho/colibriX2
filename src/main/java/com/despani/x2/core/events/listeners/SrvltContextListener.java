package com.despani.x2.core.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class SrvltContextListener implements ServletContextListener {

    @Autowired
    private ServletContext servletContext;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        String path = sc.getInitParameter("path");
        String mode = sc.getInitParameter("mode");
        sc.setAttribute("filePath", path);
        sc.setAttribute("fileMode", mode);
        System.out.println("Value saved in context.");
//        WebApplicationContext  l = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//        System.out.println(l.getBeanDefinitionNames());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

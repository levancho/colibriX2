package com.despani.core.controllers.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long startTime = System.currentTimeMillis();
       log.debug("\n-------- LogInterception.preHandle --- ");
       log.debug("Request URL: " + request.getRequestURL());
       log.debug("Start Time: " + System.currentTimeMillis());

        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
       log.debug("\n-------- LogInterception.postHandle --- ");
       log.debug("Request URL: " + request.getRequestURL());

        // You can add attributes in the modelAndView
        // and use that in the view page
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
       log.debug("\n-------- LogInterception.afterCompletion --- ");

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
       log.debug("Request URL: " + request.getRequestURL());
       log.debug("End Time: " + endTime);

       log.debug("Time Taken: " + (endTime - startTime));
    }
}

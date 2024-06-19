package com.despani.core.controllers.interceptors;

import com.despani.core.utils.DespGlobals;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MaintenanceInterceptor implements AsyncHandlerInterceptor {


    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {

        boolean siteOffline = DespGlobals.getPropertyAsBoolean("site.2.offline");

        if (siteOffline) {
            //maintenance time, send to maintenance page
            response.sendRedirect(request.getContextPath()+"/offline");
            return false;
        } else {
            return true;
        }

    }
}

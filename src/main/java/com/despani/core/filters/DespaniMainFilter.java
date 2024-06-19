package com.despani.core.filters;

import com.despani.core.utils.OXUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class DespaniMainFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = OXUtils.getURL(req);
        String host = OXUtils.getHostAndPort(req);
        String  cx =            req.getContextPath();
//        String ctx = OXUtils.getContextPath(req);
        req.setAttribute("urlx", url);
        req.setAttribute("hostx", host);
        req.setAttribute("cx", cx);
        req.setAttribute("currentUrl",req.getServletPath());
        log.info(
                "app: ",
                url);
        chain.doFilter(request, response);
        log.info("Committing a transaction for req : {}", req.getRequestURI());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
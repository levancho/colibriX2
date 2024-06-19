package com.despani.core.controllers.interceptors;

import com.despani.core.annotations.Crumb;
import com.despani.core.beans.BreadCrumbLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


@Slf4j
public class BreadCrumbInterceptor implements HandlerInterceptor {

    private static final String BREAD_CRUMB_LINKS = "breadCrumb";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Annotation[] declaredAnnotations = getDeclaredAnnotationsForHandler(handler);
        HttpSession session = request.getSession();
        emptyCurrentBreadCrumb(session);
        for (Annotation annotation : declaredAnnotations) {
            if(annotation.annotationType().equals(Crumb.class)){
                processAnnotation(request, session, annotation);
            }
        }

        return true;
    }


    private void emptyCurrentBreadCrumb(HttpSession session) {
        session.setAttribute("currentBreadCrumb", new LinkedList<>());
    }


    private void processAnnotation(HttpServletRequest request, HttpSession session, Annotation annotation) {
        Crumb Crumb = (Crumb) annotation;
        String family = Crumb.family();

        Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb = getBreadCrumbLinksFromSession(session);

        if(breadCrumb == null){
            breadCrumb = new HashMap<>();
            session.setAttribute(BREAD_CRUMB_LINKS, breadCrumb);
        }

        LinkedHashMap<String, BreadCrumbLink> familyMap = breadCrumb.get(family);


        if(familyMap == null){
            familyMap = new LinkedHashMap<String, BreadCrumbLink>();
            breadCrumb.put(family, familyMap);
        }

        BreadCrumbLink breadCrumbLink = null;
        breadCrumbLink = getBreadCrumbLink(request, Crumb, familyMap);
        LinkedList<BreadCrumbLink> currentBreadCrumb = new LinkedList<BreadCrumbLink>();
        generateBreadCrumbsRecursively(breadCrumbLink,currentBreadCrumb);
        session.setAttribute("currentBreadCrumb", currentBreadCrumb);
    }


    private BreadCrumbLink getBreadCrumbLink(HttpServletRequest request, Crumb crumb,
                                             LinkedHashMap<String, BreadCrumbLink> familyMap) {
        BreadCrumbLink breadCrumbLink;
        BreadCrumbLink breadCrumbObject = familyMap.get(crumb.label());
        resetBreadCrumbs(familyMap);
        if(breadCrumbObject != null){
            breadCrumbObject.setCurrentPage(true);
            breadCrumbLink = breadCrumbObject;
        }else{
            breadCrumbLink = new BreadCrumbLink(crumb.family(), crumb.label(),crumb.xdesc(), true, crumb.parent());
            String fullURL = request.getRequestURL().append((request.getQueryString()==null)?"":"?"+request.getQueryString()).toString();
            breadCrumbLink.setUrl(fullURL);
            createRelationships(familyMap, breadCrumbLink);
            familyMap.put(crumb.label(), breadCrumbLink);
        }
        return breadCrumbLink;
    }


    @SuppressWarnings("unchecked")
    private Map<String, LinkedHashMap<String, BreadCrumbLink>> getBreadCrumbLinksFromSession(HttpSession session) {
        Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb = (Map<String, LinkedHashMap<String, BreadCrumbLink>>)session.getAttribute(BREAD_CRUMB_LINKS);
        return breadCrumb;
    }

    private Annotation[] getDeclaredAnnotationsForHandler(Object handler) {

        if(handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            return declaredAnnotations;
        }else {
            return new Annotation[0];
        }
    }

    private void resetBreadCrumbs(LinkedHashMap<String, BreadCrumbLink> familyMap) {
        for(BreadCrumbLink breadCrumbLink : familyMap.values()){
            breadCrumbLink.setCurrentPage(false);
        }
    }

    private void generateBreadCrumbsRecursively(BreadCrumbLink Crumb , LinkedList<BreadCrumbLink> breadCrumbLinks){
        if(Crumb.getPrevious() != null){
            generateBreadCrumbsRecursively(Crumb.getPrevious(), breadCrumbLinks);
        }
        breadCrumbLinks.add(Crumb);
    }


    private void createRelationships(LinkedHashMap<String, BreadCrumbLink> familyMap , BreadCrumbLink newLink){
        Collection<BreadCrumbLink> values = familyMap.values();
        for (BreadCrumbLink breadCrumbLink : values) {
            if(breadCrumbLink.getLabel().equalsIgnoreCase(newLink.getParentKey())){
                breadCrumbLink.addNext(newLink);
                newLink.setPrevious(breadCrumbLink);
                newLink.setParent(breadCrumbLink);
            }
        }

    }

}

package com.ays.auth.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liquibase.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.*;

@Component
public class UnMappedEndPointFilter extends OncePerRequestFilter {

   private final RequestMappingHandlerMapping requestMappingHandlerMapping;
   private HashMap<String, List<String>> endpoints;
    public UnMappedEndPointFilter(RequestMappingHandlerMapping requestMappingHandlerMapping ) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    private void init() {
         endpoints = new HashMap<>();
        endpoints.put("PUT",new ArrayList<>());
        endpoints.put("POST",new ArrayList<>());
        endpoints.put("GET",new ArrayList<>());
        endpoints.put("DELETE",new ArrayList<>());
        endpoints.put("OPTIONS",new ArrayList<>());
        endpoints.put("PATCH",new ArrayList<>());
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        map.forEach((k,v) ->{
            String prefix = k.toString().split(" ")[1];
            prefix = prefix.substring(1);
            prefix = prefix.replaceAll("]","");
            prefix = prefix.replaceAll("}"," ");
            prefix = prefix.replaceAll(" ","" );
            if(prefix.contains("{")) {
                prefix = prefix.concat("}");
            }
            String method = k.toString().split(" ")[0].substring(1);

            if(endpoints.containsKey(method)) endpoints.get(method).add(prefix);
        } );

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(endpoints.containsKey(request.getMethod())){
            List<String> urlList = endpoints.get(request.getMethod());
            Optional<String> ref = urlList.stream().filter(i -> i.contains(request.getServletPath()))
                    .findFirst();

            if(ref.isPresent()){
                filterChain.doFilter(request,response);
                return;
            }
            response.setStatus(404);
            return;
        }
        response.setStatus(405);

    }
}

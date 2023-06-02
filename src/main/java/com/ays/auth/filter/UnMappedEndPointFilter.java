package com.ays.auth.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UnMappedEndPointFilter extends OncePerRequestFilter {

   private final RequestMappingHandlerMapping requestMappingHandlerMapping;
   private HashMap<String,String> endpoints;
    public UnMappedEndPointFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    private void init() {
        endpoints = new HashMap<>();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        map.forEach((k,v) ->{
            String prefix = k.toString().split(" ")[1];
            prefix = prefix.substring(1);
            prefix = prefix.replaceAll("]"," ");
            prefix = prefix.replaceAll("}"," ");
            prefix = prefix.replaceAll(" ","" );
            String method = k.toString().split(" ")[0].substring(1);
            endpoints.put(prefix,method) ;
        } );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

          if(endpoints.containsKey(request.getServletPath())){
            boolean method = endpoints.get(request.getServletPath()).contains(request.getMethod());
              if(!method) {
                  response.setStatus(405);
                  return;
              }
              filterChain.doFilter(request,response);
          }
          response.setStatus(404);
    }
}

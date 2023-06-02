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
import java.util.*;

/**
 * A filter that handles unmapped endpoints by checking if the request URI exists in the registered mappings.
 * If the URI exists but the HTTP method is not allowed, it returns a 405 Method Not Allowed response.
 * If the URI does not exist, it returns a 404 Not Found response.
 */
@Component
public class UnMappedEndPointFilter extends OncePerRequestFilter {


    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private HashMap<String, List<String>> endpoints;

    /**
     * Constructs a new UnMappedEndPointFilter with the specified RequestMappingHandlerMapping.
     *
     * @param requestMappingHandlerMapping the RequestMappingHandlerMapping to use for mapping information
     */
    public UnMappedEndPointFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }


    /**
     * Initializes the filter by populating the endpoints map with registered mappings.
     */
    @PostConstruct
    private void init() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        endpoints = new HashMap<>();
        map.forEach((k, v) -> {
            String prefix = k.toString().split(" ")[1];
            prefix = prefix.substring(1);
            prefix = prefix.replaceAll("]", "");
            prefix = prefix.replaceAll("}", "");
            prefix = prefix.replaceAll("\\{\\w+","");
            prefix = prefix.endsWith("/") ? prefix.substring(0,prefix.length()-1) : prefix;
            String method = k.toString().split(" ")[0].substring(1);

            if (endpoints.containsKey(prefix)) {

                List<String> list = new ArrayList<>();
                list.add(method);
                list.addAll(endpoints.get(prefix));
                endpoints.replace(prefix, list);
                 return;
            }
            if (!method.equals("")) {
                endpoints.put(prefix, List.of(method));
            }
        });
    }

    /**
     * Filters incoming requests by handling unmapped endpoints. It checks if the request URI exists in the registered mappings.
     * If the URI exists but the HTTP method is not allowed, it returns a 405 Method Not Allowed response.
     * If the URI does not exist, it returns a 404 Not Found response.
     *
     * @param request     the current HTTP request
     * @param response    the current HTTP response
     * @param filterChain the filter chain for invoking the next filter or the servlet
     * @throws ServletException if any servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
         if(path.split("(\\w*-\\w*)").length>0) {
             path = path.split("(\\w*-\\w*$)")[0];
         }
         if(path.split("/[0-9]+").length>0) {
             path = path.split("/[0-9]+$")[0];
         }
        path = path.endsWith("/") ? path.substring(0,path.length()-1) : path;
        if (endpoints.containsKey(path)) {
            System.out.println(request.getServletPath());
            List<String> methodList = endpoints.get(path);
            boolean isMethodTrue = false;
            for (String method : methodList) {
                if (method.equals(request.getMethod())) {
                    isMethodTrue = true;
                    break;
                }
            }
            if (isMethodTrue) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(405);
            return;
        }
        response.setStatus(404);

    }
}

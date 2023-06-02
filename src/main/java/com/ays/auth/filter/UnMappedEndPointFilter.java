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

/**
 * A filter that handles unmapped endpoints by checking if the request URI exists in the registered mappings.
 * If the URI exists but the HTTP method is not allowed, it returns a 405 Method Not Allowed response.
 * If the URI does not exist, it returns a 404 Not Found response.
 */
@Component
public class UnMappedEndPointFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private HashMap<String, String> endpoints;

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
        endpoints = new HashMap<>();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        map.forEach((k, v) -> {
            String prefix = k.toString().split(" ")[1];
            prefix = prefix.substring(1);
            prefix = prefix.replaceAll("]", " ");
            prefix = prefix.replaceAll("}", " ");
            prefix = prefix.replaceAll(" ", "");
            String method = k.toString().split(" ")[0].substring(1);
            endpoints.put(prefix, method);
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

        if (endpoints.containsKey(request.getServletPath())) {
            boolean method = endpoints.get(request.getServletPath()).contains(request.getMethod());
            if (!method) {
                response.setStatus(405);
                return;
            }
            filterChain.doFilter(request, response);
        }
        response.setStatus(404);
    }
}

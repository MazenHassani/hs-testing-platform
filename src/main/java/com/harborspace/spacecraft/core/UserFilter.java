package com.harborspace.spacecraft.core;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserFilter implements Filter {

    public static final String USER_ID = "subject";
    public static final String USER_NAME = "username";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getUserPrincipal() != null && req.getUserPrincipal() instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) req.getUserPrincipal();
            String subject = principal.getAccount().getKeycloakSecurityContext().getToken().getSubject();
            String username = principal.getAccount().getKeycloakSecurityContext().getToken().getName();
            request.setAttribute(USER_ID, subject);
            request.setAttribute(USER_NAME, username);
        }
        chain.doFilter(request, response);
    }
}
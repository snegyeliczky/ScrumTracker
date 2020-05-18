package com.codecool.scrumtracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenServices jwtTokenServices;

    JwtTokenFilter(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    // this is called for every request that comes in (unless its filtered out before in the chain)
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenServices.getTokenFromRequest((HttpServletRequest) req);
        if (token != null && jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            // Marks the user as authenticated.
            // If this code does not run, the request will fail for routes that are configured to need authentication
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // process the next filter.
        filterChain.doFilter(req, res);
    }
}

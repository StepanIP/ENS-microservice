package com.example.untitled3.service;


import com.example.untitled3.configuration.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class.getName());

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 FilterChain filterChain)
            throws ServletException, IOException {
        if (!hasAuthorizationBearer(httpServletRequest)) {
            LOGGER.info("Request does not have Authorization header with Bearer token. Proceeding without authentication.");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = getAccessToken(httpServletRequest);
        if (!jwtUtils.validateJwtToken(token)) {
            LOGGER.warn("Invalid or expired token. Proceeding without authentication.");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        setAuthenticationContext(token, httpServletRequest);
        LOGGER.info("User successfully authenticated. Proceeding with the request.");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        String[] jwtSubject = jwtUtils.getSubject(token).split(",");
        return userDetailsService.loadUserByUsername(jwtSubject[0]);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1].trim();
    }
}
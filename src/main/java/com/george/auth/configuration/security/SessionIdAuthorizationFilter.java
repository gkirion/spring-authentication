package com.george.auth.configuration.security;

import com.george.auth.configuration.security.util.SessionIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("sessionIdAuthorizationFilter")
public class SessionIdAuthorizationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(SessionIdAuthorizationFilter.class);

    @Autowired
    private Jedis jedis;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader("Authorization");
        logger.info("Authorization: {}", authHeader);
        logger.info("jedis client: {}", jedis.clientId());

        String authToken = SessionIdUtils.extractAuthToken(authHeader);

        if (authToken != null) {
            logger.info("Kleidarithmos: {}", authToken);
            String username = jedis.get(authToken);
            logger.info("username: {}", username);

            Authentication humanAuthenticationToken = new HumanAuthenticationToken(username);
            humanAuthenticationToken = authenticationProvider.authenticate(humanAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(humanAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}

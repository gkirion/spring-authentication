package com.george.auth.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

@Component
public class SessionIdAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(SessionIdAuthenticationProvider.class);

    @Autowired
    private Jedis jedis;

    @Autowired
    @Qualifier("myRemoteUserDetailsService")
    private UserDetailsService myUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationServiceException("no principal provided");
        }

        String principal = (String) authentication.getPrincipal();
        logger.info("got auth request, principal: {}", principal);
        logger.info("jedis client: {}", jedis.clientId());

        String username = jedis.get(principal);
        logger.info("username: {}", username);

        if (username == null) {
            throw new AuthenticationServiceException("could not find session");
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

}

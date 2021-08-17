package com.george.auth.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HumanAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(HumanAuthenticationProvider.class);

    @Autowired
    @Qualifier("myRemoteUserDetailsService")
    private UserDetailsService myUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null && authentication instanceof HumanAuthenticationToken && authentication.getPrincipal() != null) {
            logger.info("got auth request, principal: {}", authentication.getPrincipal());
            UserDetails userDetails = myUserDetailsService.loadUserByUsername((String)authentication.getPrincipal());
            ((HumanAuthenticationToken) authentication).setCredentials(userDetails.getPassword());
            ((HumanAuthenticationToken) authentication).setAuthorities(new ArrayList<>(userDetails.getAuthorities()));
            return authentication;
        }
        throw new AuthenticationServiceException("could not authenticate user");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

}

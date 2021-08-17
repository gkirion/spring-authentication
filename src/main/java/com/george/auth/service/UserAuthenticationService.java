package com.george.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Service("userService")
public class UserAuthenticationService {

    private Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

    @Autowired
    @Qualifier("myRemoteUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private Jedis jedis;

    public String login(String username, String password) throws Exception {
        try {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.getPassword().equals(password)) {
                throw new Exception("Invalid username and/or password");
            }

            String sessionId = UUID.randomUUID().toString();
            jedis.set(sessionId, username);
            logger.info("jedis client: {}", jedis.clientId());
            return sessionId;

        } catch (UsernameNotFoundException e) {
            throw new Exception("Invalid username and/or password");
        }
    }

}

package com.george.auth.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserAuthenticationService {

    private Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

    @Autowired
    @Qualifier("myRemoteUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private Jedis jedis;

    @Autowired
    private MeterRegistry meterRegistry;

    private Timer timer, timerFindInDB, timerSetSessionId, timerGenerateId;

    @PostConstruct
    private void init() {
        timer = meterRegistry.timer("login", "time", "total");
        timerFindInDB = meterRegistry.timer("login", "time", "find_in_db");
        timerSetSessionId = meterRegistry.timer("login", "time", "set_session_id");
        timerGenerateId = meterRegistry.timer("login", "time", "generate_id");
    }

    public String login(String username, String password) throws Exception {
        try {

            long start = System.currentTimeMillis();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            timerFindInDB.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
            if (!userDetails.getPassword().equals(password)) {
                throw new Exception("Invalid username and/or password");
            }

            long generateIdStart = System.currentTimeMillis();
            String sessionId = UUID.randomUUID().toString();
            timerGenerateId.record(System.currentTimeMillis() - generateIdStart, TimeUnit.MILLISECONDS);
            long setSessionStart = System.currentTimeMillis();
            jedis.set(sessionId, username);
            timerSetSessionId.record(System.currentTimeMillis() - setSessionStart, TimeUnit.MILLISECONDS);
            logger.info("jedis client: {}", jedis.clientId());
            timer.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
            return sessionId;

        } catch (UsernameNotFoundException e) {
            throw new Exception("Invalid username and/or password");
        }
    }

}

package com.george.auth.service;

import com.george.auth.model.MyUserDetails;
import com.george.auth.entity.User;
import com.george.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("got auth request for user with username: {}", s);
        List<User> users =  userRepository.findByUsername(s);
        if (users.isEmpty()) {
            logger.info("no user found with username: {}", s);
            throw new UsernameNotFoundException("no user found with username: " + s);
        }
        User user = users.get(0);
        logger.info("found user: {}", user);
        return new MyUserDetails(user.getUsername(), user.getPassword(), user.getRoles());
    }

}

package com.george.auth.service;

import com.george.auth.model.Human;
import com.george.auth.model.MyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service("myRemoteUserDetailsService")
public class MyRemoteUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(MyRemoteUserDetailsService.class);

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("got auth request for user with username: {}", s);

        try {

            ResponseEntity<Human> responseEntity = restTemplate.exchange(RequestEntity.get(URI.create("http://localhost:8088/api/v1/human/" + s))
                .accept(MediaType.APPLICATION_JSON)
                .build(), Human.class);

            Human human = responseEntity.getBody();
            MyUserDetails myUserDetails = new MyUserDetails(human.getName(), human.getPassword(), human.getRoles());
            return myUserDetails;

        } catch (RestClientException e) {
            throw new UsernameNotFoundException("could not find user " + s);
        }
    }

}

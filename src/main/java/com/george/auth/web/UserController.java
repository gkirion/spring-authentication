package com.george.auth.web;

import com.george.auth.entity.User;
import com.george.auth.service.UserAuthenticationService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-authentication")
public class UserController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @PostMapping("/login")
    public String login(@RequestBody User user) throws Exception {
        return userAuthenticationService.login(user.getUsername(), user.getPassword());
    }

}

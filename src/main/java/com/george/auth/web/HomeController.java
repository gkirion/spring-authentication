package com.george.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "<h1>hello!</h1>";
    }

    @GetMapping("/user")
    public String getUser() {
        return "<h1>hello user!</h1>";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "<h1>hello admin!</h1>";
    }

}

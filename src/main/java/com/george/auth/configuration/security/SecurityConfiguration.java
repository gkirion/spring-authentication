package com.george.auth.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("sessionIdAuthorizationFilter")
    private Filter filter;

    @Autowired
    @Qualifier("myRemoteUserDetailsService")
    private UserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user-authentication").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .anyRequest().permitAll()
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}

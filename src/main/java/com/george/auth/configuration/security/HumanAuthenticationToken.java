package com.george.auth.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class HumanAuthenticationToken implements Authentication {

    private Object credentials;

    private Object details;

    private Object principal;

    private boolean authenticated = true;

    private String name;

    private List<? extends GrantedAuthority> authorities;

    public HumanAuthenticationToken(Object principal) {
        this(principal, null, List.of());
    }

    public HumanAuthenticationToken(Object principal, Object credentials) {
        this(principal, credentials, List.of());
    }

    public HumanAuthenticationToken(Object principal, Object credentials, List<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthorities(List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}

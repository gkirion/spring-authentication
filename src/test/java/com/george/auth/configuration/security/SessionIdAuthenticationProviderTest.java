package com.george.auth.configuration.security;

import com.george.auth.model.MyUserDetails;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import redis.clients.jedis.Jedis;

@ExtendWith(MockitoExtension.class)
public class SessionIdAuthenticationProviderTest {

    @InjectMocks
    private SessionIdAuthenticationProvider sessionIdAuthenticationProvider;

    @Mock
    private Jedis jedis;

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    public void authenticateNullTokenTest() throws AuthenticationException {
        Assertions.assertThrows(AuthenticationException.class, () -> sessionIdAuthenticationProvider.authenticate(null));
    }

    @Test
    public void authenticateNullPrincipalTest() throws AuthenticationException {
        Assertions.assertThrows(AuthenticationException.class, () -> sessionIdAuthenticationProvider.authenticate(new HumanAuthenticationToken(null)));
    }

    @Test
    public void authenticateSessionNotFoundTest() throws AuthenticationException {
        Mockito.when(jedis.get(Mockito.any(String.class))).thenReturn(null);
        Assertions.assertThrows(AuthenticationException.class, () -> sessionIdAuthenticationProvider.authenticate(new HumanAuthenticationToken("")));
    }

    @Test
    public void authenticateUsernameNotFoundTest() throws AuthenticationException {
        Mockito.when(jedis.get(Mockito.any(String.class))).thenReturn("");
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenThrow(new UsernameNotFoundException("username not found"));
        Assertions.assertThrows(AuthenticationException.class, () -> sessionIdAuthenticationProvider.authenticate(new HumanAuthenticationToken("")));
    }

    @Test
    public void authenticateOKTest() throws AuthenticationException {
        Mockito.when(jedis.get(Mockito.any(String.class))).thenReturn("");
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(new MyUserDetails(""));
        Assertions.assertTrue(Matchers.any(Authentication.class).matches(sessionIdAuthenticationProvider.authenticate(new HumanAuthenticationToken(""))));
    }

}

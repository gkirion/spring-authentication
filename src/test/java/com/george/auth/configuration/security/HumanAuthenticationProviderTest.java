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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class HumanAuthenticationProviderTest {

    @InjectMocks
    private HumanAuthenticationProvider humanAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    public void authenticateNullTokenTest() throws AuthenticationException {
        Assertions.assertThrows(AuthenticationException.class, () -> humanAuthenticationProvider.authenticate(null));
    }

    @Test
    public void authenticateDifferentTokenTypeTest() throws AuthenticationException {
        Assertions.assertThrows(AuthenticationException.class, () -> humanAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("","")));
    }

    @Test
    public void authenticateNullPrincipalTest() throws AuthenticationException {
        Assertions.assertThrows(AuthenticationException.class, () -> humanAuthenticationProvider.authenticate(new HumanAuthenticationToken(null)));
    }

    @Test
    public void authenticateNotFoundTest() throws AuthenticationException {
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenThrow(new UsernameNotFoundException("username not found"));
        Assertions.assertThrows(AuthenticationException.class, () -> humanAuthenticationProvider.authenticate(new HumanAuthenticationToken("")));
    }

    @Test
    public void authenticateUsernameNotFoundTest() throws AuthenticationException {
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenThrow(new UsernameNotFoundException("username not found"));
        Assertions.assertThrows(AuthenticationException.class, () -> humanAuthenticationProvider.authenticate(new HumanAuthenticationToken("")));
    }

    @Test
    public void authenticateOKTest() throws AuthenticationException {
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(new MyUserDetails(""));
        Assertions.assertTrue(Matchers.any(HumanAuthenticationToken.class).matches(humanAuthenticationProvider.authenticate(new HumanAuthenticationToken(""))));
    }

}

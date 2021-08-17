package com.george.auth.configuration.security.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionIdUtilsTest {


    @Test
    public void authHeaderNullTest() {
        Assertions.assertNull(SessionIdUtils.extractAuthToken(null));
    }

    @Test
    public void authHeaderNotStartsWithTest() {
        Assertions.assertNull(SessionIdUtils.extractAuthToken("Kleid"));
    }

    @Test
    public void authHeaderWrongFormatTest() {
        Assertions.assertNull(SessionIdUtils.extractAuthToken("Kleidarithmos dsjkfj409u43jt3"));
    }

    @Test
    public void authHeaderOKTest() {
        Assertions.assertEquals("fhsekfhesf-fhuiefihf-fjkdsh454jkfghd-dhfsfheufhes", SessionIdUtils.extractAuthToken("Kleidarithmos: fhsekfhesf-fhuiefihf-fjkdsh454jkfghd-dhfsfheufhes"));
    }

}

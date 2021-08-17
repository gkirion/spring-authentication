package com.george.auth.configuration.security.util;

public class SessionIdUtils {

    public static String extractAuthToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Kleidarithmos")) {
            return null;
        }
        String[] tokens = authHeader.split(":");
        if (tokens.length != 2) {
            return null;
        }
        return tokens[1].trim();
    }

}

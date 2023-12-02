package com.trello.trello.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.stereotype.Service;

@Service
public class GoogleTokenVerifier {
    private static final String CLIENT_ID = "671355781221-4r8cs2rkrn2q5lkkgqhrbm0r0npl6iqe.apps.googleusercontent.com";

    public boolean isValidToken(String token) {
        boolean isValid = tokenValid(token);
        return isValid;
    }

    private static boolean tokenValid(String idToken) {
        NetHttpTransport transport = new NetHttpTransport();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            return (googleIdToken != null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

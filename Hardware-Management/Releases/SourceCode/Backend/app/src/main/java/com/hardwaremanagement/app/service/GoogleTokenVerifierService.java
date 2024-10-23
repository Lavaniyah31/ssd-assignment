package com.hardwaremanagement.app.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hardwaremanagement.app.model.User;
import com.hardwaremanagement.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleTokenVerifierService {

    private static final String CLIENT_ID = "433839212385-j186dbs3et8793saorkh259109uaf1ob.apps.googleusercontent.com";

    @Autowired
    private UserRepository userRepository;

    public Optional<User> verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            // Verify the ID token
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get user information from the payload
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Check if the user already exists in your database
                Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByEmail(email));

                if (userOptional.isPresent()) {
                    // User already exists, return it
                    return userOptional;
                } else {
                    // Create a new user if they don't exist
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPassword(""); // No password required for Google users

                    // Save the new user
                    userRepository.save(newUser);

                    return Optional.of(newUser);
                }
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

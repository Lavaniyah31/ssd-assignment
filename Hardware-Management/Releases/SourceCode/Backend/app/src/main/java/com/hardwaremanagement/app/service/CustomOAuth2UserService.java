//package com.hardwaremanagement.app.service;
//
//import com.hardwaremanagement.app.model.User;
//import com.hardwaremanagement.app.repository.UserRepository;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        // Extract email and name from attributes
//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//
//        // Check if user already exists in the database
//        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByEmail(email));
//        User user;
//
//        if (userOptional.isPresent()) {
//            // Update user details if necessary
//            user = userOptional.get();
//            user.setName(name);
//        } else {
//            // Create a new user if not already present
//            user = new User();
//            user.setEmail(email);
//            user.setName(name);
//            user.setPassword("");  // No password required for Google users
//            userRepository.save(user);
//        }
//
//        // Set the user's role
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
//
//        return new DefaultOAuth2User(
//                Collections.singleton(authority),  // Authorities/roles
//                attributes,  // User attributes from Google
//                "email"  // Use email as the principal identifier
//        );
//    }
//}

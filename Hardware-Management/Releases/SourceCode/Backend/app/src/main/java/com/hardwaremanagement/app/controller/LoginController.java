package com.hardwaremanagement.app.controller;


import com.hardwaremanagement.app.dao.LoginDao;
import com.hardwaremanagement.app.model.User;
import com.hardwaremanagement.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/auth")
    public ResponseEntity<?> getUserDetails(@RequestBody LoginDao loginDao) {

        User user = userRepository.findUserByEmail(loginDao.getUsername());

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Compare the entered password with the hashed password in the database
        if (passwordEncoder.matches(loginDao.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}

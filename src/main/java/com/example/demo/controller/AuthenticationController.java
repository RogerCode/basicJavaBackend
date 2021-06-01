package com.example.demo.controller;

import com.example.demo.pojo.authentication.LoginRequest;
import com.example.demo.pojo.authentication.RegisterRequest;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }
}

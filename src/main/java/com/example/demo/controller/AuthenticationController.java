package com.example.demo.controller;

import com.example.demo.pojo.authentication.LoginRequest;
import com.example.demo.pojo.authentication.RegisterRequest;
import com.example.demo.pojo.authentication.ValidateTokenRequest;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        return authenticationService.register(registerRequest, request);
    }

    @GetMapping("validate-token")
    public ResponseEntity validateToken() {
        return ResponseEntity.ok("Correct Token");
    }

    @GetMapping("register-activation")
    public RedirectView registerConfirm(HttpServletRequest request, HttpServletResponse response, @RequestParam("token") String token) {
        return authenticationService.registerConfirm(token, request, response);
    }


}

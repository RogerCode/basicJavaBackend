package com.example.demo.controller;

import com.example.demo.entity.UserRepository;
import com.example.demo.pojo.user.RemoveImageRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("add-image")
    public ResponseEntity addImage(@RequestParam("file") MultipartFile file) throws IOException {
        return userService.addUserImage(file);
    }

    @PostMapping("remove-image")
    public ResponseEntity removeImage(@RequestBody RemoveImageRequest request) throws IOException {
        return userService.removeUserImage(request);
    }
}

package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRepository;
import com.example.demo.pojo.user.RemoveImageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserService {

    @Value("${application.filesystem.path}")
    private String fileSystemPath;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity addUserImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String filePath = fileSystemPath + UUID.randomUUID() + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, bytes);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = (User) securityContext.getAuthentication().getPrincipal();
        user.getImages().add(filePath);
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public ResponseEntity removeUserImage(RemoveImageRequest request) throws IOException {
        String filePath = fileSystemPath + request.getImageName();
        Path path = Paths.get(filePath);
        path.toFile().delete();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = (User) securityContext.getAuthentication().getPrincipal();
        user.getImages().remove(request.getImageName());
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}

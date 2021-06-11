package com.example.demo.controller;

import com.example.demo.entity.UserRepository;
import com.example.demo.pojo.user.RemoveImageRequest;
import com.example.demo.service.UserService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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

    @GetMapping(value = "image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getImage() throws IOException {
        File file = new File("src/main/resources/static/fc652a73-d2b7-433a-b625-fe9a41a15e4adescarga.jpg");
        return  ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename" +file.getName())
                .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
                .body(Files.readAllBytes(file.toPath()));
    }

}

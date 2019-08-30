package com.codecool.zsuzsi.puzzlesbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@CrossOrigin
public class UploadController {

    @Value("${IMAGE_PATH}")
    private String imagePath;

    @PostMapping("/upload")
    public void saveImage(@RequestPart("file") MultipartFile file) {
        try {
            Path path = Paths.get(imagePath);
            Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/image-resource/{image}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FileSystemResource getFile(@PathVariable("image") String fileName) {
        return new FileSystemResource(imagePath + fileName);
    }
}

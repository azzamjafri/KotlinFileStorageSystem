//package com.example.bucket.bucketservice;
//
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class randon {
//
//    Path path = Paths.get("/path/to/the/file.txt");
//    String name = "file.txt";
//    String originalFileName = "file.txt";
//    String contentType = "text/plain";
//    byte[] content = null;
//    try {
//        content = Files.readAllBytes(path);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//    MultipartFile result = new MockMultipartFile(name,
//            originalFileName, contentType, content);
//}

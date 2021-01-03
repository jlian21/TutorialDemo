package com.backend.filetransfer.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.filetransfer.model.FileDB;
import com.backend.filetransfer.service.FileService;
import com.backend.filetransfer.message.ResponseFile;
import com.backend.filetransfer.message.ResponseMessage;;

@RestController
public class FileController {
    @Autowired
    FileService service;
    
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam(required = false) String title,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        String message = "";
        try {
            String id = service.addFile(title, file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDB>> getAllFiles() throws IllegalStateException, IOException {
    	System.out.println("enter get all files");
        List<FileDB> files = service.getAllFiles();
        //List<FileDB> responses= new ArrayList<FileDB>();
        for (FileDB file : files) {
            file.setUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
                    .path(file.getId())
                    .toUriString());
            System.out.println(file.getUrl());
            System.out.println(file.getId());
            
        }
        System.out.println("finish get all files");
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable String id) throws Exception {
        FileDB file = service.getFile(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = file.getStream();
        int data = in.read();
        while (data >= 0) {
            out.write((char) data);
            data = in.read();
        }
        out.flush();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getTitle() + "\"")
                .body(out.toByteArray());
    }
}

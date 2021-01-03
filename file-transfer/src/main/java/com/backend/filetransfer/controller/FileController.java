package com.backend.filetransfer.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

@RestController
public class FileController {
    @Autowired
    FileService service;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        String id = service.addFile(title, file);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDB>> getAllFiles() throws IllegalStateException, IOException {
        List<FileDB> files = service.getAllFiles();
        for (FileDB file : files) {
            file.setUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
                    .path(file.getId())
                    .toUriString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable String id, Model model) throws Exception {
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

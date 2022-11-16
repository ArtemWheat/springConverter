package com.example.springConverter.controller;

import com.example.springConverter.ResponseData;
import com.example.springConverter.entity.Converter;
import com.example.springConverter.entity.FileData;
import com.example.springConverter.service.ConverterService;
import com.example.springConverter.service.FileDataService;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Array;

@RestController
public class AttachmentController {

    @Autowired
    private FileDataService fileDataService;

    @Autowired
    private ConverterService converterService;

    public AttachmentController(FileDataService fileDataService,
                                ConverterService converterService) {
        this.fileDataService = fileDataService;
        this.converterService = converterService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileData> uploadFileStorage(@RequestParam("file")MultipartFile file) throws Exception {
        FileData fileData = fileDataService.saveAttachmentStorage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileData);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<FileData> downloadFileStorage(@PathVariable String fileId) throws Exception {
        FileData fileData = fileDataService.getAttachmentStorage(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileData);
    }

    @PostMapping("/cmd")
    public ResponseEntity<String> command(@RequestParam String cmd) throws Exception {
        if (!converterService.executeCommand(cmd)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Выполнена команда " + cmd);
        }
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Конвертация проведена успешно");
    }

    @GetMapping("/source")
    public ResponseEntity<String[][]> get_mini_source() throws Exception{
        return ResponseEntity.status(HttpStatus.OK)
                .body(converterService.outputSource());
    }

    @GetMapping("/sample")
    public ResponseEntity<String[][]> get_mini_sample() throws Exception{
        return ResponseEntity.status(HttpStatus.OK)
                .body(converterService.outputSample());
    }
}

package com.example.springConverter.controller;

import com.example.springConverter.entity.FileData;
import com.example.springConverter.service.ConverterService;
import com.example.springConverter.service.FileDataService;
import org.apache.poi.EmptyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;


@Controller
public class AttachmentController {
    @Autowired
    private final FileDataService fileDataService;
    @Autowired
    private final ConverterService converterService;

    public AttachmentController(FileDataService fileDataService,
                                ConverterService converterService) {
        this.fileDataService = fileDataService;
        this.converterService = converterService;
    }

    @PostMapping("/upload_source")
    public ResponseEntity<FileData> uploadFileSource(@RequestParam("file")MultipartFile file) throws Exception {
        var fileData = fileDataService.saveAttachmentStorage(file);
        try {
            converterService.setSourcePath(fileData.getFilePath());
            return ResponseEntity.status(HttpStatus.OK).body(fileData);
        } catch (EmptyFileException exception) {
            System.out.println("File to boot is not selected");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fileData);
        }
    }

    @PostMapping("/upload_sample")
    public ResponseEntity<FileData> uploadFileSample(@RequestParam("file")MultipartFile file) throws Exception {
        var fileData = fileDataService.saveAttachmentStorage(file);
        try {
            converterService.setSamplePath(fileData.getFilePath());
            return ResponseEntity.status(HttpStatus.OK).body(fileData);
        } catch (EmptyFileException exception) {
            System.out.println("File to boot is not selected");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fileData);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFileStorage() throws Exception {
        converterService.executeCommand("convert");
        InputStreamResource file = fileDataService.getFileFromFileSystem(converterService.getSamplePath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    @PostMapping("/cmd")
    public ResponseEntity<String> command(@RequestParam String cmd) throws Exception {
        if (!converterService.executeCommand(cmd)){
            return ResponseEntity.status(HttpStatus.OK).body("Выполнена команда " + cmd);
        }
        else
            return ResponseEntity.status(HttpStatus.OK).body("Конвертация проведена успешно");
    }

    @GetMapping("/source")
    public ResponseEntity<String[][]> get_mini_source() throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(converterService.outputSource());
    }

    @GetMapping("/sample")
    public ResponseEntity<String[][]> get_mini_sample() throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(converterService.outputSample());
    }

    @GetMapping("/")
    public String mainPage() throws Exception{
        return "page";
    }
}
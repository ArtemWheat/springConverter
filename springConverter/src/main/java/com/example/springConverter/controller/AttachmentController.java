package com.example.springConverter.controller;

import com.example.springConverter.ResponseData;
import com.example.springConverter.entity.Converter;
import com.example.springConverter.entity.Attachment;
import com.example.springConverter.entity.FileData;
import com.example.springConverter.service.AttachmentService;
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
    private AttachmentService attachmentService;

    @Autowired
    private FileDataService fileDataService;

    @Autowired
    private ConverterService converterService;

    public AttachmentController(AttachmentService attachmentService,
                                FileDataService fileDataService,
                                ConverterService converterService) {
        this.attachmentService = attachmentService;
        this.fileDataService = fileDataService;
        this.converterService = converterService;
    }


    @PostMapping("/upload") //TODO надо убрать загрзку в БД
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment = null;
        String downloadURl = "";
        attachment = attachmentService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @PostMapping("/storage/upload")
    public ResponseEntity<FileData> uploadFileStorage(@RequestParam("file")MultipartFile file) throws Exception{

        FileData fileData = fileDataService.saveAttachmentStorage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileData);
    }

    @GetMapping("/storage/download/{fileId}")
    public ResponseEntity<FileData> downloadFileStorage(@PathVariable String fileId) throws Exception{
        FileData fileData = fileDataService.getAttachmentStorage(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileData);
    }

    @PostMapping("storage/command")
    public ResponseEntity<String> command(@RequestParam String command) throws Exception{
        if (!converterService.executeCommand(command)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Выполнена команда " + command);
        }
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Конвертация проведена успешно");
    }

    @GetMapping("storage/source")
    public ResponseEntity<String[][]> get_mini_source() throws Exception{
        return ResponseEntity.status(HttpStatus.OK)
                .body(converterService.outputSource());
    }

    @GetMapping("storage/sample")
    public ResponseEntity<String[][]> get_mini_sample() throws Exception{
        return ResponseEntity.status(HttpStatus.OK)
                .body(converterService.outputSample());
    }
}

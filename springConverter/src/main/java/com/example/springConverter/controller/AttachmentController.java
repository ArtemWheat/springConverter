package com.example.springConverter.controller;

import com.example.springConverter.ResponseData;
import com.example.springConverter.entity.Converter;
import com.example.springConverter.entity.Attachment;
import com.example.springConverter.entity.FileData;
import com.example.springConverter.service.AttachmentService;
import com.example.springConverter.service.FileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileDataService fileDataService;

    public AttachmentController(AttachmentService attachmentService, FileDataService fileDataService) {
        this.attachmentService = attachmentService;
        this.fileDataService = fileDataService;
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

    @PostMapping("/storage/upload") //TODO сделать 2 вида загрузки
    public ResponseEntity<FileData> uploadFileStorage(@RequestParam("file")MultipartFile file) throws Exception{

        FileData fileData = fileDataService.saveAttachmentStorage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileData);
    }

    @GetMapping("/storage/download/{fileId}")
    public ResponseEntity<Resource> downloadFileStorage(@PathVariable String fileId) throws Exception{
       return null;
    }

    @PostMapping("/storage/combiner")
    public ResponseEntity<Converter> convert(@RequestParam int[] columns,
                                             @RequestParam String splitter,
                                             @RequestParam int id) throws Exception{ //TODO настроить систему id процессов
        return null;
    }

    //TODO закинуть проект ильи сюда
    //TODO прописать логику id
    //TODO прописать запросы в проекту ильи
}

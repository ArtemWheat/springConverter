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

    @PostMapping("storage/combine")
    public ResponseEntity<String> combine(@RequestParam String fileId1,
                                            @RequestParam String fileId2,
                                            @RequestParam String columnsNums,
                                            @RequestParam String splitter) throws Exception{
        var columnsNumsArr = columnsNums.split(", ");
        var columnsNumsArrInt = new int[columnsNumsArr.length];
        for (var i = 0; i < columnsNumsArr.length; i++){
            columnsNumsArrInt[i] = Integer.parseInt(columnsNumsArr[i]);
        }

        String fileResulthPath = converterService.combine(fileId1,
                fileId2,
                columnsNumsArrInt,
                splitter);

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileResulthPath); //TODO разобраться, почему не меняется ничего в таблицах
    }

    @PostMapping("storage/divide")
    public ResponseEntity<String> divide(@RequestParam String fileId1,
                                         @RequestParam String fileId2,
                                         @RequestParam String columnNum,
                                         @RequestParam String splitter) throws Exception{

        String fileResulthPath = converterService.divide(fileId1,
                fileId2,
                Integer.parseInt(columnNum),
                splitter);

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileResulthPath);
    }

    @PostMapping("storage/connect")
    public ResponseEntity<String> connect(@RequestParam String fileId1,
                                         @RequestParam String fileId2,
                                         @RequestParam String srcColumn,
                                         @RequestParam String dstColumn) throws Exception{

        String fileResulthPath = converterService.connect(fileId1,
                fileId2,
                Integer.parseInt(srcColumn),
                Integer.parseInt(dstColumn));

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileResulthPath);
    }
}

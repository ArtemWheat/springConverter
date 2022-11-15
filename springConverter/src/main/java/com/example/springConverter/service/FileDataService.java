package com.example.springConverter.service;

import com.example.springConverter.entity.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataService {
    FileData saveAttachmentStorage(MultipartFile file) throws Exception;

    FileData getAttachmentStorage(String fileId) throws Exception;
}

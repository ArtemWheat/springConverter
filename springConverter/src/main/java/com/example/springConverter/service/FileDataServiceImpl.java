package com.example.springConverter.service;

import com.example.springConverter.entity.FileData;
import com.example.springConverter.repository.FileDataStorageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static com.example.springConverter.util.KeyGeneratorUtil.generateKey;

@Service
@RequiredArgsConstructor
public class FileDataServiceImpl implements FileDataService {
    private static final String FOLDER_PATH = "C:\\Users\\lenovo\\OneDrive - УрФУ\\Рабочий стол\\springConverter\\springConverter\\src\\main\\resources\\UploadedFiles\\";
    @Autowired
    private FileDataStorageRepository fileDataRepository;

    public FileDataServiceImpl(FileDataStorageRepository fileDataRepository){
        this.fileDataRepository = fileDataRepository;
    }

    @Override
    public FileData saveAttachmentStorage(MultipartFile file) throws Exception {
        try {
            var fileName = StringUtils.cleanPath(file.getOriginalFilename());
            var filePath = FOLDER_PATH + generateKey(fileName) + ".xlsx"; //TODO сделать для 2-го типа еще
            var fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .filePath(filePath).build());
            file.transferTo(new File(filePath));
            return fileData;
        } catch (Exception e) {
            throw new Exception("Could not save File: " + file.getOriginalFilename());
        }
    }

    @Override
    public FileData getAttachmentStorage(String fileId) throws Exception {
        return fileDataRepository
                .findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
    }

    @Override
    public InputStreamResource getFileFromFileSystem(String filePath) throws Exception {
        return new InputStreamResource(new FileInputStream(new File(filePath)));
    }
}

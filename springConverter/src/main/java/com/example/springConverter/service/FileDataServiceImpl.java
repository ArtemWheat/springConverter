package com.example.springConverter.service;

import com.example.springConverter.entity.FileData;
import com.example.springConverter.repository.FileDataStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileDataServiceImpl implements FileDataService {

    private static final String FOLDER_PATH = "C:/Users/lenovo/Desktop/springConverter/springConverter/src/main/resources/UploadedFiles/";

    @Autowired
    private FileDataStorageRepository fileDataRepository;

    public FileDataServiceImpl(FileDataStorageRepository fileDataRepository){
        this.fileDataRepository = fileDataRepository;
    }

    @Override
    public FileData saveAttachmentStorage(MultipartFile file) throws Exception {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        try{
        FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));
            return fileData;
        } catch (Exception e){
            throw new Exception("Could not save File: " + file.getOriginalFilename());
        }
    }

    @Override
    public FileData getAttachmentStorage(String fileId) throws Exception {
        return fileDataRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }
}

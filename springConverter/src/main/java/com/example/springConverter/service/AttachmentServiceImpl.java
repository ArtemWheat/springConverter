package com.example.springConverter.service;

import com.example.springConverter.entity.Attachment;
import com.example.springConverter.repository.AttachmentRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.example.springConverter.util.KeyGeneratorUtil.generateKey;

@Service
public class AttachmentServiceImpl implements AttachmentService{

    @Autowired
    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
       var fileName = StringUtils.cleanPath(file.getOriginalFilename());
       try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }
            var attachment = new Attachment(
                    fileName,
                    file.getContentType(),
                    generateKey(fileName),
                    file.getBytes());
            return attachmentRepository.save(attachment);

       } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
       }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
    }
}

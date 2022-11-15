package com.example.springConverter.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private String fileType;
    private String fileKey;
    @Lob
    private byte[] data;

    public Attachment(String fileName, String fileType, String fileKey, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.fileKey = fileKey;
    }
}

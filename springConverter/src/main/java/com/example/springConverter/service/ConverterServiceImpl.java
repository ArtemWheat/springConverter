package com.example.springConverter.service;

import com.example.springConverter.excel.converter.ExcelConverter;
import com.example.springConverter.repository.FileDataStorageRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ConverterServiceImpl implements ConverterService{

    @Autowired
    private FileDataService fileDataService;

    public ConverterServiceImpl(FileDataStorageRepository fileDataRepository){
        this.fileDataService = fileDataService;
    }

    @Override
    public String combine(String fileId1, String fileId2, int[] columnsNum, String splitter) throws Exception {
        final String sourcePath = fileDataService.getAttachmentStorage(fileId1).getFilePath();
        final String samplePath = fileDataService.getAttachmentStorage(fileId2).getFilePath();
        final String resultPath = "springConverter/src/main/resources/UploadedFiles/result.xlsx";

        try (
                var source = new FileInputStream(sourcePath);
                var sample = new FileInputStream(samplePath)
        ) {
            var sourceWB = new XSSFWorkbook(source);
            var sampleWB = new XSSFWorkbook(sample);
            var converter = new ExcelConverter(sourceWB, sampleWB);

            converter.combineColumns(columnsNum, splitter);

            var result = converter.getFinalResult();
            try (var outputStream = new FileOutputStream(resultPath)) {
                result.write(outputStream);
            }
            return resultPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String divide(String fileId1, String fileId2, int columnNum, String splitter) throws Exception {
        final String sourcePath = fileDataService.getAttachmentStorage(fileId1).getFilePath();
        final String samplePath = fileDataService.getAttachmentStorage(fileId2).getFilePath();
        final String resultPath = "springConverter/src/main/resources/UploadedFiles/result.xlsx";

        try (
                var source = new FileInputStream(sourcePath);
                var sample = new FileInputStream(samplePath)
        ) {
            var sourceWB = new XSSFWorkbook(source);
            var sampleWB = new XSSFWorkbook(sample);
            var converter = new ExcelConverter(sourceWB, sampleWB);

            converter.divideColumns(columnNum, splitter);

            var result = converter.getFinalResult();
            try (var outputStream = new FileOutputStream(resultPath)) {
                result.write(outputStream);
            }
            return resultPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String connect(String fileId1, String fileId2, int srcColumnNum, int dstColumnNum) throws Exception {
        final String sourcePath = fileDataService.getAttachmentStorage(fileId1).getFilePath();
        final String samplePath = fileDataService.getAttachmentStorage(fileId2).getFilePath();
        final String resultPath = "springConverter/src/main/resources/UploadedFiles/result.xlsx";

        try (
                var source = new FileInputStream(sourcePath);
                var sample = new FileInputStream(samplePath)
        ) {
            var sourceWB = new XSSFWorkbook(source);
            var sampleWB = new XSSFWorkbook(sample);
            var converter = new ExcelConverter(sourceWB, sampleWB);

            converter.connectColumns(srcColumnNum, dstColumnNum);

            var result = converter.getFinalResult();
            try (var outputStream = new FileOutputStream(resultPath)) {
                result.write(outputStream);
            }
            return resultPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

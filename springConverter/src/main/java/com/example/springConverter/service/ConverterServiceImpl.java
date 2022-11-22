package com.example.springConverter.service;

import com.example.springConverter.excel.converter.ExcelConverter;
import com.example.springConverter.excel.util.ExcelHelper;
import com.example.springConverter.repository.FileDataStorageRepository;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

@Service
public class ConverterServiceImpl implements ConverterService{
    private String sourcePath;
    private String samplePath;
    @Autowired
    private FileDataService fileDataService;
    private final ExcelConverter converter;


    public ConverterServiceImpl(FileDataStorageRepository fileDataRepository) throws IOException {
        this.fileDataService = fileDataService;
        this.sourcePath = "springConverter/src/main/resources/UploadedFiles/source.xlsx";
        this.samplePath = "springConverter/src/main/resources/UploadedFiles/sample.xlsx";
        try {
            FileInputStream source = new FileInputStream(sourcePath);
            FileInputStream sample = new FileInputStream(samplePath);
            XSSFWorkbook sourceWB = new XSSFWorkbook(source);
            XSSFWorkbook sampleWB = new XSSFWorkbook(sample);
            this.converter = new ExcelConverter(sourceWB, sampleWB);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean executeCommand(String command) throws IOException {
        var parts = command.split(" ");
        switch (parts[0]) {
            case "combine" -> {
                var splitter = command.split("\"")[1];
                var columnsNums = Arrays.stream(parts)
                        .filter(str -> str.matches("\\d+"))
                        .mapToInt(Integer::parseInt)
                        .map(num -> num - 1)
                        .toArray();
                converter.combineColumns(columnsNums, splitter);
            }
            case "divide" -> {
                var splitter = command.split("\"")[1];
                var columnNum = Integer.parseInt(parts[1]) - 1;
                converter.divideColumns(columnNum, splitter);
            }
            case "connect" -> {
                var sourceNum = Integer.parseInt(parts[1]) - 1;
                var resultNum = Integer.parseInt(parts[2]) - 1;
                converter.connectColumns(sourceNum, resultNum);
            }
            case "cancel" -> {
                converter.cancelLast();
            }
            case "convert" -> {
                var result = converter.getFinalResult();
                try (var outputStream = new FileOutputStream(samplePath)) {
                    result.write(outputStream);
                } catch (IOException e){
                    throw new RuntimeException(e);
                }
                return true;
            }
            default -> {
                throw new RuntimeException();
            }
        }
        return false;
    }

    @Override
    public String[][] outputSource() {
        var source = converter.getSourceExample();
        return ExcelHelper.toStringMatrix(source.getSheetAt(0));
    }

    @Override
    public void setSourcePath(String path) {
        sourcePath = path;
    }

    @Override
    public void setSamplePath(String path) {
        samplePath = path;
    }

    @Override
    public String[][] outputSample() {
        var sample = converter.getResultExample();
        return ExcelHelper.toStringMatrix(sample.getSheetAt(0));
    }

    private static void printTable(String[][] table) {
        for (int i = 0; i < table[0].length; i++)
            System.out.printf("%-30s ", String.format("(%s) %s", i + 1, table[0][i]));
        System.out.println();

        for (int j = 1; j < table.length; j++) {
            var line = table[j];
            for (var cell : line)
                System.out.printf("%-30s ", cell);
            System.out.println();
        }
    }
}

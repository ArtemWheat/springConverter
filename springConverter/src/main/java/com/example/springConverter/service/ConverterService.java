package com.example.springConverter.service;

import com.example.springConverter.excel.converter.ExcelConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.javatuples.Pair;

import java.io.IOException;

public interface ConverterService {
    boolean executeCommand(String command) throws IOException;

    String[][] outputSample();

    String[][] outputSource();

    void setSourcePath(String path);
    void setSamplePath(String path);
    String getSamplePath();
}

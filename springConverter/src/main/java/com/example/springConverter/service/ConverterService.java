package com.example.springConverter.service;

public interface ConverterService {
    String combine(String fileId1, String fileId2, int[] columnsNum, String splitter) throws Exception;

    String divide(String fileId1, String fileId2, int columnNum, String splitter) throws Exception;

    String connect(String fileId1, String fileId2, int srcColumnNum, int dstColumnNum) throws Exception;


}

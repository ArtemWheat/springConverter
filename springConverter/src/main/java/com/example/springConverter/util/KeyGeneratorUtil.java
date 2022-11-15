package com.example.springConverter.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

public class KeyGeneratorUtil {
    public static String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now());
    }
}

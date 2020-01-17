package com.leon.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils() {

    }

    public static String readFileFromResources(String fileName) throws URISyntaxException, IOException {
        URL url = FileUtils.class.getClassLoader().getResource(fileName);
        if (url != null) {
            return new String(Files.readAllBytes(Paths.get(url.toURI())));
        } else {
            throw new IllegalArgumentException("File not found");
        }
    }

}
package com.codehunter.springhtmlpdfdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

@Service
public class PdfGeneratorService {

    public byte[] generateFromResource(String path) throws Exception {
        File file = ResourceUtils.getFile(path);
        return Files.readAllBytes(file.toPath());
    }
}

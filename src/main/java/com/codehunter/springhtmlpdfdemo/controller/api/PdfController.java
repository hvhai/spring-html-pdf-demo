package com.codehunter.springhtmlpdfdemo.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/pdf")
@Slf4j
public class PdfController {
    @GetMapping(value = "/{page}", produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String page) {
        byte[] content = null;
        try {
            File file = ResourceUtils.getFile("classpath:static/pdf/example.pdf");
            content = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(content);
        } catch (Exception exception) {
            log.error("error", exception);
        }
        return null;
    }
}

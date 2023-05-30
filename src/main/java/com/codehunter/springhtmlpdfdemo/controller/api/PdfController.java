package com.codehunter.springhtmlpdfdemo.controller.api;

import com.codehunter.springhtmlpdfdemo.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
@Slf4j
@RequiredArgsConstructor
public class PdfController {
    public static final String CLASSPATH_STATIC_PDF = "classpath:static/pdf/";

    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping(value = "/{page}", produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String page) {
        byte[] content = null;
        String fileName = "example.pdf";
        try {
            content = pdfGeneratorService.generateFromResource(CLASSPATH_STATIC_PDF + fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(content);
        } catch (Exception exception) {
            log.error("error", exception);
        }
        return null;
    }
}

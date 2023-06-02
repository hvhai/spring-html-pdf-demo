package com.codehunter.springhtmlpdfdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfGeneratorService {
    private final SpringTemplateEngine springTemplateEngine;

    public byte[] generateFromResource(String path) throws Exception {
        File file = ResourceUtils.getFile(path);
        return Files.readAllBytes(file.toPath());
    }

    public byte[] generateFromTemplate(String template) throws IOException {
        Context context = new Context();
        context.setVariable("createBy", "hive.happymoney.com");
        String htmlContent = springTemplateEngine.process(template, context);

        String xhtmlContent = htmlToXhtml(htmlContent);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        log.info("Dot per point {} ", renderer.getDotsPerPoint());
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        sharedContext.setReplacedElementFactory(new ITextReplacedElementFactory(new ITextOutputDevice(renderer.getDotsPerPoint())));
        sharedContext.getTextRenderer().setSmoothingThreshold(0);
        renderer.setDocumentFromString(xhtmlContent);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "hive.happymoney.com");

        return templateEngine.process("thymeleaf_template", context);
    }

    private static String htmlToXhtml(String inputHTML) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");
        System.out.println("parsing ...");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        System.out.println("parsing done ...");
        return document.html();
    }
}

package com.codehunter.springhtmlpdfdemo.service;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

import javax.imageio.ImageIO;

public class ImageReplacedElementFactory implements ReplacedElementFactory{
    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if (nodeName.equals("img")) {
            String attribute = e.getAttribute("src");
            FSImage fsImage;
            try {
                fsImage = imageForPDF(attribute, uac);
            } catch (BadElementException e1) {
                fsImage = null;
            } catch (IOException e1) {
                fsImage = null;
            }
            if (fsImage != null) {
                if (cssWidth != -1 || cssHeight != -1) {
                    //System.out.println("scaling");
                    fsImage.scale(cssWidth, cssHeight);
                }else {
                    fsImage.scale(2500, 1500);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    protected FSImage imageForPDF(String attribute, UserAgentCallback uac) throws IOException, BadElementException {
        InputStream input = null;
        FSImage fsImage;
//        input = new FileInputStream(attribute);
//        final byte[] bytes = IOUtils.toByteArray(input);
//        final Image image = Image.getInstance(bytes);
//        BufferedImage bufferedImage = ImageIO.read(new URL(attribute));
        final Image image = Image.getInstance(new URL(attribute));
        fsImage = new ITextFSImage(image);
        return fsImage;
    }

    @Override
    public void reset() {

    }

    @Override
    public void remove(Element e) {

    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {

    }
}

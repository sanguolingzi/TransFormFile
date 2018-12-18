/*
package cn.com.doc.pptToImage;

import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TestPPTToImage {
    public static void main(String args[]) throws IOException {

        //creating an empty presentation
        File file=new File("C:\\Users\\Administrator\\Desktop\\UI建议整合.pptx");
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

        //getting the dimensions and size of the slide
        Dimension pgsize = ppt.getPageSize();
        List<XSLFSlide> slide = ppt.getSlides();

        BufferedImage img = null;

        for (int i = 0; i < slide.size(); i++) {
            XSLFSlide xslfSlide =slide.get(i);
            List<XSLFShape> shapes =xslfSlide.getShapes();
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape sh = (XSLFTextShape) shape;
                    List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();
                    for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                        List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                        for (XSLFTextRun xslfTextRun : textRuns) {
                            xslfTextRun.setFontFamily("宋体");
                        }
                    }
                }
            }

            img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            //clear the drawing area
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

            //render
            slide.get(i).draw(graphics);

            //creating an image file as output
            FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\ppt_image_"+i+".png");
            javax.imageio.ImageIO.write(img, "png", out);
            ppt.write(out);
            out.close();
        }

        System.out.println("Image successfully created");
    }
}
*/

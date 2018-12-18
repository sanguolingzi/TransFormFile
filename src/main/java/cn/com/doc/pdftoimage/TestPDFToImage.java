package cn.com.doc.pdftoimage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 目前可用
 */
public class TestPDFToImage {

    private static String absolutePath = "C:\\Users\\Administrator\\Desktop\\";
    private static String fileName = "temp.pdf";

    public static boolean pdfToImg(String pdfPath,String imgDir){
         File file = new File(pdfPath);
         PDDocument doc;
        try {
                doc = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(doc);
                int pageCount = doc.getNumberOfPages();
                for(int i=0;i<pageCount;i++){
                BufferedImage image = renderer.renderImageWithDPI(i, 96);
                String path=imgDir+File.separator;
                if(new File(path).exists()==false){
                    new File(path).mkdirs();
                }
            ImageIO.write(image, "PNG", new File(imgDir+File.separator+i+".png"));
            }
            } catch (InvalidPasswordException e) {
            e.printStackTrace();
            return false;
            } catch (IOException e) {
            e.printStackTrace();
            return false;
            }
        return true;
        }
    public static void main(String[] args) throws Exception{
        TestPDFToImage.pdfToImg(absolutePath+fileName,absolutePath+"img");
    }

}

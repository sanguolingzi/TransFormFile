package cn.com.doc.model;

import com.aspose.slides.*;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class PptToImage {
    private boolean licenseResult = false;

    private int picCount = 1;

    private PdfOptions pdfOptions;

    public PptToImage() throws Exception {
        InputStream is = null;
        try {
            is = PptToImage.class.getClassLoader().getResourceAsStream("slides-license.xml");// license路径
            if(is != null) {
                License aposeLic = new License();
                aposeLic.setLicense(is);
                licenseResult = true;
            }

            pdfOptions = new PdfOptions();
            pdfOptions.setJpegQuality((byte)80);
            pdfOptions.setTextCompression(PdfTextCompression.Flate);
            pdfOptions.setSaveMetafilesAsPng(true);
            pdfOptions.setCompliance(PdfCompliance.Pdf15);

        } catch (Exception e) {
            //e.printStackTrace();
            throw  e;
        } finally {
            if(is != null){
                is.close();
            }
        }
    }


    /**
     *  aspose-slides for java 实现 ppt 转换成pdf 返回 pdf文件对象
     * @return
     */
    private File pptToPdf(String fileName,String pdfDestPath) throws Exception {

        if(!licenseResult){
            System.out.println("aspose-slides for java license error");
            return null;
        }

        InputStream srcSlides = null;
        FileOutputStream fileOS = null;
        Presentation auxPresentation = null;
        Presentation pres = null;
        File fileDir = null;
        ISlideCollection slides = null;
        File file = null;
        try {
            srcSlides = new FileInputStream(new File(fileName));
            if(srcSlides==null){
                return null;
            }
            long old = System.currentTimeMillis();
            pres = new Presentation(srcSlides);

            fileDir = new File(pdfDestPath);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }

            file = new File(fileDir.getPath()+File.separator+subFileName(fileName)+".pdf");// 输出pdf路径
            fileOS = new FileOutputStream(file);

            /*
            int pptSize = pres.getSlides().size();
            if(pptSize < picCount){
                picCount = pptSize;
            }
            */

            auxPresentation  = new Presentation();
            //新生成的对象中会默认给一个空白页
            auxPresentation.getSlides().removeAt(0);
            slides = pres.getSlides();
            //只加载小于initSize数量的ppt页数
            for(int i=0;i<picCount;i++){
                auxPresentation.getSlides().insertClone(i,slides.get_Item(i));
            }
            auxPresentation.save(fileOS,SaveFormat.Pdf,pdfOptions);
            //pres.save(fileOS, SaveFormat.Pdf);
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + file.getPath());

            return file;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.err.println(" pptToPdf error fileName:"+fileName);
            throw e;
        }finally {
            if(fileOS != null){
                fileOS.close();
                fileOS=null;
            }
            if(srcSlides != null){
                srcSlides.close();
                srcSlides=null;
            }
            if(pres != null){
                pres.dispose();
                pres=null;
            }

            if(auxPresentation != null){
                auxPresentation.dispose();
                auxPresentation=null;
            }
            fileDir = null;
            slides = null;
            System.gc();
        }
    }

    /**
     * pdfbox 实现  pdf转换成图片
     * @param srcPdf
     * @return
     */
    private boolean pdfToImg(File srcPdf) throws Exception{
        if(srcPdf == null){
            System.err.println("pdf file is null");
            return false;
        }

        if(!srcPdf.exists() || srcPdf.length() == 0){
            System.err.println("pdf file is empty");
            srcPdf.delete();
            return false;
        }

        PDDocument doc = null;
        PDFRenderer renderer = null;
        File pathFile = null;
        File tempFile = null;
        try {
            doc = PDDocument.load(srcPdf, MemoryUsageSetting.setupTempFileOnly());
            renderer = new PDFRenderer(doc);
            //int pageCount = doc.getNumberOfPages();
            //图片存放在名为pptName+"dir"的文件夹下
            //for(int i=0;i<pageCount;i++){
                //if(i >= picCount){
                //    break;
                //}
            BufferedImage image = renderer.renderImageWithDPI(0, 96);
            String path=srcPdf.getParent()+File.separator;
            pathFile = new File(path);
            if(pathFile.exists()==false){
                pathFile.mkdirs();
            }
            tempFile = new File(pathFile.getPath()+File.separator+subFileName(srcPdf.getName())+"_0.png");
            ImageIO.write(image, "PNG", tempFile);
            //System.out.println("create img success!");
            pathFile  = null;
            image = null;
            tempFile = null;
            //}
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if(doc != null){
                doc.close();
                doc = null;
            }

            if(srcPdf != null){
                srcPdf.delete();
                srcPdf = null;
            }

            pathFile = null;
            tempFile = null;
            System.gc();
        }
        return true;
    }

    private String subFileName(String srcFileName){
        int start = srcFileName.lastIndexOf(File.separator);
        int end = srcFileName.lastIndexOf(".");
        String name = srcFileName.substring(start+1,end);
        return name;
    }

    public boolean pptToImage(String fileName,String pdfDestPath) throws Exception{
        File pdf = pptToPdf(fileName,pdfDestPath);
        return pdfToImg(pdf);
    }
}

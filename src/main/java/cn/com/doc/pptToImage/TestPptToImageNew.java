package cn.com.doc.pptToImage;


import com.aspose.slides.*;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 先将ppt转换为临时pdf 再由pdf转换成图片
 */
public class TestPptToImageNew {

    private static InputStream license;
    private static InputStream slides;
    //初始ppt文件路径
    private static String pptSrcPath = System.getenv("pptSrcPath")==null?"C:\\Users\\Administrator\\Desktop\\": System.getenv("pptSrcPath");
    //临时存储pdf路径
    private static String pdfDestPath = System.getenv("pdfDestPath")==null?"C:\\Users\\Administrator\\Desktop\\": System.getenv("pdfDestPath");
    //pdf转换后的图片路径
    private static String pngDestPath =  System.getenv("pngDestPath")==null?"C:\\Users\\Administrator\\Desktop\\pngPpt": System.getenv("pngDestPath");


    private static String pdfExt = ".pdf";
    private static String pngExt = ".png";
    //Aspose slide 授权
    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            license = TestPptToImageNew.class.getClassLoader().getResourceAsStream("slides-license.xml");// license路径
            if(license == null){
                return false;
            }
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  aspose-slides for java 实现 ppt 转换成pdf 返回 pdf文件对象
     * @return
     */
    public static File pptToPdf(String fileName) throws IOException {

        if(!getLicense()){
            System.out.println("aspose-slides for java license error");
            return null;
        }

        FileOutputStream fileOS = null;
        Presentation auxPresentation = null;
        Presentation pres = null;
        try {
            slides = new FileInputStream(new File(pptSrcPath+fileName));
            if(slides==null){
                return null;
            }
            long old = System.currentTimeMillis();
            pres = new Presentation(slides);
            File file = new File(pdfDestPath+subFileName(fileName)+pdfExt);// 输出pdf路径
            fileOS = new FileOutputStream(file);


            int pptSize = pres.getSlides().size();
            int initSize = 3;
            if(pptSize < initSize){
                initSize = pptSize;
            }
            auxPresentation  = new Presentation();
            //新生成的对象中会默认给一个空白页
            auxPresentation.getSlides().removeAt(0);
            ISlideCollection slides = pres.getSlides();
            //只加载小于initSize数量的ppt页数
            for(int i=0;i<initSize;i++){
                auxPresentation.getSlides().insertClone(i,slides.get_Item(i));
            }

            PdfOptions pdfOptions = new PdfOptions();
            pdfOptions.setJpegQuality((byte)80);
            pdfOptions.setTextCompression(PdfTextCompression.Flate);
            pdfOptions.setSaveMetafilesAsPng(true);
            pdfOptions.setCompliance(PdfCompliance.Pdf15);
            auxPresentation.save(fileOS,SaveFormat.Pdf,pdfOptions);

            //pres.save(fileOS, SaveFormat.Pdf);
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + file.getPath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileOS != null){
                fileOS.close();
            }
            if(slides != null){
                slides.close();
            }

            if(license != null){
                license.close();
            }

            if(pres != null){
                pres.dispose();
            }

            if(auxPresentation != null){
                auxPresentation.dispose();
            }
        }
        return null;
    }

    /**
     * pdfbox 实现  pdf转换成图片
     * @param srcPdf
     * @return
     */
    public static boolean pdfToImg(File srcPdf) throws Exception{
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
        try {
            doc = PDDocument.load(srcPdf,MemoryUsageSetting.setupTempFileOnly());
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            //图片存放在名为pptName+"dir"的文件夹下
            for(int i=0;i<pageCount;i++){
                if(i >= 4){
                    break;
                }
                BufferedImage image = renderer.renderImageWithDPI(i, 96);
                String path=pngDestPath+File.separator+subFileName(srcPdf.getName())+File.separator;
                if(new File(path).exists()==false){
                    new File(path).mkdirs();
                }
                ImageIO.write(image, "PNG", new File(path+File.separator+subFileName(srcPdf.getName())+"_"+i+pngExt));
                System.out.println("create img success!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(doc != null){
                doc.close();
            }

            if(srcPdf != null){
                srcPdf.delete();
            }
            return false;
        }
        return true;
    }

    public static String subFileName(String srcFileName){
        int end = srcFileName.indexOf(".");
        String name = srcFileName.substring(0,end);
        return name;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("pptSrcPath:"+pptSrcPath);
        System.out.println("pdfDestPath:"+pdfDestPath);
        System.out.println("pngDestPath:"+pngDestPath);

        //if(args == null || args.length == 0){
        //    System.out.println("目标文件参数未传递!");
        //    return;
        //}

        //String fileName = args[0].toString();

        String fileName = "六年级上册语文课件-3 把我的心脏带回祖国｜苏教版 共25张PPT.ppt";


        System.out.println(java.util.Arrays.toString(args));
        //先把ppt转换为pdf
        File tempPdfFile = pptToPdf(fileName);
        //把pdf转换为图片
        System.out.println("pft to image result:"+pdfToImg(tempPdfFile));
    }


}

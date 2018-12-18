package cn.com.doc.ppttopdf;

import com.aspose.slides.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestPptToPdf {
    private static InputStream license;
    private static InputStream slides;

    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            license = TestPptToPdf.class.getClassLoader().getResourceAsStream("slides-license.xml");// license路径
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // 验证License
        if (!getLicense()) {
            return;
        }
        Presentation pres = null;
        Presentation auxPresentation = null;
        try {
            slides = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\testPPt.pptx"));
            long old = System.currentTimeMillis();


            LoadOptions loadOptions = new LoadOptions();
            pres = new Presentation(slides,loadOptions);
            //ppt长度
            System.out.println(pres.getSlides().size());
            int pptSize = pres.getSlides().size();

            int initSize = 3;
            if(pptSize < initSize){
                initSize = pptSize;
            }
            auxPresentation = new Presentation();
            //新生成的对象中会默认给一个空白页
            auxPresentation.getSlides().removeAt(0);
            ISlideCollection slides = pres.getSlides();
            //只加载小于initSize数量的ppt页数
            for(int i=0;i<initSize;i++){
                auxPresentation.getSlides().addClone(slides.get_Item(i));
            }

            System.out.println(auxPresentation.getSlides().size());
            PdfOptions pdfOptions = new PdfOptions();
            pdfOptions.setJpegQuality((byte)80);
            pdfOptions.setTextCompression(PdfTextCompression.Flate);
            pdfOptions.setSaveMetafilesAsPng(true);
            pdfOptions.setCompliance(PdfCompliance.Pdf15);
            auxPresentation.save("C:\\Users\\Administrator\\Desktop\\temp.pdf",SaveFormat.Pdf,pdfOptions);
            //pres.save("C:\\Users\\Administrator\\Desktop\\temp.pdf",SaveFormat.Pdf,pdfOptions);
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + "C:\\Users\\Administrator\\Desktop\\temp.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auxPresentation.dispose();
            pres.dispose();
        }
    }
}

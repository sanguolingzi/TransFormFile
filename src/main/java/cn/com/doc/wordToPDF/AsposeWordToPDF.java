package cn.com.doc.wordToPDF;

import com.aspose.words.Document;
import com.aspose.words.*;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AsposeWordToPDF {

    //初始doc文件路径
    private static String docSrcPath = System.getenv("docSrcPath")==null?"C:\\Users\\Administrator\\Desktop\\":System.getenv("docSrcPath");
    //doc转换后的图片路径
    private static String pngDocDestPath =  System.getenv("pngDocDestPath")==null?"C:\\Users\\Administrator\\Desktop\\pngDoc\\":System.getenv("pngDocDestPath");


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        System.out.println("docSrcPath:"+docSrcPath);
        System.out.println("pngDocDestPath:"+pngDocDestPath);

        /*
        if(args == null || args.length == 0){
            System.out.println("目标文件参数未传递!");
            return;
        }
        */

        //String fileName = args[0];
        String fileName = "六年级上册语文同步练习-4古诗两首苏教版.doc";

        File f = new File(docSrcPath+fileName);
        System.out.println(f.getAbsolutePath());
        System.out.println(f.getPath());
        System.out.println(f.getParent());

        //doc2png(fileName, pngDocDestPath);
        // TODO 自动生成的方法存根
        //doc2pdf("C:\\Users\\Administrator\\Desktop\\海花岛智能化提资复盘暨童世界智能化系统提资标准v4.0.1合并集团信息化意见.docx", "C:\\Users\\Administrator\\Desktop\\haihuadao.pdf");   //测试成功

//		pdf2doc("D://ImageTest//0054.pdf", "D://ImageTest//0056.doc");
    }
    public static boolean getLicense() {
        boolean result = false;
        try {

            InputStream is = AsposeWordToPDF.class.getClassLoader().getResourceAsStream("license.xml");
            // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String subFileName(String srcFileName){
        int end = srcFileName.indexOf(".");
        String name = srcFileName.substring(0,end);
        return name;
    }

    public static void doc2png(String fileName, String outPath) throws IOException {

        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            System.out.println("----lincese error---");
            return;
        }
        Document doc = null;
        FileOutputStream os = null;
        try {
            long old = System.currentTimeMillis();
            doc = new Document(docSrcPath+fileName); // Address是将要被转化的word文档
            if(doc == null){
                System.out.println("err: "+docSrcPath+fileName+" not found");
                return;
            }

            //图片转换设置
            ImageSaveOptions options = new ImageSaveOptions(SaveFormat.PNG);
            options.setPrettyFormat(true);
            options.setUseAntiAliasing(true);
            options.setUseHighQualityRendering(true);
            int pageCount = doc.getPageCount();

            File fileDir = new File(outPath+subFileName(fileName));
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }

            for(int i=0;i<pageCount;i++){
                File file = new File(fileDir.getPath()+File.separator+subFileName(fileName)+"_"+i+".png"); //生成目标图片
                os = new FileOutputStream(file);
                //设置转换第几页图片
                options.setPageIndex(i);
                doc.save(os, options);// 全面支持DOC, DOCX, OOXML, RTF HTML,
                os.flush();
                os.close();
            }
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null){
                os.close();
            }
        }
    }

    /* public static void doc2pdf(String inPath, String outPath) {

        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        File file = null;
        try {
            long old = System.currentTimeMillis();
            file = new File(outPath); // 新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML,
            // OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
//			Document doc = new Document(getMyDir() + "Document.doc");
//			doc.save(getMyDir() + "Document Out.html");
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
            if(file != null){
                file.delete();
            }
        }
    }*/
}

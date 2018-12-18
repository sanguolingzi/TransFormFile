package cn.com.doc.model;

import cn.com.doc.wordToPDF.AsposeWordToPDF;
import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocToImage {

    private boolean licenseResult = false;
    private String docPng = "docPng";
    private ImageSaveOptions options;

    public DocToImage() throws Exception {
        InputStream is = null;
        try {
            is = AsposeWordToPDF.class.getClassLoader().getResourceAsStream("license.xml");
            // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            licenseResult = true;


            options = new ImageSaveOptions(SaveFormat.PNG);
            options.setPrettyFormat(true);
            options.setUseAntiAliasing(true);
            //默认转换第一页
            options.setPageIndex(0);
            options.setUseHighQualityRendering(true);
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if(is != null){
                is.close();
            }
        }
    }


    public String subFileName(String srcFileName){
        int start = srcFileName.lastIndexOf(File.separator);
        int end = srcFileName.lastIndexOf(".");
        String name = srcFileName.substring(start+1,end);
        return name;
    }

    /**
     *
     * @param fileName 目标文件
     * @param outPath 输出图片的目录
     * @throws IOException
     */
    public void doc2png(String fileName, String outPath) throws Exception {
        if (!licenseResult) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            System.out.println("----lincese error---");
            return;
        }
        Document doc = null;
        FileOutputStream os = null;
        File file = null;
        File fileDir = null;
        try {
            //long old = System.currentTimeMillis();
            doc = new Document(fileName); // Address是将要被转化的word文档
            if(doc == null){
                System.out.println("err: "+fileName+" not found");
                return;
            }

            //图片转换设置
            //int pageCount = doc.getPageCount();

            fileDir = new File(outPath);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }

            //for(int i=0;i<pageCount;i++){

                //if(i>0){
                //    break;
                //}
            file = new File(fileDir.getPath()+File.separator+subFileName(fileName)+"_0.png"); //生成目标图片
            os = new FileOutputStream(file);
            //设置转换第几页图片
            doc.save(os, options);// 全面支持DOC, DOCX, OOXML, RTF HTML,
            os.flush();
            os.close();
            //}
            //long now = System.currentTimeMillis();
            //System.out.println(fileName+" doc to image 转换已完成,共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if(os != null){
                os.close();
                os = null;
            }
            doc = null;
            file = null;
            fileDir = null;
            System.gc();
        }
    }
}

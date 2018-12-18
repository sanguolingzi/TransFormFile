/*
package cn.com.doc.wordToPDF;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

*/
/**
 * 目前可用
 *//*

public class TestWordToPDF2 {
        public static void main(String[] args) {
            //this should be same as class name
            //create object of class
            TestWordToPDF2 cwoWord = new TestWordToPDF2();
            //you can specify your own path on the basis of file located
            cwoWord.ConvertToPDF("C:\\Users\\Administrator\\Desktop\\商业街店铺级客流统计系统方案1120【板式已调整】.docx", "C:\\Users\\Administrator\\Desktop\\dianpu.pdf");
        }

        public void ConvertToPDF(String docPath, String pdfPath) {
            try {
                //taking input from docx file
                InputStream doc = new FileInputStream(new File(docPath));
                //process for creating pdf started
                XWPFDocument document = new XWPFDocument(doc);
                PdfOptions options = PdfOptions.create();
                OutputStream out = new FileOutputStream(new File(pdfPath));
                PdfConverter.getInstance().convert(document, out, options);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }
}
*/

package cn.com.doc.application;

import cn.com.doc.model.DocToImage;
import cn.com.doc.model.PptToImage;
import cn.com.doc.model.VideoInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.RandomAccessFile;

public class ReadDir {
    //本地
    /*
    public String path = "C:\\Users\\Administrator\\Desktop\\2007-3D";
    public String previewPath = "F:\\preview";
    public String ffmepgPath = "D:\\ChromeCoreDownloads\\ffmpeg-20181128-b9aff7a-win64-static\\ffmpeg-20181128-b9aff7a-win64-static\\bin\\ffmpeg.exe";
    */
    //测试
    public String path = "G:\\黄色硬盘\\一号盘文件\\all\\";
    public String previewPath = "G:\\preview";
    public String ffmepgPath = "G:\\preview\\ffmpeg-20181128-b9aff7a-win64-static\\ffmpeg-20181128-b9aff7a-win64-static\\bin\\ffmpeg.exe";


    private DocToImage docToImage;

    private PptToImage pptToImage;

    private VideoInfo videoInfo;

    private RandomAccessFile randomAccessFile;

    private RandomAccessFile randomAccessSuccessFile;


    private long  seekLengh = 0;

    private long  seekSuccessLengh = 0;

    public static void main(String[] args) throws Exception{
        if(args.length > 0){
            String name = args[0];

            ReadDir readDir = new ReadDir();
            readDir.test(name);
        }else{
            System.out.println("args is empty");
        }

    }

    public ReadDir() throws Exception{
        docToImage = new DocToImage();
        pptToImage = new PptToImage();
        videoInfo = new VideoInfo(ffmepgPath);
        randomAccessFile = new RandomAccessFile(previewPath+File.separator+"errorFileList.txt","rw");
        randomAccessSuccessFile = new RandomAccessFile(previewPath+File.separator+"successFileList.txt","rw");

    }

    /**
     * 递归处理逻辑
     * 判断文件类型 若是 目录  则向下递归
     * 若是 文件 则直接处理业务
     */
    public void test(String name) throws Exception{
        File file = new File(path+name);
        System.out.println("file.isDirectory():"+file.isDirectory());
        System.out.println("file.isFile():"+file.isFile());
        if(file.isDirectory()){
            File[] files = handleFileList(file);
            for(File f:files){
                test2(f);
            }
        }else{
            test2(file);
        }
    }


    public void test2(File file){
        try{
            if(file.isDirectory()){
                File[] files = handleFileList(file);
                for(File f:files){
                    test2(f);
                }
            }else{
                //处理生成图片文件路径
                //处理前 f:\\a\\b\\c
                //处理后 previewPath+\\a\\b\\c
                String parentPath = file.getParent();
                int index = parentPath.indexOf(":");
                String tempPath = previewPath;
                if(index > 0){
                    tempPath = previewPath+parentPath.substring(index+1)+File.separator;
                }

                String fileName = file.getName();
                index = fileName.lastIndexOf(".");
                String ext = "";
                if(index > 0 ) {
                    ext = fileName.substring(index+1);
                }

                //判断是doc/docx
                if(isDoc(ext)){
                    docToImage.doc2png(file.getPath(),tempPath);
                    addSuccessInfoToLog(file);
                    //判断是ppt/pptx
                }else if(isPpt(ext)){
                    pptToImage.pptToImage(file.getPath(),tempPath);
                    addSuccessInfoToLog(file);
                }else if(isVideo(ext)){
                    videoInfo.handleVideo(file.getPath(),tempPath);
                    addSuccessInfoToLog(file);
                }else{
                    System.out.println("file:"+file.getPath());
                    System.out.println("file ext is not right :"+file.getName());
                    //errorFile.add(file);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            addErrorInfoToLog(file);
            //errorFile.add(file);
        }
    }


    /**
     * 过滤 指定file 目录下的文件列表
     * 后缀名为 doc/docx/ppt/pptx的视为ok
     * @param file
     * @return
     */
    private File[] handleFileList(File file){
        File[] files = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if(pathname.isDirectory()){
                    return true;
                }
                String fileName = pathname.getName();
                int index = fileName.lastIndexOf(".");
                if(index > 0 ){
                    String ext = fileName.substring(index+1);

                    if(isDoc(ext)
                        || isPpt(ext)
                        || isVideo(ext)    ){
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
        return files;
    }

    private boolean isDoc(String ext){
        return (ext.equalsIgnoreCase("doc")
                || ext.equalsIgnoreCase("docx"));
    }


    private boolean isPpt(String ext){
        return (ext.equalsIgnoreCase("ppt")
                || ext.equalsIgnoreCase("pptx"));
    }


    private boolean isVideo(String ext){
        return (ext.equalsIgnoreCase("avi")
                || ext.equalsIgnoreCase("mp4")
                || ext.equalsIgnoreCase("rmv")
                || ext.equalsIgnoreCase("mpg")
                || ext.equalsIgnoreCase("mpeg")
                || ext.equalsIgnoreCase("flv")
                || ext.equalsIgnoreCase("wmv"));
    }

    public void addErrorInfoToLog(File errorFile){
        try{
            randomAccessFile.seek(seekLengh);
            randomAccessFile.writeUTF("\n"+errorFile.getPath());
            seekLengh =  randomAccessFile.length();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addSuccessInfoToLog(File errorFile){
        try{
            randomAccessSuccessFile.seek(seekSuccessLengh);
            randomAccessSuccessFile.writeUTF("\n"+errorFile.getPath());
            seekSuccessLengh =  randomAccessSuccessFile.length();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

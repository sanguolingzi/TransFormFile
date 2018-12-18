package cn.com.doc.model;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoInfo {
        //视频路径
        private String ffmpegApp;
        //视频时
        private int hours;
        //视频分
        private int minutes;
        //视频秒
        private float seconds;
        //视频width
        private int width;
        //视频height
        private int heigt;


        public VideoInfo() {}

        public VideoInfo(String ffmpegApp)
        {
            this.ffmpegApp = ffmpegApp;
        }

        public String toString()
        {
            return "time: " + hours + ":" + minutes + ":" + seconds + ", width = " + width + ", height= " + heigt;
        }

        public void getInfo(String videoFilename) throws Exception
        {
            InputStream stderr = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try{

            String tmpFile = videoFilename + ".tmp.png";
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp, "-y",
                    "-i", videoFilename, "-vframes", "1", "-ss", "0:0:0", "-an",
                    "-vcodec", "png", "-f", "rawvideo", "-s", "100*100", tmpFile);

            Process process = processBuilder.start();
            stderr = process.getErrorStream();
            isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line;
            //打印 sb，获取更多信息。 如 bitrate、width、heigt
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            new File(tmpFile).delete();

            System.out.println("video info:\n" + sb);
            Pattern pattern = Pattern.compile("Duration: (.*?),");
            Matcher matcher = pattern.matcher(sb);
            if (matcher.find())
            {
                String time = matcher.group(1);
                calcTime(time);
            }
              /*
            pattern = Pattern.compile("w:\\d+ h:\\d+");
            matcher = pattern.matcher(sb);


            if (matcher.find())
            {
                String wh = matcher.group();
                //w:100 h:100
                String[] strs = wh.split("\\s+");
                if(strs != null && strs.length == 2)
                {
                    width = Integer.parseInt(strs[0].split(":")[1]);
                    heigt = Integer.parseInt(strs[1].split(":")[1]);
                }
            }
            */
            process.waitFor();
            }catch (Exception e){
                throw e;
            }finally {
                System.gc();
                if(br != null){
                    br.close();
                    br= null;
                }

                if(isr != null){
                    isr.close();
                    isr= null;
                }

                if(stderr != null){
                    stderr.close();
                    stderr= null;
                }
            }

        }

        /**
         *
         * @param videoFilename
         * @param thumbFilename
         * @param ssTime  截取视频开始时间 "时:分:秒" 如 "00:01:11"
         * @param toTime  截取视频结束时间 同上
         * @throws IOException
         * @throws InterruptedException
         */
        public void getThumbVideo(String videoFilename, String thumbFilename,String ssTime ,String toTime) throws IOException,
                InterruptedException
        {

            InputStream stderr = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try{
                ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp,
                        "-i", videoFilename, "-vcodec","copy", "-acodec","copy", "-ss",ssTime,
                        "-to", toTime,thumbFilename,"-y");

                Process process = processBuilder.start();

                stderr = process.getErrorStream();
                isr = new InputStreamReader(stderr);
                br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null)
                    ;
                process.waitFor();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                System.gc();
                if(br != null){
                    br.close();
                    br = null;
                }

                if(isr != null){
                    isr.close();
                    isr = null;
                }

                if(stderr != null){
                    stderr.close();
                    stderr = null;
                }

            }
        }

        private void calcTime(String timeStr)
        {
            String[] parts = timeStr.split(":");
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            seconds = Float.parseFloat(parts[2]);
        }


        public void handleVideo(String srcPath,String destPath) throws Exception{
            this.getInfo(srcPath);
            //截取30s 那么判断当前视频时长是否有30s长
            String ssTimeStart ="00:00:00";
            String ssTimeEnd = "00:00:30";
            int videoMin = this.getMinutes();
            if(videoMin < 1){
                float videoSec = this.getSeconds();
                if(videoSec < 30){
                    ssTimeEnd = "00:00:"+new Float(videoSec).intValue();
                }
            }
            this.getThumbVideo(srcPath,destPath+File.separator+subFileName(srcPath)+"_0"+getFileExt(srcPath),ssTimeStart,ssTimeEnd);
        }

        private String subFileName(String srcPath){
            int start = srcPath.lastIndexOf(File.separator);
            int end = srcPath.lastIndexOf(".");
            String name = srcPath.substring(start+1,end);
            return name;
        }

        private String getFileExt(String srcPath){
            int index = srcPath.lastIndexOf(".");
            if(index > 0){
                return srcPath.substring(index);
            }
            return ".mp4";
        }
        public String getFfmpegApp()
        {
            return ffmpegApp;
        }

        public void setFfmpegApp(String ffmpegApp)
        {
            this.ffmpegApp = ffmpegApp;
        }

        public int getHours()
        {
            return hours;
        }

        public void setHours(int hours)
        {
            this.hours = hours;
        }

        public int getMinutes()
        {
            return minutes;
        }

        public void setMinutes(int minutes)
        {
            this.minutes = minutes;
        }

        public float getSeconds()
        {
            return seconds;
        }

        public void setSeconds(float seconds)
        {
            this.seconds = seconds;
        }

        public int getWidth()
        {
            return width;
        }

        public void setWidth(int width)
        {
            this.width = width;
        }

        public int getHeigt()
        {
            return heigt;
        }

        public void setHeigt(int heigt)
        {
            this.heigt = heigt;
        }

        public static void main(String[] args)
        {
            VideoInfo videoInfo = new VideoInfo("D:\\ChromeCoreDownloads\\ffmpeg-20181128-b9aff7a-win64-static\\ffmpeg-20181128-b9aff7a-win64-static\\bin\\ffmpeg.exe");
            try
            {
                videoInfo.getInfo("C:\\Users\\Administrator\\Desktop\\ZXXK200631493811993.avi");
                System.out.println(videoInfo);

                //截取一分钟 那么判断当前视频时长是否有一分钟长
                String ssTimeStart ="00:00:00";
                String ssTimeEnd = "00:00:30";
                int videoMin = videoInfo.getMinutes();
                if(videoMin < 1){
                    float videoSec = videoInfo.getSeconds();
                    if(videoSec < 30){
                        ssTimeEnd = "00:00:"+new Float(videoSec).intValue();
                    }
                }
                videoInfo.getThumbVideo("C:\\Users\\Administrator\\Desktop\\ZXXK200631493811993.avi",
                        "C:\\Users\\Administrator\\Desktop\\ZXXK200631493811993_0.avi",
                        ssTimeStart,ssTimeEnd);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
}

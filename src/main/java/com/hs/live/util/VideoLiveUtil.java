package com.hs.live.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;


import javax.imageio.ImageIO;

@Slf4j
public class VideoLiveUtil{
    /**
     * 获取直播第一帧
     * @param streamURL  直播拉流路径
     * @param path 图片路径
     */
    public static void getFirtPicByStream(String streamURL,String path){

        //String streamURL="rtmp://xxx.xxxxx.com/appName/1591250292?auth_key=1591252092-0-0-e996f5d75ca9c86fb57c9e3a3cb47bf4";
        File file = new File(path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamURL);
        try {
            grabber.start();
            int ftp = grabber.getLengthInFrames();
            int flag=0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = grabber.grabImage();
                //过滤前3帧，避免出现全黑图片
                if ((flag>3)&&(frame != null)) {
                    break;
                }
                flag++;
            }
            FrameToBufferedImage(frame, file);
        } catch (FrameGrabber.Exception e) {
            e.getMessage();
        }  catch (IOException e) {
            e.getMessage();
        } finally {
            if(grabber != null){
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void FrameToBufferedImage(Frame frame,File file) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(frame);
        BufferedImage bi = new BufferedImage(390, 195, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(390, 195, Image.SCALE_SMOOTH),0, 0, null);
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
//    public void main() {
//    	String url = "rtmp://210.37.8.148:1935/live/chvfily";
//    	String path = "D:/liveTempImg/chvfily-t";
//    	this.getFirtPicByStream(url, path);
//    	
//    }

}



package com.hs.live.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;



public class VideoUtil {
	/**
	 * @author yuxuhang
	 * 
	 * @deprecated video frame  
	 * 
	 * @param path video path
	 * @param index  index of frame
	 * @param outImgPath output image of the one frame of video
	 * 
	 * */
    public static String getVideoImg(String path,int index, String outImgPath)throws Exception {
        Frame frame = null;
       //构造器支持InputStream，可以直接传MultipartFile.getInputStream()
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(path);
        //开始播放
        fFmpegFrameGrabber.start();
        //获取视频总帧数
        int ftp = fFmpegFrameGrabber.getLengthInFrames();
        //指定第几帧  保证帧数 
        if(ftp<index)
        	index = ftp;
        fFmpegFrameGrabber.setFrameNumber(index);
        //获取指定第几帧的图片
        frame = fFmpegFrameGrabber.grabImage();
        //文件绝对路径+名字
        String fileName =  outImgPath;    // "D:/upload/video/2019/08/24/first.jpg";
        File outPut = new File(fileName);
        ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
        // 设置相应的权限 
        changeFolderPermission(outPut);
        return fileName;
    }
    
    /**
     * @author yuxuhang 
     * live frame
     * 获取直播第一帧 生成jpg
     * @param streamURL 直播拉流路径
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
            // 设置相应的权限 
            changeFolderPermission(file);
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


    public static BufferedImage FrameToBufferedImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
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

    // 修改文件权限
    private static void changeFolderPermission(File dirFile) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
//        perms.add(PosixFilePermission.OWNER_EXECUTE); //可执行文件
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.OTHERS_READ);  //其他用户可读
//       perms.add(PosixFilePermission.GROUP_EXECUTE);
        try {
            Path path = Paths.get(dirFile.getAbsolutePath());
            Files.setPosixFilePermissions(path, perms);
        } catch (Exception e) {
        	System.out.print(e);
            }
        }


}

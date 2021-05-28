package com.hs.live.util;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.live.entity.HsVideo;
import com.hs.live.mapper.HsVideoMapper;


/**
 * 定时任务 获取video 的其中一帧
 * @author yuxuhang
 * @since 2021/05/03
 * 
 */
@Component
public class FirstFrameRefaceTaskUtil {
	
	@Autowired HsVideoMapper videoMapper;
	boolean enableReface = true; //是否可以更新 
	/**
	 * 1.获取视频所有的路径+名称+时间戳
	 * 2.更新所有图片资源所有的图像资源
	 * */
    @Scheduled(cron = "0 */1 * * * ?")  //一天更新一次
    public void execute() {
    	
    	if(enableReface) {
//    		enableReface  = false;
    		// 获取所有视频列表
    		List<HsVideo> videoList = videoMapper.findVideoAll();
            // 生成对应的第一帧图像
    		VideoUtil videoUtil = new VideoUtil();
    		for(HsVideo video:videoList){
    			if(videoList.size()!=0) {
    				String videoFileName = video.getSrsFile(); // /var/www/html/live/test.20210311211937471.mp4
    	            String outputPath = "//mnt//file//hs_video//live//videoImg//"+video.getVideoTitle()+".jpg"; // /var/www/html/live/videoImg/test.20210311211937471.mp4
    				int index = 1;
    	            File file = new File(outputPath);
    	            if (!file.getParentFile().exists()){
    	                file.getParentFile().mkdirs();
    	            }
    	            try {
    	    			System.out.println(videoUtil.getVideoImg(videoFileName,index,outputPath));
    	    		} catch (Exception e) {
    	    			// TODO Auto-generated catch block
    	    			e.printStackTrace();
    	    		}
    			}
    		}// end for       
    	}   
    }

}

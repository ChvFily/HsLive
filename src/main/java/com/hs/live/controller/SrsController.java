package com.hs.live.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hs.live.entity.HsFileServer;
import com.hs.live.entity.HsVideo;
import com.hs.live.service.IHsVideoService;
import com.hs.live.service.impl.HsFileServerServiceImpl;
import com.hs.live.srs.CallBackParam;
import com.hs.live.util.Helper;
//srs回调接口
@RestController
@RequestMapping("/srs")
public class SrsController {
	@Autowired IHsVideoService vs;
	@Autowired HsFileServerServiceImpl fss;
	
	//		/var/www/html/live/test.1609384972543.flv
	@RequestMapping("/dvr")
	public String on_dvr( HttpServletRequest request,@RequestBody CallBackParam p) {
		System.out.println(JSON.toJSONString(p));
		HsVideo v = new HsVideo();
		v.setClientId(p.client_id);
		v.setClientIp(p.ip);
		v.setSrsVhost(p.vhost);
		v.setSrsApp(p.app);
		v.setSrsStream(p.stream);
		v.setSrsParam(p.param);;
		v.setSrsCwd(p.cwd);
		v.setSrsFile(p.file);
		//v.setSaveTime(new Date());
		String serverIp = Helper.getIpAddress(request);
		System.out.println("srs服务器ip:"+serverIp);
		v.setSrsIp(serverIp);
		v.setVideoTitle(Helper.getFileNameFromUrl(p.file));
		v.setVideoDesc("");
		v.setVideoType("");
		v.setDeleteFlag(0);
		//生成url
		String url = getUrl(serverIp, p.file);
		if(url==null) {
			System.out.println("解析视频下载地址失败！"+p.file);
		}else {
			System.out.println("解析视频下载地址成功！"+url);
			v.setVideoUrl(url);
			vs.save(v);
		}
		return "0";
	}
	private String getUrl(String ip,String file) {
		List<HsFileServer> fsList =  fss.list();
		for(HsFileServer fs : fsList) {
			if(fs.getServerIp().equals(ip)) {
				String docDir = fs.getServerDocDir();
				int idx = file.indexOf(docDir);
				if(idx>=0){
					String path =  file.substring(docDir.length()); 
					String port =80==fs.getServerPort()?"":":"+fs.getServerPort();
					return fs.getServerSchema()+"://"+fs.getServerIp()+port+path; //自行拼接起来视频地址
				}
			}
		}
		return null;
	}
}

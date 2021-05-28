package com.hs.live.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.live.entity.HsFileServer;
import com.hs.live.entity.HsVideo;
import com.hs.live.mapper.HsVideoMapper;
import com.hs.live.service.IHsUserService;
import com.hs.live.service.IHsVideoService;
import com.hs.live.service.impl.HsFileServerServiceImpl;
import com.hs.live.srs.LiveStream;
import com.hs.live.util.LiveProperties;
import com.hs.live.util.VideoUtil;
//首页
@CrossOrigin  //支持跨域问题
@Controller
public class WebController {
	@Autowired HttpSession session;
	@Autowired IHsUserService us;
	@Autowired RestTemplate rt;
	@Autowired IHsVideoService vs;
	@Autowired LiveProperties liveProperties;
	@Autowired HsFileServerServiceImpl fss; // 获取服务器列表
	
	boolean flageIsVideoFramg = true;  // 是否执行剪切视频图片的功能
	
	@RequestMapping("/me")
	@ResponseBody
    public Object getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
	@RequestMapping("/mng")
	public String mng() {
		return "videoMng";
	}

	/**
	 * 获取视频流
	 * 更新视频的图片
	 * */
	@RequestMapping("/streams.do")
	public String streams(Model model) {
		//获取视频流列表
		List<LiveStream> liveList = getActiveStreams();  //返回 
		List<HsFileServer> fsList = fss.list();  // 获取服务器列表 （多个IP）
		
		String url = "";
		
		//String dir = "D:\\liveTempImg/";  //本地图片
		String dir = "//mnt//file//hs_video//live//liveImg//";   // /mnt/file/live/liveImg/ 直播图片
		//获取数据流地址 rtmp
		//拼凑地址
		for(LiveStream live:liveList) {
			// 获取数据流地址
			url="rtmp://"+live.ip+":1935/"+live.app+"/"+live.name;
			// 生成对应的图片
			VideoUtil.getFirtPicByStream(url, dir+live.name+".jpg");
		}
		// String url = "rtmp://"+ip+":1935/"+app+"/"+name;  
		model.addAttribute("streams", liveList);   // 获取 stream 字段的 数据 
		return "fragment/liveFragment";
		
	}
	/** 
	 * 数据格式
	 * {"code":0,"server":46638,
	 *	"streams":[
	 *  	{	
	 	*  		"id":46646,"name":"test","vhost":46639,"app":"live","live_ms":1611110008857,"clients":3,"frames":1626,
		 *		"send_bytes":26873333,
		 *		"recv_bytes":19727822,
		 *		"kbps":{"recv_30s":1075,"send_30s":3020},
		 *		"publish":{"active":true,"cid":824},
		 *		"video":{"codec":"H264","profile":"High","level":"3.1","width":1280,"height":720},
		 *		"audio":{"codec":"AAC","sample_rate":44100,"channel":2,"profile":"LC"}
	 *		}
	 *  ]}
	 *  url =  "rtmp://"+ip+":1935/"+app+"/"+name;
	 *  获取当前live info
	 *  显示直播事宜
	 */
	private List<LiveStream>  getActiveStreams() {
		List<LiveStream> ret = new ArrayList<>();
		List<HsFileServer> fsList = fss.list();  // 获取服务器列表 （多个IP）
		for(HsFileServer fs:fsList) {
			String api = "http://"+fs.getServerIp()+":"+fs.getSrsApiPort(); //http://210.37.8.148:1985/api/v1/streams/
			String url = api+"/api/v1/streams/";   // http://ip:1985/api/v1/streams/ 推流接口 推流情况
			String rs =  rt.getForObject(url, String.class); // 获取所有的视频流数据   
			JSONObject jo = JSONObject.parseObject(rs);  //  划分多个视频流数据 推流个数 stream 
			JSONArray arr = jo.getJSONArray("streams");  // 获取视频流的组
			//获取推流
			for(int i=0;i<arr.size();i++) {
				JSONObject s = arr.getJSONObject(i);
				LiveStream ls = LiveStream.from(s);  // 获取只需要的 数据 段 
				ls.ip = fs.getServerIp();
				if(ls.active) ret.add(ls);
			}
		}
		// String url = liveProperties.getSrsApiServer()+"/api/v1/streams/";
		return ret;
	}
	
	/**
	 * 返回搜索页面
	 * */
	@RequestMapping("/testInfo")
	@ResponseBody  // 发送数据能力  
	public String testInfo(String info) {
		/**
		 * 根据名称搜索，返回所有的关键字 
		 * */
		return info;
	}
	
	/*
	 * @RequestMapping("/error") public String error(){ return "error"; }
	 */
	@RequestMapping("/test")
    public String product(){
//		fss.list();
        return "fragment/seach";
    }
	
	@RequestMapping("/") // 返回主页
	public String home(Model model,Integer pageIndex) {
		if(pageIndex==null||pageIndex<1) pageIndex=1;
		//判断是否为管理员
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		//System.out.println(a.toString());
		boolean onlyPub = true;//只查找公开的视频
		if(a!=null  && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken)) { //认证后查询所有
			onlyPub = false;
		}
		Page<HsVideo> p = new Page<>(pageIndex,8);
		long totalPages = 0;//总页数
		try {
			IPage<HsVideo> rs = vs.page(p,Wrappers.lambdaQuery(HsVideo.class)
					.eq(onlyPub ,HsVideo::getPublicType, 1)
					.eq(HsVideo::getDeleteFlag, 0)
					.orderByDesc(HsVideo::getId));
			totalPages = rs.getPages();
		}catch (Exception e) {
			e.printStackTrace();	
		} 
		model.addAttribute("page",p); // 添加数据到 主页点的前端 所有内容
		model.addAttribute("totalPages",totalPages); //需要展示的页 内容
		return "index";
	}
	
	@RequestMapping("/liveDetails")   // url = /liveDetails?id=xxxx&ip=  通过 get 方式 获取 数据
    public String liveDetails(String ip,Integer id,Model model){
		if(id==null||id<=0 || ip==null || ip.isBlank()) return "liveDetails";
		HsFileServer fs =  fss.getById(ip);
		//String url = liveProperties.getSrsApiServer()+"/api/v1/streams/"+id;  回调窗口 
		String url =  "http://"+fs.getServerIp()+":"+fs.getSrsApiPort()+"/api/v1/streams/"+id; //返回一个直播根据id确定
		String rs =  rt.getForObject(url, String.class);
		JSONObject jo = JSONObject.parseObject(rs);
		LiveStream ls = LiveStream.from(jo.getJSONObject("stream"));
		if(!ls.active)	return "liveDetails";
		ls.url = "http://"+fs.getServerIp()+":"+fs.getSrsHttpPort()+"/"+ls.app+"/"+ls.name;
		model.addAttribute("live", ls);
        return "liveDetails";
    }
	/**
	 * @author yuxuhang
	 * @deprecated 获取一个直播的在线人数
	 * @param  id   根据id 信号数据 
	 * @return  number of clients
	 * */
	@RequestMapping("/clients") 
    public String liveClients( Integer id , Model model){
		/*
		 * 获取LiveStream 数据
		 * 循环遍历
		 * 返回对应id的 clients
		 *  
		 * */
		int liveClients = 0;
		List<LiveStream> LsList = new ArrayList<>(); // 存放 getActiveStreams() LiveStream
		LsList = getActiveStreams();
		for(LiveStream ls:LsList) {
			if(id == ls.id) {
				liveClients = ls.clients;
			}
		}
		model.addAttribute("clients", liveClients);
		return "fragment/liveClients";
    }
	
	@RequestMapping("/pageLive")
    public String pageLive(Model model){
		model.addAttribute("streams", getActiveStreams());
        return "pageLive";
    }
	
	@RequestMapping("/loadSeach_v")  //返回搜查的数据
    public String loadSeach_v(Model model,Integer pageIndex ,String seachInfo){
		if(pageIndex==null||pageIndex<1) pageIndex=1;
		//判断是否为管理员
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		//System.out.println(a.toString()); 
		boolean onlyPub = true;// 只查找 公开的视频
		if(a!=null  && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken)) { //认证后查询所有
			onlyPub = false;
		}
		Page<HsVideo> p = new Page<>(pageIndex,12);
		long totalPages = 0; //总页数
//		 List<HsVideo> users = HsVideoMapper.findAll();
		
		try {
			IPage<HsVideo> rs = vs.page(p,Wrappers.lambdaQuery(HsVideo.class)
					.eq(onlyPub ,HsVideo::getPublicType, 1)
					.eq(HsVideo::getDeleteFlag, 0)
					.eq(HsVideo::getSrsStream, seachInfo)  //搜索关键字段的数据
						.orderByDesc(HsVideo::getId));
			totalPages = rs.getPages();
		}catch (Exception e) {
			e.printStackTrace();	
		}
		// 封装 Page 
		model.addAttribute("page",p);
		model.addAttribute("seachInfo",seachInfo);  
		//model.addAttribute("totalPages",totalPages);
		return "fragment/show-v";
    }
	
	@RequestMapping("/loadSeach_p")  //返回搜查的数据
    public String loadSeach_p(Model model,Integer pageIndex ,String seachInfo){
		if(pageIndex==null||pageIndex<1) pageIndex=1;
		//判断是否为管理员
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		//System.out.println(a.toString());
		boolean onlyPub = true;// 只查找 公开的视频
		if(a!=null  && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken)) { //认证后查询所有
			onlyPub = false;
		}
		Page<HsVideo> p = new Page<>(pageIndex,12);
		long totalPages = 0; //总页数
		try {
			IPage<HsVideo> rs = vs.page(p,Wrappers.lambdaQuery(HsVideo.class)
					.eq(onlyPub ,HsVideo::getPublicType, 1)
					.eq(HsVideo::getDeleteFlag, 0)
					.eq(HsVideo::getSrsStream, seachInfo)  //搜索关键字段的数据
						.orderByDesc(HsVideo::getId));
			totalPages = rs.getPages();
		}catch (Exception e) {
			e.printStackTrace();	
		}
		// 封装 Page 
		model.addAttribute("page",p);
		model.addAttribute("totalPages",totalPages);
		model.addAttribute("seachInfo",seachInfo);
		return "fragment/show-p";
    }
	
	@RequestMapping("/pageVideo")
	public String pageVideo(Model model,Integer pageIndex) {
		if(pageIndex==null||pageIndex<1) pageIndex=1;
		//判断是否为管理员
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		//System.out.println(a.toString());
		boolean onlyPub = true;//只查找 公开的视频
		if(a!=null  && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken)) { //认证后查询所有
			onlyPub = false;
		}
		Page<HsVideo> p = new Page<>(pageIndex,12);
		long totalPages = 0;//总页数
		try {
			IPage<HsVideo> rs = vs.page(p,Wrappers.lambdaQuery(HsVideo.class)
					.eq(onlyPub ,HsVideo::getPublicType, 1)
					.eq(HsVideo::getDeleteFlag, 0)
						.orderByDesc(HsVideo::getId));
			totalPages = rs.getPages();
		}catch (Exception e) {
			e.printStackTrace();
		}
		// 封装 Page 
		model.addAttribute("page",p);
		model.addAttribute("totalPages",totalPages);
		return "pageVideo";
	}
	
	@RequestMapping("/details/{id}")
	public String details(Model model,@PathVariable int id) {
		HsVideo v = vs.getById(id);
		model.addAttribute("video", v); // 添加当前的视频数据
		return "videoDetails"; // 返回videoDetails 页面
	}
	
	@RequestMapping("/blank")
	public String blank() {
		return "blank";
	}
	//覆盖默认的登陆页面	//DefaultLoginPageGeneratingFilter
	@RequestMapping("/loginPage")
	public String loginPage(Model model,String error) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if(auth instanceof AnonymousAuthenticationToken){
	      return "login";
	    }else{
	      return "redirect:/";
	    }
	}
	
	@RequestMapping("/profile")
	public String r(String token,Model model) {
		if(!StringUtils.hasLength(token)) {
			String url = liveProperties.getCasServerUrl()+"/oauth2.0/profile?access_token="
					+token;
			try {
				//RestTemplate rt =new RestTemplate();//HttpClientUtils.getInstance(null, false);
				model.addAttribute("data",rt.getForObject(url, String.class));
			}
			catch (Exception e) {
				model.addAttribute("data",e.getMessage());
			}
		}
		return "profile";
	}
	// 播放视频流
	@RequestMapping(value = "/getVideo/{id}")
	public void getVideo(HttpServletRequest request, HttpServletResponse response,@PathVariable int id)  {
		try {
			HsVideo v = vs.getById(id);
			String url = v.getVideoUrl();
			InputStream in = new URL(url).openStream();
			//InputStream in = new FileInputStream(new File("C:\\Users\\LIBO\\Videos\\class.mp4"));
			ServletOutputStream out = response.getOutputStream(); 
			int length;
			byte[] buffer = new byte[20 * 1024];
			// 向前台输出视频流
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/getImg")
	public void getImg(HttpServletRequest request, HttpServletResponse response)  {
		try {
			//String url = "https://t7.baidu.com/it/u=2588035336,903742099&fm=193&f=GIF";
			String url = "http://zh.hnhggp.com:18083/apk/timg.jpg";
			InputStream in = new URL(url).openStream();
			ServletOutputStream out = response.getOutputStream(); 
			int length;
			byte[] buffer = new byte[20 * 1024];
			// 向前台输出视频流
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

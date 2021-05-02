package com.hs.live.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.live.entity.HsVideo;
import com.hs.live.entity.Result;
import com.hs.live.service.IHsVideoService;
//管理页
@Controller
@RequestMapping("/mng")
public class MngController {
	@Autowired IHsVideoService vs;
	
	@RequestMapping("/srsMng")
	public String srsMng() {
		return "sys/srsMng";
	}
	@RequestMapping("/userMng")
	public String userMng() {
		return "sys/userMng";
	}
	@RequestMapping("/videoTypeMng")
	public String videoTypeMng() {
		return "sys/videoTypeMng";
	}
	
	@RequestMapping("/videoList.do")
	@ResponseBody
	public String videoList(String videoType,Integer publicType) {
		if(publicType==null) publicType=-1;
		if(videoType==null) videoType="";
		JSONObject jo = new JSONObject();
		Page<HsVideo> p = new Page<>(1,100);//只查询前100条记录
		IPage<HsVideo> rs = vs.page(p,Wrappers.lambdaQuery(HsVideo.class) 
				.eq(publicType>=0,HsVideo::getPublicType, publicType)
				.eq(StringUtils.hasLength(videoType) ,HsVideo::getVideoType,videoType )
				.orderByDesc(HsVideo::getId));
		
		List<HsVideo> list =rs.getRecords();//vs.list();
		
		JSONArray ja = new JSONArray();
		ja.addAll(list);
		jo.put("data", ja);
		return jo.toJSONString();
		
	}
	@RequestMapping("/saveVideo.do")
	@ResponseBody
	public String saveVideo(HsVideo v) {
		boolean rs =  vs.update(null,Wrappers.lambdaUpdate(HsVideo.class).eq(HsVideo::getId, v.getId())
				.set(HsVideo::getVideoTitle, v.getVideoTitle()==null?"":v.getVideoTitle())
				.set(HsVideo::getVideoDesc, v.getVideoDesc()==null?"":v.getVideoDesc())
				.set(HsVideo::getVideoType, v.getVideoType()==null?"":v.getVideoType())
				.set(HsVideo::getPublicType, v.getPublicType())
				.set(HsVideo::getDeleteFlag, v.getDeleteFlag())
				);
		if(rs)return JSON.toJSONString(new Result(true,"修改成功！",vs.getById(v.getId())));
		else return  JSON.toJSONString(new Result(false,"修改失败！",v));
	}
	@RequestMapping("/removeVideo.do")
	@ResponseBody
	public String removeVideo(int id) {
		boolean rs =  vs.update(null,Wrappers.lambdaUpdate(HsVideo.class).eq(HsVideo::getId, id)
				.set(HsVideo::getDeleteFlag, 1)
				);
		if(rs)return JSON.toJSONString(new Result(true,"操作成功！"));
		else return  JSON.toJSONString(new Result(false,"操作失败！"));
	}
	@RequestMapping("/removeVideoCompletely.do")
	@ResponseBody
	public String removeVideoCompletely(int id) {
		HsVideo v = vs.getById(id);
		String path = v.getSrsFile();
		if(!StringUtils.hasLength(path)) {
			return  JSON.toJSONString(new Result(false,"操作失败：文件路径不存在！"));
		}
		File f = new File(v.getSrsFile());
		if(!f.exists()) {
			return  JSON.toJSONString(new Result(false,"操作失败：文件不存在或无权访问该文件！"));
		}
		boolean rs =  f.delete();
		if(!rs) return  JSON.toJSONString(new Result(false,"操作失败：文件删除失败！"));
		rs = vs.removeById(id);//删除记录
		if(rs)return JSON.toJSONString(new Result(true,"记录和文件均已删除成功！"));
		else return  JSON.toJSONString(new Result(false,"文件已被删除，但记录删除失败！"));
	}
	
	//用户管理
	
	
}

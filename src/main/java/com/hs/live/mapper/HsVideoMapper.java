package com.hs.live.mapper;

import com.hs.live.entity.HsVideo;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 录制的视频 Mapper 接口 
 * </p>
 *
 * @author LIBO
 * @since 2020-12-31
 */
public interface HsVideoMapper extends BaseMapper<HsVideo> { 
	
	//* 数据库 mybatis 接口
	/**
	 * 查询hs_video 表所有数据
	 * */
	@Select("select * from hs_video")
		List<HsVideo> findVideoAll();

}

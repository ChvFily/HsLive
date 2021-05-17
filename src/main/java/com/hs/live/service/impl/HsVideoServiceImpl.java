package com.hs.live.service.impl;

import com.hs.live.entity.HsVideo;
import com.hs.live.mapper.HsVideoMapper;
import com.hs.live.service.IHsVideoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 录制的视频 服务实现类
 * </p>
 *
 * @author LIBO
 * @since 2020-12-31
 */
@Service
public class HsVideoServiceImpl extends ServiceImpl<HsVideoMapper, HsVideo> implements IHsVideoService {

	public boolean updateInfo(HsVideo v){
		return update(null,Wrappers.lambdaUpdate(HsVideo.class).eq(HsVideo::getId, v.getId())
				.set(HsVideo::getVideoTitle, v.getVideoTitle()) //*视频标题
				.set(HsVideo::getVideoDesc, v.getVideoDesc())   //*解码描述
				.set(HsVideo::getVideoType, v.getVideoType())   //*视频类型 
				);
	}

}

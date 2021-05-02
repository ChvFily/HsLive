package com.hs.live.service.impl;

import com.hs.live.entity.HsFileServer;
import com.hs.live.mapper.HsFileServerMapper;
import com.hs.live.service.IHsFileServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频文件下载的服务器配置 服务实现类
 * </p>
 *
 * @author LIBO
 * @since 2020-12-31
 */
@CacheConfig(cacheNames = "HsFileServer")
@Service
public class HsFileServerServiceImpl extends ServiceImpl<HsFileServerMapper, HsFileServer> implements IHsFileServerService {
	@Cacheable(key = "#root.methodName",unless = "#result==null")
	@Override
	public List<HsFileServer> list() {
		return super.list();
	}
	@Cacheable(key="#id",unless = "#result==null")
	@Override
	public HsFileServer getById(Serializable id) {
		/**
		 * 根据 ID 查询数据
		 * */
		return super.getById(id);
	}
}

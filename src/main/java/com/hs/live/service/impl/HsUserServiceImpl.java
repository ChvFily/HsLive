package com.hs.live.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.live.entity.HsUser;
import com.hs.live.mapper.HsUserMapper;
import com.hs.live.service.IHsUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIBO
 * @since 2021-01-11
 */
@Service
public class HsUserServiceImpl extends ServiceImpl<HsUserMapper, HsUser> implements IHsUserService {
	// 实现类
	public HsUser  findByUsername(String username){
		// 根据条件 查询一个记录
		return getOne(Wrappers.lambdaQuery(HsUser.class).eq(HsUser::getUsername, username));
	}
}

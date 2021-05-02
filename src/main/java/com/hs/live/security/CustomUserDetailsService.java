package com.hs.live.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.hs.live.entity.HsUser;
import com.hs.live.service.impl.HsUserServiceImpl;
import com.hs.live.util.LiveProperties;
//本地数据库源，表示管理用户，SSO认证的表示普通用户
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired HsUserServiceImpl us;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HsUser user = us.findByUsername(username);
		if (user == null) {
			// log.warn("User: {} not found", login);
			throw new UsernameNotFoundException("账号‘" + username + "’不存在");
			// 这里找不到必须抛异常
		}
		//UsernamePasswordAuthenticationFilter
		// 2. 设置角色
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");	
		grantedAuthorities.add(grantedAuthority);

		return new User(username, user.getUserpwd(),
				grantedAuthorities);
	}

}

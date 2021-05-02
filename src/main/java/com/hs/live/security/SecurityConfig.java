package com.hs.live.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hs.live.util.EncryptUtil;
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired 
	private UserDetailsService userDetailsService;
	
	
	@Bean
	public MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(userDetailsService);
        return myAuthenticationProvider;
    }
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				if(rawPassword==null|| encodedPassword==null) return false;
				System.out.println(rawPassword);
				System.out.println(encodedPassword);
				return encodedPassword.equals(encode(rawPassword));
			}
			@Override
			public String encode(CharSequence rawPassword) {
				if(rawPassword==null) return null;
				return EncryptUtil.md5Base64(rawPassword.toString());
			}
		};
	}
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(myAuthenticationProvider()));
	}
	//认证
	protected void configure2(AuthenticationManagerBuilder auth) throws Exception {
		//super.configure(auth);
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
		/*
		 auth
         .inMemoryAuthentication()
         .withUser("admin") // 添加用户admin
         .password("{noop}admin")  // 不设置密码加密
         .roles("ADMIN", "USER")// 添加角色为admin，user
         .and()
         .withUser("user") // 添加用户user
         .password("{noop}user") 
         .roles("USER")
     	.and()
     	.withUser("tmp") // 添加用户tmp
         .password("{noop}tmp")
     	.roles(); // 没有角色
		 */
	}
	//授权
	// DefaultRequiresCsrfMatcher
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		 http
		 .csrf().disable()
		 .headers().frameOptions().disable().and()
         .authorizeRequests()
         	//.antMatchers(HttpMethod.POST,"/doLogin").permitAll()  // 自定义登录处理
         	.antMatchers("/","/streams.do","/test","/pageVideo","/pageLive","/srs/**"
         			,"/loginPage","/liveDetails/**","/clients/**","/testInfo/**","/loadSeach**").permitAll()
	        .antMatchers("/mng/**","/console/**").hasRole("ADMIN") //添加/admin/** 下的所有请求只能由admin角色才能访问
	        .anyRequest().authenticated() // 没有定义的请求，所有的角色都可以访问（tmp也可以）。
	        .and()
         .formLogin().loginPage("/loginPage").loginProcessingUrl("/loginProcess")
		    //.defaultSuccessUrl("/")
		    //.failureUrl("/login?error=true")
         	.and()
         .httpBasic()
         .and().logout();//.logoutUrl("/logout");
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/res/**","/favicon.ico");
	}
}

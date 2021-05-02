package com.hs.live.util;

import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
@Component
public class SpringUtil implements ApplicationContextAware{
	private static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(context == null) {
				context = applicationContext;
	       }
	}
	//通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
    //手动清空所有缓存,
    public static String clearCache() {
    	CacheManager cm = getBean(CacheManager.class);//CaffeineCacheManager
    	for(String name:cm.getCacheNames()) {
    		Cache cache = cm.getCache(name);
    		if(cache!=null) cache.clear();
    	}
    	return "已完全清除了缓存，名称列表:"+ StringUtils.collectionToCommaDelimitedString(cm.getCacheNames());
    }
    public static String showCache() {
    	CacheManager cm = getBean(CacheManager.class);
    	return "缓存名称列表:"+ StringUtils.collectionToCommaDelimitedString(cm.getCacheNames());
    }
}

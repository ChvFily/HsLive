package com.hs.live.util;

import java.nio.charset.StandardCharsets;

import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

public class EncryptUtil {
	public static void main(String[] args) {
		String str = "admin654321";
		System.out.println(md5Base64(str));
		System.out.println(md5Hex(str));
	}
	public static String md5Base64(String rawPassword) {
		if(rawPassword==null) return null;
        byte[] bs =  DigestUtils.md5Digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(bs);
	}
	public static String md5Hex(String rawPassword) {
		if(rawPassword==null) return null;
		return DigestUtils.md5DigestAsHex(rawPassword.getBytes(StandardCharsets.UTF_8));
	}
}

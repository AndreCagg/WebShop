package it.userservice.service.impl;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;

@Service
public class CookieServ {
	public Cookie createCookie(String name, String value, String path, Integer exp) {
		Cookie c=new Cookie(name, value);
		c.setHttpOnly(true);
		c.setPath(path);
		c.setMaxAge(exp);
		
		return c;
	}
}

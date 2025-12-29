package it.userservice.service.intf;

import jakarta.servlet.http.Cookie;

public interface ICookieServ {
	public Cookie createCookie(String name, String value, String path, Integer exp);
}

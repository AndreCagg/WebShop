package it.userservice.service.intf;

import java.util.Map;

public interface IAuthenticationServ {
	public Map<String, String> authenticate(String user, String psw);
}

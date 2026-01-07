package it.userservice.service.intf;

import java.util.List;

import it.userservice.entity.Ruoli;
import it.userservice.entity.User;

public interface IUserServ {
	public User cerca(String id);
	public User crea(String user, String psw, List<Ruoli> ruoli);
	//public Boolean isLogged(String jwt);
}

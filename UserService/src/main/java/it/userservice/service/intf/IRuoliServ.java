package it.userservice.service.intf;

import java.util.List;

import it.userservice.entity.Ruoli;

public interface IRuoliServ {
	public List<Ruoli> cercaByUserid(String id);
}

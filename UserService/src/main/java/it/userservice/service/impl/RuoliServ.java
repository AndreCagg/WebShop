package it.userservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.userservice.entity.Ruoli;
import it.userservice.repo.IRuoliRepo;
import it.userservice.service.intf.IRuoliServ;

@Service
public class RuoliServ implements IRuoliServ{
	@Autowired
	private IRuoliRepo repoRuoli;
	
	@Override
	public List<Ruoli> cercaByUserid(String id) {
		return this.repoRuoli.selByUserid(id);
	}

}

package it.userservice.service.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import it.userservice.entity.Ruoli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.userservice.entity.User;
import it.userservice.repo.IUserRepo;
import it.userservice.service.intf.IUserServ;


@Service
public class UserServ implements IUserServ {
	@Autowired
	private IUserRepo repoUser;
	
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public User cerca(String id) {
		return this.repoUser.findById(id).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
	}
	
	@Override
	public User crea(String user, String psw, List<Ruoli> ruoli) {
		User u=new User();
		u.setUserid(user);
		
		String encoded=encoder.encode(psw);
		u.setPassword(encoded);
		
		u.setId(UUID.randomUUID().toString());
		System.out.println(u.getId());
		u.setAttivo("Si");
		
		Random random=new Random();
		
		for(Ruoli r : ruoli) {
			r.setId(random.nextInt(Integer.MAX_VALUE));
			r.setUtente(u);
		}
		
		u.setRuoli(ruoli);
		
		//this.repoRuoli.saveAll(ruoli);
		return this.repoUser.save(u);
	}
	
	@Override
	public Boolean isLogged(String jwt) {
		JwtServ serv=new JwtServ();
		return serv.isValid(jwt);
	}

}

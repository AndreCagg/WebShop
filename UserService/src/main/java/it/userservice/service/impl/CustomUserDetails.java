package it.userservice.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.userservice.entity.Ruoli;
import it.userservice.entity.User;
import it.userservice.repo.IUserRepo;

@Service
public class CustomUserDetails implements UserDetailsService {
    
    @Autowired
    private IUserRepo repoUser;
    
    /*public CustomUserDetails(IUserRepo userRepository) {
        this.repoUser = userRepository;
    }*/


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u=this.repoUser.findByUserid(username);
		
		if(u==null) {
			throw new UsernameNotFoundException("Utente non trovato");
		}
		
		
		return org.springframework.security.core.userdetails.User.withUsername(u.getUserid().trim()).password(u.getPassword().trim())
				.roles(u.getRuoli().stream().map(Ruoli::getRuolo).toArray(String[]::new)).build();

	}

    
}


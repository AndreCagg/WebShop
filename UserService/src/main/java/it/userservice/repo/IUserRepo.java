package it.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import it.userservice.entity.User;

public interface IUserRepo extends JpaRepository<User, String> {
	public User findByUserid(String id);
}

package it.userservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.userservice.entity.Ruoli;

public interface IRuoliRepo extends JpaRepository<Ruoli, Integer> {
	@Query(value="SELECT r FROM Ruoli r WHERE r.utente.id=:id")
	public List<Ruoli> selByUserid(@Param("id") String id);
}

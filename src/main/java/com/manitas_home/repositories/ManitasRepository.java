package com.manitas_home.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Manitas;

public interface ManitasRepository extends JpaRepository<Manitas, Long>{
	public Manitas findOneByEmailAndPassword(String email,String password);
	public Manitas findOneByEmail(String email);
}

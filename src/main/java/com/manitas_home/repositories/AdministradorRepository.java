package com.manitas_home.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long>{
	public Administrador findOneByEmailAndPassword(String email,String password);
	public Administrador findOneByEmail(String email);
}

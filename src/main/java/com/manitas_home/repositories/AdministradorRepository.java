package com.manitas_home.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long>{
	public <List>Administrador findByEmailAndPassword(String email,String password);
	public <List>Administrador findByEmail(String email);
}

package com.manitas_home.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	public <List>Cliente findByEmailAndPassword(String email,String password);
	public <List>Cliente findByEmail(String email);
}

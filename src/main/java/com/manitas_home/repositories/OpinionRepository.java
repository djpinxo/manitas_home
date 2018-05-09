package com.manitas_home.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Opinion;

public interface OpinionRepository extends JpaRepository<Opinion, Long>{
	public Opinion findOneByClienteAndManitas(Cliente cliente,Manitas manitas);
	public <Opinion>List findByManitas(Long idManitas);
}

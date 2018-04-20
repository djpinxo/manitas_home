package com.manitas_home.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Empleo;

public interface EmpleoRepository extends JpaRepository<Empleo, Long>{
	public Empleo findOneByNombre(String nombre);
}

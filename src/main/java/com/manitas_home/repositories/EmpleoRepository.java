package com.manitas_home.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Categoria;
import com.manitas_home.domain.Empleo;

public interface EmpleoRepository extends JpaRepository<Empleo, Long>{
	public Empleo findOneByNombre(String nombre);
	public List<Empleo> findByCategoria(Categoria categoria);
}

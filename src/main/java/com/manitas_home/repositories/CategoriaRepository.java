package com.manitas_home.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	public Categoria findOneByNombre(String nombre);

}

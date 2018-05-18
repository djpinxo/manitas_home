package com.manitas_home.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Empleo {
	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	@ManyToOne
	private Categoria categoria;
	@ManyToMany(mappedBy="empleos")
	private List<Manitas> manitas;
	
	public Empleo(){
	}
	public Empleo(String nombre, Categoria categoria){
		this.nombre=nombre;
		this.categoria=categoria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Manitas> getManitas() {
		return manitas;
	}
	public void setManitas(List<Manitas> manitas) {
		this.manitas = manitas;
	}
	
}

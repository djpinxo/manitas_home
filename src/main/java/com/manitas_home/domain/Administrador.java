package com.manitas_home.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Administrador {
	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	private String email;
	private String password;
	private boolean borrable;

	public Administrador() {
	}

	public Administrador(String nombre, String email, String password, boolean borrable) {
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.borrable = borrable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getBorrable() {
		return borrable;
	}

	public void setBorrable(boolean borrable) {
		this.borrable = borrable;
	}
}

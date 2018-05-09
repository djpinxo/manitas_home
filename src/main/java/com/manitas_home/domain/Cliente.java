package com.manitas_home.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cliente extends Usuario{

	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	private String apellidos;
	private String telefono;
	private String email;
	private String password;
	private String direccion;
	private boolean habilitado;
	@OneToMany(mappedBy = "cliente")
	private Collection<Opinion> opiniones;

	public Cliente() {
		this.habilitado = false;
	}

	public Cliente(String nombre, String apellidos, String telefono, String email, String password, String direccion) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.direccion = direccion;
		this.habilitado = false;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	public Collection<Opinion> getOpiniones() {
		return opiniones;
	}
	public void setOpiniones(Collection<Opinion> opiniones) {
		this.opiniones = opiniones;
	}
}

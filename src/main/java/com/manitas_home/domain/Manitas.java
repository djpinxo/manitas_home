package com.manitas_home.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Manitas extends Usuario{
	
	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	private String apellidos;
	private String telefono;
	private String email;
	private String password;
	private String direccion;
	private String descripcion;
	private int radioAccion;
	private boolean habilitado;
	@ManyToMany
	private List<Empleo> empleos;
	@OneToMany(mappedBy = "manitas")
	private List<Opinion> opiniones;


	public Manitas() {
		this.habilitado=false;
	}
	public Manitas(String nombre,String apellidos,String telefono, String email, String password,String direccion,String descripcion,int radioAccion) {
	this.nombre=nombre;
	this.apellidos=apellidos;
	this.telefono=telefono;
	this.email=email;
	this.password=password;
	this.direccion=direccion;
	this.descripcion=descripcion;
	this.radioAccion=radioAccion;
	this.habilitado=false;
	this.empleos=new <Empleo>ArrayList();
	this.opiniones=new <Opinion>ArrayList();
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	public int getRadioAccion() {
		return radioAccion;
	}
	public void setRadioAccion(int radioAccion) {
		this.radioAccion = radioAccion;
	}
	public List<Empleo> getEmpleos() {
		return empleos;
	}
	public void setEmpleos(List<Empleo> empleos) {
		this.empleos = empleos;
	}
	public List<Opinion> getOpiniones() {
		return opiniones;
	}
	public void setOpiniones(List<Opinion> opiniones) {
		this.opiniones = opiniones;
	}
	public String getNombreYApellidos(){
		String salida=this.nombre.substring(0, 1).toUpperCase()+this.nombre.substring(1).toLowerCase()+" ";
		String [] apellidos=this.apellidos.split(" ");
		for(String apellido:apellidos){
			salida+=apellido.substring(0, 1).toUpperCase()+apellido.substring(1).toLowerCase()+" ";
		}
		return salida.substring(0,salida.length()-1);
	}
	public String mediaValoracionesString(){
		String retorno="";
		double valor=0;
		
			for (int i=0;i<opiniones.size();i++){
				valor+=opiniones.get(i).getValoracion();
			}
			String stringRetorno=String.valueOf(valor/opiniones.size());
			if(stringRetorno.length()<5)
				retorno=stringRetorno.substring(0, stringRetorno.length());
			else
				retorno=stringRetorno.substring(0, 4);
		return retorno;
	}
	public double mediaValoracionesDouble(){
		double valor=0;
			for (int i=0;i<opiniones.size();i++){
				valor+=opiniones.get(i).getValoracion();
			}
		return valor/opiniones.size();
	}
	public int contarEstrellas(int opcion){
		int retorno=0;
		int valor=0;
		switch (opcion) {
		case 1:
			for (int i=0;i<opiniones.size();i++){
				if(opiniones.get(i).getValoracion()==1)
					valor++;
			}
			retorno=valor;
			break;

		case 2:
			for (int i=0;i<opiniones.size();i++){
				if(opiniones.get(i).getValoracion()==2)
					valor++;
			}
			retorno=valor;
			break;
		case 3:
			for (int i=0;i<opiniones.size();i++){
				if(opiniones.get(i).getValoracion()==3)
					valor++;
			}
			retorno=valor;
			break;

		case 4:
			for (int i=0;i<opiniones.size();i++){
				if(opiniones.get(i).getValoracion()==4)
					valor++;
			}
			retorno=valor;
			break;

		case 5:
			for (int i=0;i<opiniones.size();i++){
				if(opiniones.get(i).getValoracion()==5)
					valor++;
			}
			retorno=valor;
			break;
	}
		return retorno;
	}	

}

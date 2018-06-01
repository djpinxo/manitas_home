package com.manitas_home.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.manitas_home.Date;

@Entity
public class Mensaje {
	@Id
	@GeneratedValue
	private Long id;
	private String remitente;
	private String destinatario;
	private String mensaje;
	private long fecha;
	boolean leido;
	
	public Mensaje(){
	}
	public Mensaje(String remitente, String destinatario, String mensaje){
		this.remitente=remitente;
		this.destinatario=destinatario;
		this.mensaje=mensaje;
		this.fecha=System.currentTimeMillis();
		this.leido=false;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Date getFecha() {
		return new Date(fecha);
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	public boolean isLeido() {
		return leido;
	}
	public void setLeido(boolean leido) {
		this.leido = leido;
	}
	
}

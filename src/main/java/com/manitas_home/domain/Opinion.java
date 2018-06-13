package com.manitas_home.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Opinion {
	@Id
	@GeneratedValue
	private Long id;
	private String comentario;
	private Double valoracion;
	private String titulo;
	private long fecha;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private Manitas manitas;
	
	public Opinion(){
	}
	public Opinion(Cliente cliente, Manitas manitas, Double valoracion, String comentario, String titulo){
		this.cliente=cliente;
		this.manitas=manitas;
		this.comentario=comentario;
		this.valoracion=valoracion;
		this.titulo=titulo;
		this.fecha=System.currentTimeMillis();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Double getValoracion() {
		return valoracion;
	}
	public void setValoracion(Double valoracion) {
		this.valoracion = valoracion;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public Manitas getManitas() {
		return manitas;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public void setManitas(Manitas manitas) {
		this.manitas = manitas;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getFecha() {
		return new Date(fecha);
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	
}

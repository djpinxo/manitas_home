package com.manitas_home.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long>{
	public <Mensaje>List findByRemitenteAndDestinatario(String remitente,String destinatario);
	public <Mensaje>List findByRemitenteAndDestinatarioOrderByFechaDesc(String remitente,String destinatario);
	public <Mensaje>List findByDestinatario(String destinatario);
	public <Mensaje>List findByRemitente(String remitente);
	public <Mensaje>List findByDestinatarioAndLeido(String destinatario,boolean leido);
	public <Mensaje>List findByDestinatarioOrderByFechaDesc(String destinatario);
	public int countByDestinatarioAndLeido(String destinatario,boolean leido);
	public int countByDestinatarioAndRemitenteAndLeido(String destinatario,String remitente,boolean leido);
}

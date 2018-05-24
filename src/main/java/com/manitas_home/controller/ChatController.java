package com.manitas_home.controller;



import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Mensaje;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;
@Controller
public class ChatController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private MensajeRepository RMensaje;
	
	@GetMapping("/chat/contactos")
	public String contactos(HttpSession session,ModelMap m) {
		if(session.getAttribute("user")!=null){
			List <Mensaje> mensajes = RMensaje.findByDestinatario(((Usuario)session.getAttribute("user")).getEmail());
			ArrayList <Mensaje> emails = new <Mensaje> ArrayList();
			ArrayList <String> correos = new <String> ArrayList();
			ArrayList <Usuario> contactos = new <Usuario> ArrayList();
			ArrayList <String> sinleer = new <String> ArrayList();
			for (int i=mensajes.size()-1;i>=0;i--){
				if(!correos.contains(mensajes.get(i).getRemitente())){
					correos.add(mensajes.get(i).getRemitente());
					emails.add(mensajes.get(i));
				}
			}
			for (int i=0;i<emails.size();i++){
				String remitente=emails.get(i).getRemitente();
				Integer total=RMensaje.countByDestinatarioAndRemitenteAndLeido(((Usuario)session.getAttribute("user")).getEmail(),remitente, false);
				sinleer.add(total.toString());
				if(RAdministrador.findOneByEmail(remitente)!=null)
					contactos.add(RAdministrador.findOneByEmail(remitente));
				else if(RManitas.findOneByEmail(remitente)!=null)
					contactos.add(RManitas.findOneByEmail(remitente));
				else if(RCliente.findOneByEmail(remitente)!=null)
					contactos.add(RCliente.findOneByEmail(remitente));
			}
			ArrayList datos=new ArrayList();
			for(int i=0;i<contactos.size();i++){
				ArrayList dato=new ArrayList();
				dato.add(contactos.get(i));
				dato.add(sinleer.get(i));
				dato.add(emails.get(i));
				datos.add(dato);
			}
			m.put("datos",datos);
		}
		m.put("view","chat/contactos");
		m.put("usuarioactivo", session.getAttribute("user"));
		if(session.getAttribute("user")!=null)
		m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		return (session.getAttribute("user")!=null)?"views/_t/main":"redirect:/login/login";
	}
	@GetMapping("/chat/conversacion")//TODO modificar
	public String coversacion(@RequestParam("email")String emailremitente,HttpSession session,ModelMap m) {
		if(session.getAttribute("user")!=null){
			List <Mensaje> mensajesRecibidos = RMensaje.findByRemitenteAndDestinatarioOrderByFechaDesc(emailremitente,((Usuario)session.getAttribute("user")).getEmail());
			List <Mensaje> mensajesEnviados = RMensaje.findByRemitenteAndDestinatarioOrderByFechaDesc(((Usuario)session.getAttribute("user")).getEmail(),emailremitente);
			ArrayList <Mensaje> mensajes = new ArrayList();
			boolean fin=false;
			int env=0;
			int rec=0;
			while(!fin){
				if(mensajesEnviados.size()<=env&&mensajesRecibidos.size()<=rec){
					fin=true;
				}
				else if(mensajesEnviados.size()<=env){
					if(!mensajes.contains(mensajesRecibidos.get(rec)))
						mensajes.add(mensajesRecibidos.get(rec));
					rec++;
				}
				else if(mensajesRecibidos.size()<=rec){
					if(!mensajes.contains(mensajesEnviados.get(env)))
						mensajes.add(mensajesEnviados.get(env));
					env++;
				}
				else if(mensajesEnviados.get(env).getFecha().getTime()>mensajesRecibidos.get(rec).getFecha().getTime()){
					if(!mensajes.contains(mensajesEnviados.get(env)))
						mensajes.add(mensajesEnviados.get(env));
					env++;
				}
				else if(mensajesEnviados.get(env).getFecha().getTime()<mensajesRecibidos.get(rec).getFecha().getTime()){
					if(!mensajes.contains(mensajesRecibidos.get(rec)))
						mensajes.add(mensajesRecibidos.get(rec));
					rec++;
				}
				else{
					if(!mensajes.contains(mensajesEnviados.get(env)))
						mensajes.add(mensajesEnviados.get(env));
					env++;
				}
			}
			ThreadMarcarMensajesLeido hilo=new ThreadMarcarMensajesLeido(RMensaje,mensajesRecibidos);
			hilo.start();
			m.put("mensajes", mensajes);
		}
		m.put("usuarioactivo", session.getAttribute("user"));
		if(session.getAttribute("user")!=null)
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		m.put("view","chat/conversacion");
		return (session.getAttribute("user")!=null)?"views/_t/main":"redirect:/login/login";
	}
	
	
	
}

package com.manitas_home.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Administrador;
import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Mensaje;
import com.manitas_home.domain.Usuario;
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class MensajeController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private MensajeRepository RMensaje;
	
	@GetMapping("/mensaje/crear")
	public String crear(@RequestParam(value="emaildestinatario",defaultValue="")String emaildestinatario,@RequestParam(value="anonimo",defaultValue="false")boolean anonimo,HttpSession session,ModelMap m) {
		if (session.getAttribute("tipo") != null && session.getAttribute("user") != null) {
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario) session.getAttribute("user")).getEmail(), false));
			if (!emaildestinatario.equals(""))
				m.put("emaildestinatario", emaildestinatario + ";");
			m.put("view", "mensaje/crear");
		}
		else if(anonimo){
			m.put("anonimo", anonimo);
			m.put("emaildestinatario", "administradores;");
			m.put("view","mensaje/crear");
		}
		return ((session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null))?"views/_t/main":"redirect:/mensaje/listar";
	}
	@PostMapping("/mensaje/crear")
	public String crear(@RequestParam(value="emaildestinatario",defaultValue="")String emaildestinatario,@RequestParam(value="mensaje",defaultValue="")String mensaje,@RequestParam(value="anonimo",defaultValue="false")boolean anonimo,@RequestParam(value="contactoanonimo",defaultValue="")String contactoanonimo,HttpSession session) {
		String emailremitente =null;
		String[]destinatarios=new String[0];
		ArrayList <String> emailsdestinatarios=new ArrayList <String> ();
		mensaje=mensaje.trim();
		if(!mensaje.equals("")){
			contactoanonimo=contactoanonimo.trim();
			if(!contactoanonimo.equals("")&&session.getAttribute("tipo")==null&&session.getAttribute("user")==null){
				emailremitente="anonimo";
				mensaje="Forma de contacto: "+contactoanonimo+"\n"+"Mensaje: "+mensaje;
				List <Administrador> administradores=RAdministrador.findAll();
				for(Administrador admin:administradores){
					emailsdestinatarios.add(admin.getEmail());
				}
			}
			else if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
				emailremitente=((Usuario)session.getAttribute("user")).getEmail();
				if(emaildestinatario.toLowerCase().contains("todos")&&session.getAttribute("tipo").equals("administrador")){
					List <Administrador> administradores=RAdministrador.findAll();
					List <Cliente> clientes=RCliente.findAll();
					List <Manitas> manitas=RManitas.findAll();
					for(Administrador admin:administradores){
						emailsdestinatarios.add(admin.getEmail());
					}
					for(Cliente clien:clientes){
						emailsdestinatarios.add(clien.getEmail());
					}
					for(Manitas mani:manitas){
						emailsdestinatarios.add(mani.getEmail());
					}
				}
				else{
					if(emaildestinatario.toLowerCase().contains("clientes")&&session.getAttribute("tipo").equals("administrador")){
						List <Cliente> clientes=(List<Cliente>) RCliente.findAll();
						for(Cliente clien:clientes){
							emailsdestinatarios.add(clien.getEmail());
						}
					}
					if(emaildestinatario.toLowerCase().contains("manitas")&&session.getAttribute("tipo").equals("administrador")){
						List <Manitas> manitas=(ArrayList<Manitas>) RManitas.findAll();
						for(Manitas mani:manitas){
							emailsdestinatarios.add(mani.getEmail());
						}
					}
					if(emaildestinatario.toLowerCase().contains("administradores")){
						List <Administrador> administradores=(List<Administrador>) RAdministrador.findAll();
						for(Administrador admin:administradores){
							emailsdestinatarios.add(admin.getEmail());
						}
					}
					String[] arraydest = emaildestinatario.split(";");
					for(int i=0;i<arraydest.length;i++){
						arraydest[i]=arraydest[i].trim();
						if(!emailsdestinatarios.contains(arraydest[i])&&!arraydest[i].toLowerCase().equals("todos")&&!arraydest[i].toLowerCase().equals("clientes")&&!arraydest[i].toLowerCase().equals("manitas")&&!arraydest[i].toLowerCase().equals("administradores")){
							emailsdestinatarios.add(arraydest[i]);
						}
					}
				}
			}
			if(emailremitente!=null&&!emailsdestinatarios.isEmpty()){
				destinatarios=emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
				ThreadEnviarMensajes hilo=new ThreadEnviarMensajes(destinatarios,RMensaje,emailremitente,mensaje);
				hilo.start(messagingTemplate);
			}
		}
		return "redirect:/chat/contactos";
	}
	//TODO message para ws
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	@MessageMapping("/mensaje/crear/{emaildestinatario}")
	public void crear(String message, SimpMessageHeaderAccessor  headerAccessor,@DestinationVariable String emaildestinatario) throws Exception {
		HttpSession session=(HttpSession) headerAccessor.getSessionAttributes().get("HttpSession");
		/*delay anti spam*/if(((HttpSession) headerAccessor.getSessionAttributes().get("HttpSession")).getAttribute("ultimomensaje")==null||((int)((HttpSession) headerAccessor.getSessionAttributes().get("HttpSession")).getAttribute("ultimomensaje"))<((int)System.currentTimeMillis()/1000)){
			((HttpSession) headerAccessor.getSessionAttributes().get("HttpSession")).setAttribute("ultimomensaje",((int) System.currentTimeMillis() / 1000));
			String emailremitente = null;
			String[] destinatarios = new String[0];
			ArrayList<String> emailsdestinatarios = new ArrayList<String>();
			message = message.trim();
			if (!message.equals("") && session.getAttribute("tipo") != null && session.getAttribute("user") != null) {
				emailremitente = ((Usuario) session.getAttribute("user")).getEmail();
				if (emaildestinatario.toLowerCase().contains("todos")
						&& session.getAttribute("tipo").equals("administrador")) {
					List<Administrador> administradores = RAdministrador.findAll();
					List<Cliente> clientes = RCliente.findAll();
					List<Manitas> manitas = RManitas.findAll();
					for (Administrador admin : administradores) {
						emailsdestinatarios.add(admin.getEmail());
					}
					for (Cliente clien : clientes) {
						emailsdestinatarios.add(clien.getEmail());
					}
					for (Manitas mani : manitas) {
						emailsdestinatarios.add(mani.getEmail());
					}
				}
				else {
					if (emaildestinatario.toLowerCase().contains("clientes")
							&& session.getAttribute("tipo").equals("administrador")) {
						List<Cliente> clientes = (List<Cliente>) RCliente.findAll();
						for (Cliente clien : clientes) {
							emailsdestinatarios.add(clien.getEmail());
						}
					}
					if (emaildestinatario.toLowerCase().contains("manitas")
							&& session.getAttribute("tipo").equals("administrador")) {
						List<Manitas> manitas = (ArrayList<Manitas>) RManitas.findAll();
						for (Manitas mani : manitas) {
							emailsdestinatarios.add(mani.getEmail());
						}
					}
					if (emaildestinatario.toLowerCase().contains("administradores")) {
						List<Administrador> administradores = (List<Administrador>) RAdministrador.findAll();
						for (Administrador admin : administradores) {
							emailsdestinatarios.add(admin.getEmail());
						}
					}
					String[] arraydest = emaildestinatario.split(";");
					for (int i = 0; i < arraydest.length; i++) {
						arraydest[i] = arraydest[i].trim();
						if (!emailsdestinatarios.contains(arraydest[i]) && !arraydest[i].toLowerCase().equals("todos")&& !arraydest[i].toLowerCase().equals("clientes")&& !arraydest[i].toLowerCase().equals("manitas")&& !arraydest[i].toLowerCase().equals("administradores")) {
							emailsdestinatarios.add(arraydest[i]);
						}
					}
					if (emailremitente != null && !emailsdestinatarios.isEmpty()) {
						destinatarios = emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
						ThreadEnviarMensajes hilo = new ThreadEnviarMensajes(destinatarios, RMensaje, emailremitente,
								message);
						hilo.start(messagingTemplate);
					}
				}
			}
		}
	}
	//TODO fin mensaje ws
	@GetMapping("/mensaje/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")&&session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
			m.put("mensajes",RMensaje.findByDestinatarioOrderByFechaDesc(((Usuario)session.getAttribute("user")).getEmail()));
			return "xml/mensaje/listar";
		}
		else{
		if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
			m.put("mensajes",RMensaje.findByDestinatarioOrderByFechaDesc(((Usuario)session.getAttribute("user")).getEmail()));
			m.put("view","mensaje/listar");
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		}
		return (session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null)?"views/_t/main":"redirect:/login/login";
		}
	}
	@GetMapping("/mensaje/ver")
	public String ver(@RequestParam(value="id" , defaultValue="")Long idmensaje,HttpSession session,ModelMap m) {
		if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null&&idmensaje!=null){
			Mensaje men=RMensaje.findOne(idmensaje);
			String emaildestinatario=null;
			emaildestinatario=((Usuario)session.getAttribute("user")).getEmail();
			if(men!=null&&men.getDestinatario().equals(emaildestinatario)){
				men.setLeido(true);
				m.put("mensaje",men);
				RMensaje.save(men);
			}
			else m.put("mensaje",null);
			m.put("view","mensaje/ver");
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		}
		return (session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null)?((m.get("mensaje")!=null)?"views/_t/main":"redirect:/mensaje/listar"):"redirect:/login/login";
	}
	
	
}
class ThreadEnviarMensajes extends Thread{
	private String[]destinatarios;
	private MensajeRepository RMensaje;
	private String emailremitente;
	private String mensaje;
	private SimpMessageSendingOperations messagingTemplate=null;
	
	public ThreadEnviarMensajes(String[]destinatarios,MensajeRepository RMensaje,String emailremitente,String mensaje) {
		super();
		this.destinatarios=destinatarios;
		this.RMensaje=RMensaje;
		this.emailremitente=emailremitente;
		this.mensaje=mensaje;
	}
	public void run(){
		long horamensaje=System.currentTimeMillis();
		for(int i=0;i<destinatarios.length;i++){
				Mensaje email=new Mensaje(emailremitente,destinatarios[i].trim(),mensaje);
				email.setFecha(horamensaje);
				RMensaje.save(email);
				if(messagingTemplate!=null){
					messagingTemplate.convertAndSend("/topic/conversacion/"+funcionstart.suscriptionCoder(emailremitente,destinatarios[i].trim()), email);
				}
			}
		System.out.println("insertados "+destinatarios.length+" mensajes nuevos");
	}
	public void start(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate=messagingTemplate;
		super.start();
	}
	
}
class ThreadModificarMensajes extends Thread{
	private MensajeRepository RMensaje;
	private String emailnuevo;
	private String emailantiguo;
	
	public ThreadModificarMensajes(MensajeRepository RMensaje,String emailnuevo,String emailantiguo) {
		super();
		this.RMensaje=RMensaje;
		this.emailnuevo=emailnuevo;
		this.emailantiguo=emailantiguo;
	}
	public void run(){
		List emails = RMensaje.findByDestinatario(emailantiguo);
		for(int i=0;i<emails.size();i++){
			((Mensaje)emails.get(i)).setDestinatario(emailnuevo);
		}
		RMensaje.save(emails);
		emails=RMensaje.findByRemitente(emailantiguo);
		for(int i=0;i<emails.size();i++){
			((Mensaje)emails.get(i)).setRemitente(emailnuevo);
		}
		RMensaje.save(emails);
		System.out.println("todos los emails de "+emailantiguo+" han sido renombrados al user "+emailnuevo);
	}
}
class ThreadMarcarMensajesLeido extends Thread{
	private MensajeRepository RMensaje;
	private List <Mensaje> mensajes;
	
	public ThreadMarcarMensajesLeido(MensajeRepository RMensaje,List <Mensaje> mensajes) {
		super();
		this.RMensaje=RMensaje;
		this.mensajes=mensajes;
	}
	public void run(){
		for(int i=0;i<this.mensajes.size();i++){
			if(!this.mensajes.get(i).isLeido()){
				this.mensajes.get(i).setLeido(true);
				RMensaje.save(this.mensajes.get(i));
			}
		}
	}
}
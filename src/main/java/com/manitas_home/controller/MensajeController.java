package com.manitas_home.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
			m.put("usuarioemails",
					RMensaje.countByDestinatarioAndLeido(((Usuario) session.getAttribute("user")).getEmail(), false));
			if (!emaildestinatario.equals(""))
				m.put("emaildestinatario", emaildestinatario + ";");
			m.put("view", "mensaje/crear");
		}
		else if(anonimo){
			m.put("anonimo", anonimo);
			m.put("emaildestinatario", "administradores;");
			m.put("view","mensaje/crear");
		}
		return ((session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null)||anonimo)?"views/_t/main":"redirect:/mensaje/listar";
	}
	@PostMapping("/mensaje/crear")//TODO modificar
	public String crear(@RequestParam("emaildestinatario")String emaildestinatario,@RequestParam("mensaje")String mensaje,@RequestParam(value="anonimo",defaultValue="false")boolean anonimo,HttpSession session) {
		if((session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null)||anonimo){
			String emailremitente =null;
			String[]destinatarios=new String[0];
			if(emaildestinatario.toLowerCase().contains("todos")&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
				emailremitente =(((Administrador)session.getAttribute("user")).getEmail());
				ArrayList <String> emailsdestinatarios=new ArrayList <String> ();
				/*long inicio=System.nanoTime();
				System.out.println(inicio);
				ThreadEnviarMensajes hilo1=new ThreadEnviarMensajes(RCliente);
				hilo1.start();
				ThreadEnviarMensajes hilo2=new ThreadEnviarMensajes(RAdministrador);
				hilo2.start();
				ThreadEnviarMensajes hilo3=new ThreadEnviarMensajes(RManitas);
				hilo3.start();
				while(hilo1.getState().toString().equals("RUNNABLE")||hilo2.getState().toString().equals("RUNNABLE")||hilo3.getState().toString().equals("RUNNABLE"));
				ArrayList <Administrador> administrador=hilo2.getAdministrador();
				ArrayList <Cliente> clientes=hilo1.getClientes();
				ArrayList <Manitas> manitas=hilo3.getManitas();
				hilo1=null;
				hilo2=null;
				hilo3=null;
				long hilos=System.nanoTime()-inicio;
				System.out.println(hilos);
				inicio=System.nanoTime();
				System.out.println(inicio);*/
				ArrayList <Administrador> administrador=(ArrayList<Administrador>) RAdministrador.findAll();
				ArrayList <Cliente> clientes=(ArrayList<Cliente>) RCliente.findAll();
				ArrayList <Manitas> manitas=(ArrayList<Manitas>) RManitas.findAll();
				/*long normal=System.nanoTime()-inicio;
				System.out.println(normal);
				System.out.println(hilos-normal);
				*/
				for(int i=0;i<clientes.size();i++)
					emailsdestinatarios.add(clientes.get(i).getEmail());
				for(int i=0;i<administrador.size();i++)
					emailsdestinatarios.add(administrador.get(i).getEmail());
				for(int i=0;i<manitas.size();i++)
					emailsdestinatarios.add(manitas.get(i).getEmail());
				destinatarios= emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
				
			}
			else if((session.getAttribute("tipo")!=null&&emaildestinatario.toLowerCase().contains("administradores"))||anonimo){
				if(anonimo)
					emailremitente="anonimo";
				else
					emailremitente=((Usuario)session.getAttribute("user")).getEmail();
				ArrayList <String> emailsdestinatarios=new ArrayList <String> ();
				ArrayList <Administrador> administrador=(ArrayList<Administrador>) RAdministrador.findAll();
				for(int i=0;i<administrador.size();i++)
					emailsdestinatarios.add(administrador.get(i).getEmail());
				destinatarios= emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
			}
			else if(emaildestinatario.toLowerCase().contains("cliente")&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
				emailremitente=((Usuario)session.getAttribute("user")).getEmail();
				ArrayList <String> emailsdestinatarios=new ArrayList <String> ();
				ArrayList <Cliente> clientes=(ArrayList<Cliente>) RCliente.findAll();
				for(int i=0;i<clientes.size();i++)
					emailsdestinatarios.add(clientes.get(i).getEmail());
				destinatarios= emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
			}
			else if(emaildestinatario.toLowerCase().contains("manitas")&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
				emailremitente=((Usuario)session.getAttribute("user")).getEmail();
				ArrayList <String> emailsdestinatarios=new ArrayList <String> ();
				ArrayList <Manitas> manitas=(ArrayList<Manitas>) RManitas.findAll();
				for(int i=0;i<manitas.size();i++)
					emailsdestinatarios.add(manitas.get(i).getEmail());
				destinatarios= emailsdestinatarios.toArray(new String[emailsdestinatarios.size()]);
			}
			else if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
				destinatarios=emaildestinatario.split(";");
				emailremitente=((Usuario)session.getAttribute("user")).getEmail();
			}
			ThreadEnviarMensajes hilo=new ThreadEnviarMensajes(destinatarios,RMensaje,emailremitente,mensaje);
			hilo.start();
		}
		return "redirect:/mensaje/listar";
	}
	@GetMapping("/mensaje/listar")
	public String listar(HttpSession session,ModelMap m) {
		if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
			m.put("mensajes",RMensaje.findByDestinatario(((Usuario)session.getAttribute("user")).getEmail()));
			m.put("view","mensaje/listar");
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		}
		return (session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null)?"views/_t/main":"redirect:/login/login";
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
	//private int accion;
	private String[]destinatarios;
	private MensajeRepository RMensaje;
	private String emailremitente;
	private String mensaje;
	/*private ArrayList <Cliente> clientes;
	private ClienteRepository RCliente;
	private ArrayList <Administrador> administrador;
	private AdministradorRepository RAdministrador;
	private ArrayList <Manitas> manitas;
	private ManitasRepository RManitas;*/
	
	public ThreadEnviarMensajes(String[]destinatarios,MensajeRepository RMensaje,String emailremitente,String mensaje) {
		super();
		this.destinatarios=destinatarios;
		this.RMensaje=RMensaje;
		this.emailremitente=emailremitente;
		this.mensaje=mensaje;
		//this.accion=1;
	}
	/*public ThreadEnviarMensajes(ClienteRepository RCliente){
		super();
		this.RCliente=RCliente;
		this.accion=2;
	}
	public ThreadEnviarMensajes(AdministradorRepository RAdministrador){
		super();
		this.RAdministrador=RAdministrador;
		this.accion=3;
	}
	public ThreadEnviarMensajes(ManitasRepository RManitas){
		super();
		this.RManitas=RManitas;
		this.accion=4;
	}
	public void run() {
		switch (accion) {
		case 1:
			guardarMensajes();
			break;
		case 2:
			selectClientes();
			break;
		case 3:
			selectAdministradores();
			break;
		case 4:
			selectManitas();
			break;
		}
	}
	private void selectClientes(){
		this.clientes=(ArrayList<Cliente>) RCliente.findAll();
	}
	private void selectAdministradores(){
		this.administrador=(ArrayList<Administrador>) RAdministrador.findAll();
	}
	private void selectManitas(){
		this.manitas=(ArrayList<Manitas>) RManitas.findAll();
	}
	private void guardarMensajes(){
		long horamensaje=System.currentTimeMillis();
		for(int i=0;i<destinatarios.length;i++)
			if(!destinatarios[i].toLowerCase().trim().equals("todos")){
				Mensaje email=new Mensaje(emailremitente,destinatarios[i].trim(),mensaje);
				email.setFecha(horamensaje);
				RMensaje.save(email);
			}
	}
	
	public ArrayList<Cliente> getClientes() {
		return clientes;
	}
	public ArrayList<Administrador> getAdministrador() {
		return administrador;
	}
	public ArrayList<Manitas> getManitas() {
		return manitas;
	}
	*/
	public void run(){
		long horamensaje=System.currentTimeMillis();
		for(int i=0;i<destinatarios.length;i++)
			if(!destinatarios[i].toLowerCase().trim().equals("todos")){
				Mensaje email=new Mensaje(emailremitente,destinatarios[i].trim(),mensaje);
				email.setFecha(horamensaje);
				RMensaje.save(email);
			}
	}
}

package com.manitas_home.controller;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Administrador;
import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Opinion;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;
import com.manitas_home.repositories.OpinionRepository;


@Controller
public class ClienteController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private MensajeRepository RMensaje;
	@Autowired
	private OpinionRepository ROpinion;
	
	@GetMapping("/cliente/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
			if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador"))
				m.put("clientes",RCliente.findAll());
			return "xml/cliente/listar";
		}
		else {
			if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
				m.put("usuarioactivo", (Usuario)session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("clientes", RCliente.findAll());
				m.put("view","cliente/listar");
				return "views/_t/main";
			}
			else return "redirect:/login/login";
		}
	}
	@GetMapping("/cliente/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {//TODO probar
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Cliente")) id=((Cliente)session.getAttribute("user")).getId();
		Cliente clien=null;
		if(id!=null)
		clien=RCliente.findOne(id);
		if(clien==null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&((Usuario)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
		if(clien!=null&&permisos(id,session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("view","cliente/modificar");
				m.put("cliente", clien);
				return "views/_t/main";
			}
		else
			return "redirect:/cliente/listar";
	}
	@PostMapping("/cliente/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="nombre", defaultValue="")String nombre ,@RequestParam(value="apellidos", defaultValue="")String apellidos ,@RequestParam(value="telefono", defaultValue="")String telefono ,@RequestParam(value="email", defaultValue="")String email ,@RequestParam(value="coordenadas", defaultValue="")String direccion ,@RequestParam(value="password", defaultValue="")String password,@RequestParam(value="passwordactualhash", defaultValue="")String passwordactual,HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		nombre=nombre.trim();
		email=email.trim();
		password=password.trim();
		apellidos=apellidos.trim();
		telefono=telefono.trim();
		direccion=direccion.trim();
		if(id!=null&&!nombre.equals("")&&!email.equals("")&&password.length()==254&&!apellidos.equals("")&&!telefono.equals("")&&!direccion.equals("")&& Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü])+[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)&& Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", apellidos)&& Pattern.matches("^[9|6]{1}([\\d]{2}[-|\\s]*){3}[\\d]{2}$", telefono)&&permisos(id,session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			Cliente cliente=RCliente.findOne(id);
			if(cliente==null&&session.getAttribute("tipo").equals("cliente")&&((Usuario)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			m.put("resultado", "OK");
			if(cliente!=null){
			email=email.toLowerCase();
			if(!cliente.getEmail().equals(email)){
				if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
					ThreadModificarMensajes hilo1 = new ThreadModificarMensajes(RMensaje,email,cliente.getEmail());
					hilo1.start();
					cliente.setEmail(email);
				}
				else m.put("resultado", "ERROR - El email esta ya en uso.");
			}
			if(!password.equals("250e9ad7d417a14a75a46c27601ca89898554ae68dc76417eb3d1476fe24e6cd67b02858640665b13566dd2994b71cb64004cd0d8bdda30b595a3f40271eaff00df2a06d62ffd749c26d63d2844fcad907b6821c0e4a1c2c885760ba10cbb4adefc66e4c42fb0b28fb7c632e9f0894f2493552d9ff599e683c660b19b129b3"))
				cliente.setPassword(password);
			cliente.setNombre(nombre.toLowerCase());
			cliente.setApellidos(apellidos.toLowerCase());
			cliente.setTelefono(telefono);
			cliente.setDireccion(direccion);
			RCliente.save(cliente);
			if(session.getAttribute("tipo")!=null&&!session.getAttribute("tipo").equals("administrador")) LoginController.logoutStatic(session);
			}
			else m.put("resultado", "ERROR - El cliente no existe.");
		}
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")) return "views/_t/resultado";
		else return "redirect:/cliente/listar";
	}
	@PostMapping("/cliente/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="passwordactualhash", defaultValue="")String passwordactual,HttpSession session,ModelMap m) {//TODO probar
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Cliente")) id=((Cliente)session.getAttribute("user")).getId();
		if(permisos(id,session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){//TODO si se cambia la modal sino eliminar el password 
			Cliente cliente=RCliente.findOne(id);
			if(cliente==null&&session.getAttribute("tipo").equals("cliente")&&((Usuario)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			if(cliente!=null){
				for(Opinion op:cliente.getOpiniones())
					ROpinion.delete(op);
				cliente.getOpiniones().clear();
				RCliente.delete(id);
				if(session.getAttribute("tipo")!=null&&!session.getAttribute("tipo").equals("administrador")) LoginController.logoutStatic(session);
			}
		}
		return "redirect:/cliente/listar";
	}
	private boolean permisos(Long id,HttpSession s){
		boolean salida=false;
		if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user"))!=null)
			salida=true;
		else if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("cliente")&&s.getAttribute("user")!=null&&((Cliente)s.getAttribute("user")).getId().equals(id))
			salida=true;
		return salida;
	}
}

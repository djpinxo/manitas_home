package com.manitas_home.controller;

import java.util.ArrayList;
import java.util.Collection;

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
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
			m.put("clientes",RCliente.findAll());
			return "xml/cliente/listar";
		}
		else {
		if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
			m.put("usuarioactivo", (Usuario)session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("clientes", RCliente.findAll());
			m.put("view","cliente/listar");
		}
		return (session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador"))?"views/_t/main":"redirect:/login/login";
		}
	}
	@GetMapping("/cliente/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Cliente")) id=((Cliente)session.getAttribute("user")).getId();
		if(permisos(id,session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("view","cliente/modificar");
				m.put("cliente", RCliente.findOne(id));
			}
		
		return permisos("views/_t/main","redirect:/cliente/listar",id,session);
	}
	@PostMapping("/cliente/modificar")
	public String modificar(@RequestParam("id")Long id,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("coordenadas")String direccion ,@RequestParam("password")String password,@RequestParam("passwordactualhash")String passwordactual,HttpSession session) {
		if(permisos(id,session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			Cliente cliente=RCliente.findOne(id);
			if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
				ThreadModificarMensajes hilo1 = new ThreadModificarMensajes(RMensaje,email,cliente.getEmail());
				hilo1.start();
				cliente.setEmail(email);
			}
			if(!password.equals("250e9ad7d417a14a75a46c27601ca89898554ae68dc76417eb3d1476fe24e6cd67b02858640665b13566dd2994b71cb64004cd0d8bdda30b595a3f40271eaff00df2a06d62ffd749c26d63d2844fcad907b6821c0e4a1c2c885760ba10cbb4adefc66e4c42fb0b28fb7c632e9f0894f2493552d9ff599e683c660b19b129b3"))
				cliente.setPassword(password);
			cliente.setNombre(nombre);
			cliente.setApellidos(apellidos);
			cliente.setTelefono(telefono);
			cliente.setDireccion(direccion);
			RCliente.save(cliente);
		}
		return ((session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&((Cliente)session.getAttribute("user")).getId().equals(id)))?"redirect:/login/logout":"redirect:/cliente/listar";
	}
	@GetMapping("/cliente/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Cliente")) id=((Cliente)session.getAttribute("user")).getId();
		if(permisos(id,session)){
			if(RCliente.exists(id)&&permisos(id,session)){
				Cliente cliente=RCliente.findOne(id);
				for(int i=0;i<cliente.getOpiniones().size();i++)
					ROpinion.delete(cliente.getOpiniones().get(i));
				RCliente.delete(id);
			}
		}
		return ((session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&((Cliente)session.getAttribute("user")).getId().equals(id)))?"redirect:/login/logout":"redirect:/cliente/listar";
	}
	
	
	private String permisos(String destinoOk,String destinoFail,Long id,HttpSession s){
		String salida=destinoFail;
		if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			salida=destinoOk;
		else if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("cliente")&&s.getAttribute("user")!=null&&((Cliente)s.getAttribute("user")).getId().equals(id))
			salida=destinoOk;
		return salida;
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

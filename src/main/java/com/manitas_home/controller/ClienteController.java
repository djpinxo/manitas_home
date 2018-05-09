package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Administrador;
import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;

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
	
	@GetMapping("/cliente/listar")
	public String listar(HttpSession session,ModelMap m){
		if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
			m.put("usuarioactivo", (Usuario)session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("clientes", RCliente.findAll());
			m.put("view","cliente/listar");
		}
		return (session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador"))?"views/_t/main":"redirect:/login/login";
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
	public String modificar(@RequestParam("id")Long id,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("coordenadas")String direccion ,@RequestParam("password")String password,HttpSession session) {
		if(permisos(id,session)){
			Cliente cliente=RCliente.findOne(id);
			if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null)
				cliente.setEmail(email);
			if(!password.equals("c2e5a90f5a957f5abf40377e72d0ad45594a7a257f678d5d6c4844194b86b3b3a61f733111346748c1f82629cd9c5763ba6b77f0d358fb5460bf111df785ffcd72844d1438792e8ea7566ba65f18b62ba1ba2012eef8ab917fab9ab4491b13e61aad97d902cc7ba3412e431fd8af9a66ea8366c86953a68ec3d2032fcee09e"))
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
			if(RCliente.exists(id)&&permisos(id,session))
				RCliente.delete(id);
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

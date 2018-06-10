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
import com.manitas_home.domain.Usuario;
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class AdministradorController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private MensajeRepository RMensaje;
		
	@GetMapping("/administrador/crear")
	public String crear(HttpSession session,ModelMap m) {//TODO probar
		if(permisosCrear(session)){
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("view","administrador/crear");
			return "views/_t/main";
		}
		else return "redirect:/administrador/listar";
	}
	@PostMapping("/administrador/crear")
	public String crear(@RequestParam(value="nombre", defaultValue="")String nombre ,@RequestParam(value="email", defaultValue="")String email ,@RequestParam(value="password", defaultValue="")String password,HttpSession session) {//TODO probar
		nombre=nombre.trim();
		email=email.trim();
		password=password.trim();
		if(!nombre.equals("")&&!email.equals("")&&password.length()==254&& Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)){
			if(permisosCrear(session)&&this.RAdministrador.findOneByEmail(email)==null&&this.RCliente.findOneByEmail(email)==null&&this.RManitas.findOneByEmail(email)==null)
				this.RAdministrador.save(new Administrador(nombre.toLowerCase(),email.toLowerCase(),password,!(funcionstart.getFirst(RAdministrador))));
		}
		return "redirect:/administrador/listar";
	}
	//TODO probar con usuario superadmin borrar sin parametro(resultado no exito)									-OK
	//TODO probar con usuario superadmin borrar con parametro id = a su usuario(resultado no exito)					-OK
	//TODO probar con usuario superadmin borrar con parametro id = otro id no existente(resultado no exito)			-OK
	//TODO probar con usuario superadmin borrar con parametro id = otro id existente(resultado exito)				-OK
	//TODO probar con usuario admin borrar sin parametro(resultado exito)											-OK
	//TODO probar con usuario admin borrar con parametro id = a su usuario(resultado exito)							-OK
	//TODO probar con usuario admin borrar con parametro id = otro id no existente(resultado no exito)				-OK
	//TODO probar con usuario admin borrar con parametro id = otro id admin(resultado no exito)						-OK
	//TODO probar con usuario admin borrar con parametro id = otro id superadmin(resultado no exito)				-OK
	//TODO probar con usuario otro user borrar sin parametro(resultado no exito)									-OK
	//TODO probar con usuario otro user borrar con parametro id = a su usuario(resultado no exito)					-OK
	//TODO probar con usuario otro user borrar con parametro id = otro id no existente(resultado no exito)			-OK
	//TODO probar con usuario otro user borrar con parametro id = otro id admin(resultado no exito)					-OK
	//TODO probar con usuario otro user borrar con parametro id = otro id superadmin(resultado no exito)			-OK
	@PostMapping("/administrador/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="passwordactualhash",defaultValue="")String passwordactual,HttpSession session,ModelMap m) {//TODO probar mejorable obteniedo primero eladmin a borrar en borrarotro pasar el admin entero (se ahorra una consulta en la bd
		if(permisos(session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			if(id==null) id=((Administrador)session.getAttribute("user")).getId();
			Administrador admin=RAdministrador.findOne(id);
			if(admin==null&&((Administrador)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			if(admin!=null&&(permisosBorrar(id,session)||permisosBorrarOtros(id,session))){
				RAdministrador.delete(id);
				if(session.getAttribute("user")!=null&&((Administrador)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			}
		}
		return "redirect:/administrador/listar";
	}
	@GetMapping("/administrador/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(permisos(session)){
			if(id==null) id=((Administrador)session.getAttribute("user")).getId();
			if(((Administrador)session.getAttribute("user")).getBorrable()&&!((Administrador)session.getAttribute("user")).getId().equals(id))
				id=((Administrador)session.getAttribute("user")).getId();
			Administrador admin=RAdministrador.findOne(id);
			if(admin==null&&((Administrador)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			if(admin!=null){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("view","administrador/modificar");
				m.put("administrador", admin);
				return "views/_t/main";
			}
			else{
				return "redirect:/administrador/listar";
			}
		}
		else return "redirect:/administrador/listar";
	}
	@PostMapping("/administrador/modificar")
	public String modificar(@RequestParam(value="idh", defaultValue="")Long id,@RequestParam(value="nombre", defaultValue="")String nombre ,@RequestParam(value="email", defaultValue="")String email ,@RequestParam(value="password", defaultValue="")String password,@RequestParam(value="passwordactualhash", defaultValue="")String passwordactual,HttpSession session) {//TODO probar
		nombre=nombre.trim();
		email=email.trim();
		password=password.trim();
		if(id!=null&&!nombre.equals("")&&!email.equals("")&&password.length()==254&& Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)&&permisos(session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			nombre=nombre.toLowerCase();
			email=email.toLowerCase();
			if(((Administrador)session.getAttribute("user")).getBorrable()&&!((Administrador)session.getAttribute("user")).getId().equals(id))
				id=((Administrador)session.getAttribute("user")).getId();
			Administrador administrador=RAdministrador.findOne(id);
			if(administrador==null&&((Administrador)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			if(administrador!=null){
				if(!administrador.getEmail().equals(email)&&RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
					ThreadModificarMensajes hilo1 = new ThreadModificarMensajes(RMensaje,email,administrador.getEmail());
					hilo1.start();
					administrador.setEmail(email);
				}
				administrador.setNombre(nombre);
				if(!password.equals("250e9ad7d417a14a75a46c27601ca89898554ae68dc76417eb3d1476fe24e6cd67b02858640665b13566dd2994b71cb64004cd0d8bdda30b595a3f40271eaff00df2a06d62ffd749c26d63d2844fcad907b6821c0e4a1c2c885760ba10cbb4adefc66e4c42fb0b28fb7c632e9f0894f2493552d9ff599e683c660b19b129b3"))
					administrador.setPassword(password);
			
				RAdministrador.save(administrador);
				if(session.getAttribute("user")!=null&&((Administrador)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			}
		}
		return "redirect:/administrador/listar";
	}
	@GetMapping("/administrador/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
			if(permisos(session)){
				m.put("administradores", RAdministrador.findAll());
				m.put("superadmin", !((Administrador)session.getAttribute("user")).getBorrable());
				m.put("emailactual", ((Usuario)session.getAttribute("user")).getEmail());
			}
			return "xml/administrador/listar";
		}
		else {
			if(permisos(session)){
				m.put("usuarioactivo", ((Administrador)session.getAttribute("user")));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("administradores", RAdministrador.findAll());
				m.put("view","administrador/listar");
				return "views/_t/main";
			}
			else return "redirect:/login/login";
		}
	}
	private boolean permisosCrear(HttpSession s){
		boolean salida=false;
		if(funcionstart.getFirst(RAdministrador))
			salida=true;
		else if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&!((Administrador)s.getAttribute("user")).getBorrable())
			salida=true;
		return salida;
	}
	private boolean permisosBorrar(Long id,HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user")).getId().equals(id)&&((Administrador)s.getAttribute("user")).getBorrable())
			salida=true;
		return salida;
	}
	private boolean permisosBorrarOtros(Long id,HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&!((Administrador)s.getAttribute("user")).getBorrable()&&RAdministrador.findOne(id).getBorrable())
				salida=true;
		return salida;
	}
	private boolean permisos(HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user"))!=null)
			salida=true;
		return salida;
	}
}

package com.manitas_home.controller;

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
	public String crear(HttpSession session,ModelMap m) {
		/*
		m.put("view","administrador/crear");
		String salida="";
		if(session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")&&((Administrador)session.getAttribute("user")).getBorrable())salida="redirect:/";
		else salida=funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
		return salida;*/
		m.put("usuarioactivo", session.getAttribute("user"));
		if(session.getAttribute("user")!=null)
		m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		m.put("view","administrador/crear");
		return permisosCrear("views/_t/main", "redirect:/administrador/listar",session);
	}
	@PostMapping("/administrador/crear")
	public String crear(@RequestParam("nombre")String nombre ,@RequestParam("email")String email ,@RequestParam("password")String password,HttpSession session) {
		/*
		if((session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")&&!((Administrador)session.getAttribute("user")).getBorrable())||this.RAdministrador.count()<=0)
		this.RAdministrador.save(new Administrador(nombre,email,password,(this.RAdministrador.count()>0)?true:false));
		return "redirect:/administrador/crear";
		*/
		nombre=nombre.toLowerCase();
		email=email.toLowerCase();
		if(permisosCrear(session)&&this.RAdministrador.findOneByEmail(email)==null&&this.RCliente.findOneByEmail(email)==null&&this.RManitas.findOneByEmail(email)==null){
			this.RAdministrador.save(new Administrador(nombre,email,password,!(funcionstart.getFirst(RAdministrador))));
		}
		return "redirect:/administrador/listar";
	}
	/*-------SUSTITUIDO POR UNICO
	@GetMapping("/administrador/borrar")
	public String borrar(HttpSession session,ModelMap m) {
		
		/*String salida="redirect:/";
		if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")){
			if(((Administrador)session.getAttribute("user")).getBorrable()){
					RAdministrador.delete(((Administrador)session.getAttribute("user")).getId());
					System.out.println("borrado el usuario "+((Administrador)session.getAttribute("user")).getNombre());
					salida="redirect:/login/logout";
			}
			else{
				salida="redirect:/administrador/crear";
				System.out.println("no se puede borrar el usuario "+((Administrador)session.getAttribute("user")).getNombre());
			}
		}*//*
		if(permisosBorrar(session)&&RAdministrador.exists(((Administrador)session.getAttribute("user")).getId()))
			RAdministrador.delete(((Administrador)session.getAttribute("user")).getId());
		return permisosBorrar("redirect:/login/logout","redirect:/administrador/listar",session);
	}*/
	
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
	@GetMapping("/administrador/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(permisos(session)){
			if(id==null) id=((Administrador)session.getAttribute("user")).getId();
			if(RAdministrador.exists(id)&&(permisosBorrar(id,session)||permisosBorrarOtros(id,session)))
			RAdministrador.delete(id);
		}
		return permisosBorrar("redirect:/login/logout","redirect:/administrador/listar",id,session);
	}
	/*-------SUSTITUIDO POR UNICO
	@PostMapping("/administrador/borrar")
	public String borrar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		if(permisosBorrarOtros(id,session))
			RAdministrador.delete(id);
		return "redirect:/administrador/listar";
	}*/
	@GetMapping("/administrador/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(permisos(session)){
			if(id==null) id=((Administrador)session.getAttribute("user")).getId();
			if(((Administrador)session.getAttribute("user")).getBorrable()&&!((Administrador)session.getAttribute("user")).getId().equals(id))
				id=((Administrador)session.getAttribute("user")).getId();
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("view","administrador/modificar");
			m.put("administrador", RAdministrador.findOne(id));
		}
		
		return permisos("views/_t/main","redirect:/administrador/listar",session);
	}
	@PostMapping("/administrador/modificar")
	public String modificar(@RequestParam("idh")Long id,@RequestParam("nombre")String nombre ,@RequestParam("email")String email ,@RequestParam("password")String password,@RequestParam("passwordactualhash")String passwordactual,HttpSession session) {
		if(permisos(session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			nombre=nombre.toLowerCase();
			email=email.toLowerCase();
			if(((Administrador)session.getAttribute("user")).getBorrable()&&!((Administrador)session.getAttribute("user")).getId().equals(id))
				id=((Administrador)session.getAttribute("user")).getId();
			Administrador administrador=RAdministrador.findOne(id);
			if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
				ThreadModificarMensajes hilo1 = new ThreadModificarMensajes(RMensaje,email,administrador.getEmail());
				hilo1.start();
				administrador.setEmail(email);
			}
			administrador.setNombre(nombre);
			if(!password.equals("250e9ad7d417a14a75a46c27601ca89898554ae68dc76417eb3d1476fe24e6cd67b02858640665b13566dd2994b71cb64004cd0d8bdda30b595a3f40271eaff00df2a06d62ffd749c26d63d2844fcad907b6821c0e4a1c2c885760ba10cbb4adefc66e4c42fb0b28fb7c632e9f0894f2493552d9ff599e683c660b19b129b3"))
				administrador.setPassword(password);
			
			RAdministrador.save(administrador);
		}
		return (session.getAttribute("user")!=null&&((Administrador)session.getAttribute("user")).getId().equals(id))?"redirect:/login/logout":"redirect:/administrador/listar";
	}
	@GetMapping("/administrador/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")&&permisos(session)){
			m.put("administradores", RAdministrador.findAll());
			m.put("superadmin", !((Administrador)session.getAttribute("user")).getBorrable());
			m.put("emailactual", ((Usuario)session.getAttribute("user")).getEmail());
			return "xml/administrador/listar";
		}
		else {
		if(permisos(session)){
			m.put("usuarioactivo", ((Administrador)session.getAttribute("user")));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("administradores", RAdministrador.findAll());
			m.put("view","administrador/listar");
		}
		return permisos("views/_t/main","redirect:/login/login",session);
		}
	}
	
	
	
	private String permisosCrear(String destinoOk,String destinoFail,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&!((Administrador)s.getAttribute("user")).getBorrable())
				salida=destinoOk;
		else if(funcionstart.getFirst(RAdministrador))
			salida=destinoOk;
		return salida;
	}
	private boolean permisosCrear(HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&!((Administrador)s.getAttribute("user")).getBorrable())
				salida=true;
		else if(funcionstart.getFirst(RAdministrador))
			salida=true;
		return salida;
	}
	private String permisosBorrar(String destinoOk,String destinoFail,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user")).getBorrable())
			salida=destinoOk;
		return salida;
	}
	private String permisosBorrar(String destinoOk,String destinoFail,Long id,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user")).getId().equals(id)&&((Administrador)s.getAttribute("user")).getBorrable())
			salida=destinoOk;
		return salida;
	}
	private boolean permisosBorrar(HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user")).getBorrable())
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
	private String permisos(String destinoOk,String destinoFail,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
				salida=destinoOk;
		return salida;
	}
	private boolean permisos(HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user"))!=null)
			salida=true;
		return salida;
	}
}

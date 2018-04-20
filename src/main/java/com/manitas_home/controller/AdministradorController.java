package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Administrador;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;

import funciones.funcionstart;

@Controller
public class AdministradorController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	
		
	@GetMapping("/administrador/crear")
	public String crear(HttpSession session,ModelMap m) {
		/*
		m.put("view","administrador/crear");
		String salida="";
		if(session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")&&((Administrador)session.getAttribute("user")).getBorrable())salida="redirect:/";
		else salida=funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
		return salida;*/
		m.put("usuarioactivo", session.getAttribute("user"));
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
		if(permisosCrear(session)&&this.RAdministrador.findByEmail(email)==null&&this.RCliente.findByEmail(email)==null&&this.RManitas.findByEmail(email)==null){
			this.RAdministrador.save(new Administrador(nombre,email,password,!(funcionstart.getFirst(RAdministrador))));
		}
		return "redirect:/administrador/listar";
	}
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
		}    */
		if(permisosBorrar(session)&&RAdministrador.exists(((Administrador)session.getAttribute("user")).getId()))
			RAdministrador.delete(((Administrador)session.getAttribute("user")).getId());
		return permisosBorrar("redirect:/login/logout","redirect:/administrador/listar",session);
	}
	@PostMapping("/administrador/borrar")
	public String borrar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		if(permisosBorrarOtros(id,session))
			RAdministrador.delete(id);
		return "redirect:/administrador/listar";
	}
	@GetMapping("/administrador/listar")
	public String listar(HttpSession session,ModelMap m) {
		if(permisos(session)){
			m.put("usuarioactivo", ((Administrador)session.getAttribute("user")));
			m.put("administradores", RAdministrador.findAll());
			m.put("view","administrador/listar");
		}
		return permisos("views/_t/main","redirect:/login/login",session);
	}
	//TODO
	
	
	
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
	private boolean permisosBorrar(HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user")).getBorrable())
			salida=true;
		return salida;
	}
	private boolean permisosBorrarOtros(Long id,HttpSession s){
		boolean salida=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&!((Administrador)s.getAttribute("user")).getBorrable())
			if(RAdministrador.findOne(id).getBorrable())
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
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			salida=true;
		return salida;
	}
}

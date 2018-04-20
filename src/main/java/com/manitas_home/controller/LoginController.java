package com.manitas_home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;

import funciones.funcionstart;

@Controller
public class LoginController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	
	@GetMapping("/login/login")
	public String login(HttpSession session,ModelMap m,HttpServletRequest r) {
			System.out.println(r.getRemoteHost());
			if(r.getAttribute("HTTP_X_REQUESTED_WITH")==null){
				System.out.println("peticion no ajax");
			}
			
			m.put("view","login/login");
			
			if(funcionstart.getFirst(RAdministrador)) System.out.println("primera vez sin admins");
		
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
			return (permisos("redirect:/",hayadmin("views/_t/main", "redirect:/administrador/crear"),session));
	}
	@PostMapping("/login/login")
	public String login(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
		//
		if(RCliente.findByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RCliente.findByEmailAndPassword(email, password));
			session.setAttribute("tipo","cliente");
		}
		else if(RManitas.findByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RManitas.findByEmailAndPassword(email, password));
			session.setAttribute("tipo","empleado");
		}
		else if(RAdministrador.findByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RAdministrador.findByEmailAndPassword(email, password));
			session.setAttribute("tipo","administrador");
		}
		
		return "redirect:/login/login";
	}
	@GetMapping("/login/logout")
	public String logout(HttpSession session) {
		session.setAttribute("user",null);
		session.setAttribute("tipo",null);
		
		return "redirect:/";
	}
	@GetMapping("/login/crear")
	public String crear(HttpSession session,ModelMap m) {
		
		m.put("view","login/crear");
		
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
		return (hayadmin("views/_t/main", "redirect:/administrador/crear"));
	}
	@PostMapping("/login/crear")//TODO modificar
	public String crear(@RequestParam("tipo")String tipo ,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("direccion")String direccion ,@RequestParam("password")String password,@RequestParam(value = "radio", defaultValue="")String radio ,HttpSession session) {
		if(session.getAttribute("user")!=null);
		else if(RCliente.findByEmail(email)==null&&RManitas.findByEmail(email)==null&&RAdministrador.findByEmail(email)==null){
			if(tipo.equals("cliente"))
				RCliente.save(new Cliente(nombre,apellidos,telefono,email,password,direccion));//TODO
			else if(tipo.equals("manitas"))
				RManitas.save(new Manitas(nombre,apellidos,telefono,email,password,direccion,Integer.parseInt(radio)));//TODO
		}
		return "redirect:/";
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
	private String permisos(String destinoOk,String destinoFail,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null)
				salida=destinoOk;
		return salida;
	}
}

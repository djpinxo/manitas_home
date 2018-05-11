package com.manitas_home.controller;

import java.util.ArrayList;

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
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;

@Controller
public class LoginController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private EmpleoRepository REmpleo;
	
	@GetMapping("/login/login")
	public String login(HttpSession session,ModelMap m,HttpServletRequest r) {
			System.out.println(r.getRemoteHost());
			if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
				System.out.println("peticion ajax");
			}
			
			m.put("view","login/login");
			
			if(funcionstart.getFirst(RAdministrador)) System.out.println("primera vez sin admins");
		
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
			return (permisos("redirect:/",hayadmin("views/_t/main", "redirect:/administrador/crear"),session));
	}
	@PostMapping("/login/login")
	public String login(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
		//
		if(RCliente.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RCliente.findOneByEmailAndPassword(email, password));
			session.setAttribute("tipo","cliente");
		}
		else if(RManitas.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RManitas.findOneByEmailAndPassword(email, password));
			session.setAttribute("tipo","manitas");
		}
		else if(RAdministrador.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RAdministrador.findOneByEmailAndPassword(email, password));
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
		m.put("empleos", REmpleo.findAll());
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
		return (permisos("redirect:/",hayadmin("views/_t/main", "redirect:/administrador/crear"),session));
	}
	@PostMapping("/login/crear")//TODO modificar añadir el empleo a manitas
	public String crear(@RequestParam("tipo")String tipo ,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("coordenadas")String direccion ,@RequestParam(value = "descripcion", defaultValue="")String descripcion,@RequestParam("password")String password,@RequestParam(value = "radio", defaultValue="10")String radio, @RequestParam(value = "idempleo", defaultValue="")ArrayList <Long> idsempleos ,HttpSession session) {
		if(session.getAttribute("user")!=null);
		else if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
			if(tipo.equals("cliente"))
				RCliente.save(new Cliente(nombre,apellidos,telefono,email,password,direccion));//TODO
			else if(tipo.equals("manitas")){
				Manitas manitas=new Manitas(nombre,apellidos,telefono,email,password,direccion,descripcion,Integer.parseInt(radio));
				for(int i=0;i<idsempleos.size();i++)
					manitas.getEmpleos().add(REmpleo.findOne(idsempleos.get(i)));
				RManitas.save(manitas);//TODO
			}
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

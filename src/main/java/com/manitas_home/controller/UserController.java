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
import com.manitas_home.domain.Manitas;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;

import com.manitas_home.funciones.funcionstart;
@Controller
public class UserController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	
	
	/*@GetMapping("/user/crear")
	public String crear(HttpSession session,ModelMap m) {
		
			m.put("view","crear/select");//carga la pagina por defecto
		
		return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
	}*/
	
	@GetMapping("/user/crear")
	public String crear(HttpSession session,ModelMap m) {
		
			m.put("view","user/crear");
		
		return funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
	}
	@PostMapping("/user/crear")//TODO modificar
	public String crear(@RequestParam("tipo")String tipo ,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("direccion")String direccion ,@RequestParam("password")String password,@RequestParam(value = "radio", defaultValue="")String radio ,HttpSession session) {
		if(session.getAttribute("user")!=null);
		else if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
			if(tipo.equals("cliente"))
				RCliente.save(new Cliente(nombre,apellidos,telefono,email,password,direccion));//TODO
			else if(tipo.equals("manitas"));
				//RManitas.save(new Manitas(nombre,apellidos,telefono,email,password,direccion,Integer.parseInt(radio)));//TODO
		}
		return "redirect:/";
	}
	
	
	
}

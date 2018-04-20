package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;

@Controller
public class ManitasController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	
	@GetMapping("/manitas/crear")
	public String crear(HttpSession session,ModelMap m) {
		
			m.put("view","manitas/crear");//carga la pagina por defecto
		
		return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
	}
}

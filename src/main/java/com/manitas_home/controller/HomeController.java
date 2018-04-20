package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manitas_home.repositories.AdministradorRepository;

import funciones.funcionstart;

@Controller
public class HomeController /*implements ErrorController*/{
	@Autowired
	private AdministradorRepository RAdministrador;
	
	////////////////////////////////////////////
	/*
	@RequestMapping(value="/error")
    public String error() {
		System.out.println();
        return "redirect:/";
    }
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
	*/
	////////////////////////////////
	@GetMapping("/")
	public String index(HttpSession session,ModelMap m) {
		
			m.put("view","home/index");//carga la pagina por defecto
			m.put("usuarioactivo", session.getAttribute("user"));
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
			return hayadmin("views/_t/main", "redirect:/administrador/crear");
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
}

package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.manitas_home.domain.Usuario;
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class HomeController{
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private MensajeRepository RMensaje;
	
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
			m.put("view","home/index");
			m.put("usuarioactivo", session.getAttribute("user"));
			if(session.getAttribute("user")!=null)
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			return hayadmin("views/_t/main", "redirect:/administrador/crear");
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
}

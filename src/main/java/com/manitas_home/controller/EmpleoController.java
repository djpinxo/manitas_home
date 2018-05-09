package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Empleo;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class EmpleoController {
	@Autowired
	private EmpleoRepository ERepository;
	@Autowired
	private CategoriaRepository CRepository;
	@Autowired
	private MensajeRepository RMensaje;
	
	@GetMapping("/empleo/crear")
	public String crear(HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		m.put("usuarioactivo", session.getAttribute("user"));
		m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		m.put("categorias", CRepository.findAll());
		m.put("view","empleo/crear");
		
		return permisos("views/_t/main","redirect:/empleo/listar",session);
	}
	@PostMapping("/empleo/crear")
	public String crear(@RequestParam("nombre")String nombre,@RequestParam("idcategoria")Long idcategoria,HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		if(permisos(session)&&ERepository.findOneByNombre(nombre)==null)
			ERepository.save(new Empleo(nombre,CRepository.findOne(idcategoria)));
		return "redirect:/empleo/listar";
	}
	@GetMapping("/empleo/modificar")
	public String modificar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		if(permisos(session)){
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("empleo", ERepository.findOne(id));
			m.put("categorias", CRepository.findAll());
			m.put("view","empleo/modificar");
		}
		return permisos("views/_t/main","redirect:/empleo/listar",session);
	}
	@PostMapping("/empleo/modificar")
	public String modificar(@RequestParam("id")Long id,@RequestParam("nombre")String nombre,@RequestParam("idcategoria")Long idcategoria,HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		if(permisos(session)){//no aceptamos mismo nombre con distinta categoria
			Empleo e=ERepository.findOne(id);
			if(ERepository.findOneByNombre(nombre)==null)
				e.setNombre(nombre);
			if(!e.getCategoria().getId().equals(idcategoria)){//si no cambia la categoria no hacemos la operacion de busqueda
				e.setCategoria(CRepository.findOne(idcategoria));
			}
			ERepository.save(e);
		}
		return "redirect:/empleo/listar";
	}
	@GetMapping("/empleo/listar")
	public String listar(HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		if (permisos(session)) {
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("empleos", ERepository.findAll());
			m.put("view", "empleo/listar");
		}
		return permisos("views/_t/main","redirect:/login/login",session);
	}
	@GetMapping("/empleo/borrar")
	public String borrar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository,ERepository);
		if(permisos(session)&&ERepository.exists(id))
			ERepository.delete(id);
		
		return "redirect:/empleo/listar";
	}
	
	private String permisos(String destinoOk,String destinoFail,HttpSession s){
		String pagina=destinoFail;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
					pagina=destinoOk;		
		return pagina;
	}
	private boolean permisos(HttpSession s){
		boolean result=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			result=true;
		return result;
	}
	
}

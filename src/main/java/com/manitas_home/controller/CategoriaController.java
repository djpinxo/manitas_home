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

import com.manitas_home.domain.Categoria;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class CategoriaController {
	@Autowired
	private CategoriaRepository CRepository;
	@Autowired
	private MensajeRepository RMensaje;
	@Autowired
	private EmpleoRepository REmpleo;
	
	@GetMapping("/categoria/crear")
	public String crear(HttpSession session,ModelMap m) {//TODO probar
		if (permisos(session)) {
			m.put("view", "categoria/crear");
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails", RMensaje.countByDestinatarioAndLeido(((Usuario) session.getAttribute("user")).getEmail(), false));
			return "views/_t/main";
		}
		else
			return "redirect:/empleo/listar";
	}

	@PostMapping("/categoria/crear")
	public String crear(@RequestParam(value = "nombre", defaultValue = "") String nombre, HttpSession session,ModelMap m, HttpServletRequest r) {//TODO probar
		nombre=nombre.trim();
		if (!nombre.equals("")&& Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& permisos(session)) {
			if (CRepository.findOneByNombre(nombre) == null) {
				nombre = nombre.toLowerCase();
				CRepository.save(new Categoria(nombre));
				m.put("resultado", "OK");
			} else
				m.put("resultado", "ERROR - La categoría ya existe.");
		} else
			m.put("resultado", "ERROR - No se puede realizar la operación.");
		if (r.getHeader("X-Requested-With") != null && r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest"))
			return "views/_t/resultado";
		else
			return "redirect:/categoria/listar";
	}
	@GetMapping("/categoria/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {//TODO probar
		if (id != null && permisos(session) && CRepository.exists(id)) {
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails", RMensaje.countByDestinatarioAndLeido(((Usuario) session.getAttribute("user")).getEmail(), false));
			m.put("categoria", CRepository.findOne(id));
			m.put("view", "categoria/modificar");
			return "views/_t/main";
		}
		else
			return "redirect:/categoria/listar";
	}
	@PostMapping("/categoria/modificar")//TODO probar
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="nombre", defaultValue="")String nombre,HttpSession session,ModelMap m, HttpServletRequest r) {
		nombre=nombre.trim();
		if(id!=null && !nombre.equals("") && Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre) && permisos(session)){
			Categoria c=CRepository.findOne(id);
			if (c != null) {
				nombre = nombre.toLowerCase();
				if (!c.getNombre().equals(nombre)){
					if(CRepository.findOneByNombre(nombre)==null) {
						c.setNombre(nombre);
						CRepository.save(c);
						m.put("resultado", "OK");
					}
					else m.put("resultado", "ERROR - La categoría ya existe.");
				}
				else m.put("resultado", "OK");
			}
			else m.put("resultado", "ERROR - La categoría no existe.");
		}
		else
			m.put("resultado", "ERROR - No se puede realizar la operación.");
		if(r.getHeader("X-Requested-With")!=null && r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest"))
			return "views/_t/resultado";
		else
			return "redirect:/categoria/listar";
	}
	@GetMapping("/categoria/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		if(r.getHeader("X-Requested-With")!=null && r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
			if(permisos(session))
				m.put("categorias",CRepository.findAll());
			return "xml/categoria/listar";
		}
		else {
			if(permisos(session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("categorias", CRepository.findAll());
				m.put("view","categoria/listar");
				return "views/_t/main";
			}
			else return "redirect:/login/login";
		}
	}
	@GetMapping("/categoria/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		if(id!=null&&permisos(session)){
			Categoria c=CRepository.findOne(id);
			if(c!=null){
				if(REmpleo.findByCategoria(c).isEmpty()){
					CRepository.delete(id);
					m.put("resultado", "OK");
				}
				else m.put("resultado", "ERROR - La categoría contiene relaciones con algun Empleo.");
			}
			else m.put("resultado", "OK");
		}
		else
			m.put("resultado", "ERROR - No se puede realizar la operación.");
		if(r.getHeader("X-Requested-With")!=null && r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")) return "views/_t/resultado";
		else return "redirect:/categoria/listar";
	}
	
	private boolean permisos(HttpSession s){
		boolean result=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			result=true;
		return result;
	}
}

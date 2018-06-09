package com.manitas_home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Categoria;
import com.manitas_home.domain.Empleo;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;
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
	public String crear(HttpSession session,ModelMap m) {//TODO probar
		m.put("usuarioactivo", session.getAttribute("user"));
		if(session.getAttribute("user")!=null)
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		m.put("categorias", CRepository.findAll());
		m.put("view","empleo/crear");
		return (permisos(session))?"views/_t/main":"redirect:/empleo/listar";
	}
	@PostMapping("/empleo/crear")
	public String crear(@RequestParam(value="nombre", defaultValue="")String nombre,@RequestParam(value="idcategoria", defaultValue="")Long idcategoria,HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
			if(!nombre.equals("") && idcategoria!=null &&permisos(session)&&ERepository.findOneByNombre(nombre)==null){//TODO validaciones expreg
				Categoria c=CRepository.findOne(idcategoria);
				if(c!=null){
					ERepository.save(new Empleo(nombre.toLowerCase(),c));
					m.put("resultado", "OK");
				}
				else m.put("resultado", "ERROR - La categoría no existe");
			}
			else m.put("resultado", "ERROR - El empleo ya existe.");
			if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest"))
				return "result";
			else
				return "redirect:/empleo/listar";
	}
	@GetMapping("/empleo/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {//TODO probar
		if(id!=null && permisos(session) && ERepository.exists(id)){
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("empleo", ERepository.findOne(id));
			m.put("categorias", CRepository.findAll());
			m.put("view","empleo/modificar");
			return "views/_t/main";
		}
		else
			return "redirect:/empleo/listar";
	}
	@PostMapping("/empleo/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="nombre", defaultValue="")String nombre,@RequestParam(value="idcategoria", defaultValue="")Long idcategoria,HttpSession session,ModelMap m, HttpServletRequest r) {//TODO probar
		if(id!=null&&idcategoria!=null&&!nombre.equals("")&&permisos(session)){//TODO añadir validaciones expreg
			nombre=nombre.toLowerCase();
			Empleo e=ERepository.findOne(id);
			if(e!=null){
				if(e.getNombre().equals(nombre)||ERepository.findOneByNombre(nombre)==null) {
					boolean result=true;
					e.setNombre(nombre);
					if(!e.getCategoria().getId().equals(idcategoria)){
						Categoria c=CRepository.findOne(idcategoria);
						if(c!=null)
							e.setCategoria(c);
						else
							result=false;
					}
					if(result){
						ERepository.save(e);
						m.put("resultado", "OK");
					}
					else
						m.put("resultado", "ERROR - La categoría no existe.");
				}
				else
					m.put("resultado", "ERROR - El empleo ya existe.");
			}
			else
				m.put("resultado", "ERROR - El empleo no existe.");
		}
		else
			m.put("resultado", "ERROR - No se puede realizar la operación.");
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest"))
			return "result";
		else return "redirect:/empleo/listar";
	}
	@GetMapping("/empleo/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {//TODO probar
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")&&permisos(session)){
			m.put("empleos",ERepository.findAll());
			return "xml/empleo/listar";
		}
		else {
			if (permisos(session)) {
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails", RMensaje.countByDestinatarioAndLeido(((Usuario) session.getAttribute("user")).getEmail(), false));
				m.put("empleos", ERepository.findAll());
				m.put("categorias", CRepository.findAll());
				m.put("view", "empleo/listar");
				return "views/_t/main";
			}
			else
				return "redirect:/login/login";
		}
	}
	@GetMapping("/empleo/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m, HttpServletRequest r) {//TODO probar
		if(id!=null&&permisos(session)){
			Empleo e=ERepository.findOne(id);
			if(e!=null){
				for(Manitas man:e.getManitas()){
					man.getEmpleos().remove(e);
				}
				e.getManitas().clear();
				ERepository.delete(id);
				m.put("resultado", "OK");
			}
			else
				m.put("resultado", "ERROR - El empleo no existe.");
		}
		else m.put("resultado", "ERROR - No se puede realizar la operación.");
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")) return "result";
		else return "redirect:/empleo/listar";
	}
	
	private boolean permisos(HttpSession s){
		boolean result=false;
		if(s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			result=true;
		return result;
	}
	
}

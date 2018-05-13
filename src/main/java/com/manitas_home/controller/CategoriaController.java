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
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.MensajeRepository;

@Controller
public class CategoriaController {
	@Autowired
	private CategoriaRepository CRepository;
	@Autowired
	private MensajeRepository RMensaje;
	
	@GetMapping("/categoria/crear")
	public String crear(HttpSession session,ModelMap m) {
		
			m.put("view","categoria/crear");
			m.put("usuarioactivo", session.getAttribute("user"));
			if(session.getAttribute("user")!=null)
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		
		return permisos("views/_t/main","redirect:/categoria/listar",session);
	}
	@PostMapping("/categoria/crear")
	public String crear(@RequestParam("nombre")String nombre,HttpSession session,ModelMap m) {
		
		/*// TODO principio
		Repositories.RepositoriesStart(CRepository, ERepository);
		long antes = System.currentTimeMillis();
		CRepository.findOneByNombre(nombre);
		System.out.println("generico tiempo" + (System.currentTimeMillis() - antes));
		antes = System.currentTimeMillis();
		Repositories.findOneByNombre(nombre);
		System.out.println("modificado tiempo" + (System.currentTimeMillis() - antes));
		// TODO fin
		if(CRepository.findOneByNombre(nombre)==null)
			CRepository.save(new Categoria(nombre));
		*/
		/*long antes = System.currentTimeMillis();
		if(CRepository.findOneByNombre(nombre)==null)
			CRepository.save(new Categoria(nombre));
		System.out.println("generico tiempo" + (System.currentTimeMillis() - antes));*/
		
		//Repositories.RepositoriesStart(CRepository);
		if(permisos(session)&&CRepository.findOneByNombre(nombre)==null)
			CRepository.save(new Categoria(nombre));
		return permisos("redirect:/categoria/listar","redirect:/categoria/listar",session);
	}
	@GetMapping("/categoria/modificar")
	public String modificar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		
		//TODO principio
		//Repositories.RepositoriesStart(CRepository);
		/*long antes = System.currentTimeMillis();
		m.put("categoria", CRepository.findOne(id));
		System.out.println("generico tiempo"+(System.currentTimeMillis()-antes));
		m.remove("categoria");*/
		if(permisos(session)){
			m.put("usuarioactivo", session.getAttribute("user"));
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
			m.put("categoria", CRepository.findOne(id));
			m.put("view","categoria/modificar");
		}
		return permisos("views/_t/main","redirect:/categoria/listar",session);
	}
	@PostMapping("/categoria/modificar")
	public String modificar(@RequestParam("id")Long id,@RequestParam("nombre")String nombre,HttpSession session,ModelMap m) {
		if(permisos(session)&&CRepository.findOneByNombre(nombre)==null){
		//Repositories.RepositoriesStart(CRepository);
			Categoria c=CRepository.findOne(id);
			c.setNombre(nombre);
			CRepository.save(c);
		}
		return "redirect:/categoria/listar";
	}
	@GetMapping("/categoria/listar")
	public String listar(HttpSession session,ModelMap m,HttpServletRequest r) {
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")&&permisos(session)){
			m.put("categorias",CRepository.findAll());
			return "xml/categoria/listar";
		}
		else {
			/*long antes = System.currentTimeMillis();
			m.put("categorias", CRepository.findAll());
			System.out.println("generico tiempo"+(System.currentTimeMillis()-antes));
			m.remove("categoria");*/
		
			if(permisos(session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("categorias", CRepository.findAll());
				m.put("view","categoria/listar");
			}
		return permisos("views/_t/main","redirect:/login/login",session);
	}
	}
	@GetMapping("/categoria/borrar")
	public String borrar(@RequestParam("id")Long id,HttpSession session,ModelMap m) {
		//Repositories.RepositoriesStart(CRepository);
		if(permisos(session)&&CRepository.exists(id))
			CRepository.delete(id);
		return "redirect:/categoria/listar";
	}
	
	//copiar cuando este terminado a empleo añadir el reenvio a administrador cuando first sea true
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

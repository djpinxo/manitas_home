package com.manitas_home.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Administrador;
import com.manitas_home.domain.Categoria;
import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Empleo;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Opinion;
import com.manitas_home.domain.Usuario;
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;
import com.manitas_home.repositories.OpinionRepository;

@Controller
public class ManitasController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private EmpleoRepository REmpleo;
	@Autowired
	private CategoriaRepository RCategoria;
	@Autowired
	private MensajeRepository RMensaje;
	@Autowired
	private OpinionRepository ROpinion;
	
	@GetMapping("/manitas/listar")
	public String listar(@RequestParam(value = "filtro", defaultValue="")String categoria ,HttpSession session,ModelMap m){//TODO añadir poder buscar por otras coordenadas y los filtros
		List<Manitas> listaBruta = RManitas.findAll();
		List<Manitas> lista = new ArrayList<Manitas>();
		if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&session.getAttribute("user")!=null){
			Cliente cliente=(Cliente)session.getAttribute("user");
			double clienteLat=Double.valueOf(cliente.getDireccion().substring(1, cliente.getDireccion().indexOf(",")));
			double clienteLng=Double.valueOf(cliente.getDireccion().substring(cliente.getDireccion().indexOf(",")+1,cliente.getDireccion().length()-1));
			for(int i=0;i<listaBruta.size();i++){
				double manitasLat=Double.valueOf(listaBruta.get(i).getDireccion().substring(1, listaBruta.get(i).getDireccion().indexOf(",")));
				double manitasLng=Double.valueOf(listaBruta.get(i).getDireccion().substring(listaBruta.get(i).getDireccion().indexOf(",")+1,listaBruta.get(i).getDireccion().length()-1));
				if(manitasLat+((double)listaBruta.get(i).getRadioAccion()/100)>clienteLat&&manitasLat-((double)listaBruta.get(i).getRadioAccion()/100)<clienteLat&&manitasLng+((double)listaBruta.get(i).getRadioAccion()/100)>clienteLng&&manitasLng-((double)listaBruta.get(i).getRadioAccion()/100)<clienteLng)
					lista.add(listaBruta.get(i));
			}
		}
		else lista=listaBruta;
		if(!categoria.equals("")){//TODO filtrado por categoria
			listaBruta = new ArrayList<Manitas>();
			for(int i=0;i<lista.size();i++){
				List<Empleo> empleos = lista.get(i).getEmpleos();
				for(int a=0;a<empleos.size();a++){
					if(empleos.get(a).getCategoria().getNombre().equals(categoria)){
						listaBruta.add(lista.get(i));
						a=empleos.size();
					}
				}
			}
			lista=listaBruta;
		}
		m.put("usuarioactivo", session.getAttribute("user"));
		if(session.getAttribute("user")!=null)
			m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
		m.put("manitas", lista);
		m.put("view","manitas/listar");
		return hayadmin("views/_t/main", "redirect:/administrador/crear");
	}
	@GetMapping("/manitas/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Manitas")) id=((Manitas)session.getAttribute("user")).getId();
		Manitas mani=null;
		if(id!=null)
			mani=RManitas.findOne(id);
		if(mani==null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("manitas")&&((Usuario)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
		if(mani!=null&&permisos(id,session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("view","manitas/modificar");
				m.put("empleos", REmpleo.findAll());
				m.put("manitas", mani);
				return "views/_t/main";
			}
		
		return "redirect:/manitas/listar";
	}
	@PostMapping("/manitas/modificar")
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="nombre", defaultValue="")String nombre ,@RequestParam(value="apellidos", defaultValue="")String apellidos ,@RequestParam(value="telefono", defaultValue="")String telefono ,@RequestParam(value="email", defaultValue="")String email ,@RequestParam(value="coordenadas", defaultValue="")String direccion ,@RequestParam(value = "descripcion", defaultValue="")String descripcion,@RequestParam(value="password", defaultValue="")String password,@RequestParam(value="passwordactualhash", defaultValue="")String passwordactual,@RequestParam(value = "radio", defaultValue="10")int radio, @RequestParam(value = "idempleo", defaultValue="")ArrayList <Long> idsempleos ,HttpSession session,ModelMap m,HttpServletRequest r) {
		nombre = nombre.trim().toLowerCase();
		email = email.trim().toLowerCase();
		password = password.trim();
		apellidos = apellidos.trim().toLowerCase();
		telefono = telefono.trim();
		direccion = direccion.trim();
		passwordactual = passwordactual.trim();
		if(id!=null&&!nombre.equals("") && !email.equals("") && password.length() == 254&& passwordactual.length() == 254&& !telefono.equals("") && !direccion.equals("")&&!idsempleos.isEmpty()&&Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)&& Pattern.matches("^[9|6]{1}([\\d]{2}[-|\\s]*){3}[\\d]{2}$", telefono)){
			if(permisos(id,session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){
			Manitas manitas=RManitas.findOne(id);
			if(manitas==null&&session.getAttribute("tipo").equals("manitas")&&((Usuario)session.getAttribute("user")).getId().equals(id)) LoginController.logoutStatic(session);
			if(manitas!=null){
				m.put("resultado", "OK");
				if(!manitas.getEmail().equals(email)){
					if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null){
						ThreadModificarMensajes hilo1 = new ThreadModificarMensajes(RMensaje,email,manitas.getEmail());
						hilo1.start();
						manitas.setEmail(email);
					}
					else m.put("resultado", "ERROR - El email esta ya en uso.");
				}
				if(!password.equals("250e9ad7d417a14a75a46c27601ca89898554ae68dc76417eb3d1476fe24e6cd67b02858640665b13566dd2994b71cb64004cd0d8bdda30b595a3f40271eaff00df2a06d62ffd749c26d63d2844fcad907b6821c0e4a1c2c885760ba10cbb4adefc66e4c42fb0b28fb7c632e9f0894f2493552d9ff599e683c660b19b129b3"))
					manitas.setPassword(password);
				manitas.setNombre(nombre);
				manitas.setApellidos(apellidos);
				manitas.setTelefono(telefono);
				manitas.setDireccion(direccion);
				manitas.setDescripcion(descripcion);
				manitas.setRadioAccion(radio);
				List <Empleo>listaEmpleos=new ArrayList<Empleo>();
				for(Long idempleo:idsempleos)
					listaEmpleos.add(REmpleo.findOne(idempleo));
				manitas.setEmpleos(listaEmpleos);
				RManitas.save(manitas);
				if(session.getAttribute("tipo")!=null&&!session.getAttribute("tipo").equals("administrador")) LoginController.logoutStatic(session);
			}
			else m.put("resultado", "ERROR - El manitas no existe.");
		}
		}
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")) return "result";
		else return "redirect:/manitas/listar";
	}
	@GetMapping("/manitas/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="passwordactualhash", defaultValue="")String passwordactual,HttpSession session,ModelMap m) {
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Manitas")) id=((Manitas)session.getAttribute("user")).getId();
		if(permisos(id,session)&&((Usuario)session.getAttribute("user")).getPassword().equals(passwordactual)){//TODO si se cambia la modal sino eliminar el password ){
			Manitas manitas=RManitas.findOne(id);
			if(manitas!=null){
				for(Opinion op:manitas.getOpiniones())
					ROpinion.delete(op);
				manitas.getOpiniones().clear();
				RManitas.delete(id);
				if(session.getAttribute("tipo")!=null&&!session.getAttribute("tipo").equals("administrador")) LoginController.logoutStatic(session);
			}
		}
		return "redirect:/manitas/listar";
	}
	@GetMapping("/manitas/ver")
	public String ver(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m){
		Manitas manitas=null;
		if(id!=null){
			manitas=RManitas.findOne(id);
			if(manitas!=null){
				m.put("usuarioactivo", session.getAttribute("user"));
				if(session.getAttribute("user")!=null){
					m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
					if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente"))
						m.put("opinionClienteManitas", ROpinion.findOneByClienteAndManitas(((Cliente)session.getAttribute("user")), manitas));
				}
				m.put("manitas", RManitas.findOne(id));
				m.put("view","manitas/ver");
			}
		}
		return hayadmin((manitas!=null)?"views/_t/main":"redirect:/manitas/listar", "redirect:/administrador/crear");
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
	private boolean permisos(Long id,HttpSession s){
		boolean salida=false;
		if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador")&&s.getAttribute("user")!=null&&((Administrador)s.getAttribute("user"))!=null)
			salida=true;
		else if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("manitas")&&s.getAttribute("user")!=null&&((Manitas)s.getAttribute("user")).getId().equals(id))
			salida=true;
		return salida;
	}
	////////TODO filtrar no funciona del todo
	private ArrayList <Manitas> filtrar(ArrayList <Manitas> listaAFiltrar,ArrayList <String> filtros){
		ArrayList<Categoria> filtrosCategoria = new ArrayList <Categoria>();
		ArrayList<Empleo> filtrosEmpleo = new ArrayList <Empleo>();
		ArrayList<Manitas> listaFiltrada= new ArrayList <Manitas>();
		for(int i=0;i<filtros.size();i++){
			Categoria cat=RCategoria.findOneByNombre(filtros.get(i));
			if(cat!=null){
				filtrosCategoria.add(cat);
			}
			else{
				Empleo emp=REmpleo.findOneByNombre(filtros.get(i));
				if(emp!=null){
					filtrosEmpleo.add(emp);
				}
			}
		}
		if(filtrosEmpleo.size()<=0&&filtrosCategoria.size()>0) filtrosEmpleo=(ArrayList<Empleo>) REmpleo.findAll();
		for(int i=0;i<listaAFiltrar.size();i++){
			Manitas man=listaAFiltrar.get(i);
			for(int a=0;a<filtrosEmpleo.size();a++){
				if(man.getEmpleos().contains(filtrosEmpleo.get(a))){
					if(filtrosCategoria.size()>0){
						for(int x=0;x<filtrosCategoria.size();x++){
							if(filtrosEmpleo.get(a).getCategoria().equals(filtrosCategoria.get(x))){
								listaFiltrada.add(man);
							}
						}
					}
					else listaFiltrada.add(man);
				}
			}
			
		}
		return listaFiltrada;
	}
}

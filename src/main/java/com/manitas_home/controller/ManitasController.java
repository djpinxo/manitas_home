package com.manitas_home.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.manitas_home.domain.Usuario;
import com.manitas_home.funciones.funcionstart;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.MensajeRepository;

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
	
	/*@GetMapping("/manitas/listar")
	public String listar(@RequestParam(value = "filtro", defaultValue="")ArrayList <String> filtros ,HttpSession session,ModelMap m){//TODO añadir poder buscar por otras coordenadas y los filtros -actualizandose
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
		
		if(filtros.size()>0){//TODO kitar el false
			lista=filtrar((ArrayList<Manitas>) lista, filtros);
		}
		m.put("usuarioactivo", session.getAttribute("user"));
		m.put("manitas", lista);
		m.put("view","manitas/listar");
		return hayadmin("views/_t/main", "redirect:/administrador/crear");
	}*/
	@GetMapping("/manitas/listar")
	public String listar(@RequestParam(value = "filtro", defaultValue="")String filtro ,HttpSession session,ModelMap m){//TODO añadir poder buscar por otras coordenadas y los filtros
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
		System.out.println(session.getAttribute("tipo"));
		if(permisos(id,session)){
				m.put("usuarioactivo", session.getAttribute("user"));
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("view","manitas/modificar");
				m.put("empleos", REmpleo.findAll());
				m.put("manitas", RManitas.findOne(id));
			}
		
		return permisos("views/_t/main","redirect:/manitas/listar",id,session);
	}
	@PostMapping("/manitas/modificar")
	public String modificar(@RequestParam("id")Long id,@RequestParam("nombre")String nombre ,@RequestParam("apellidos")String apellidos ,@RequestParam("telefono")String telefono ,@RequestParam("email")String email ,@RequestParam("coordenadas")String direccion ,@RequestParam(value = "descripcion", defaultValue="")String descripcion,@RequestParam("password")String password,@RequestParam(value = "radio", defaultValue="10")String radio, @RequestParam(value = "idempleo", defaultValue="")ArrayList <Long> idsempleos ,HttpSession session) {
		if(permisos(id,session)){
			Manitas manitas=RManitas.findOne(id);
			if(RCliente.findOneByEmail(email)==null&&RManitas.findOneByEmail(email)==null&&RAdministrador.findOneByEmail(email)==null)
				manitas.setEmail(email);
			if(!password.equals("c2e5a90f5a957f5abf40377e72d0ad45594a7a257f678d5d6c4844194b86b3b3a61f733111346748c1f82629cd9c5763ba6b77f0d358fb5460bf111df785ffcd72844d1438792e8ea7566ba65f18b62ba1ba2012eef8ab917fab9ab4491b13e61aad97d902cc7ba3412e431fd8af9a66ea8366c86953a68ec3d2032fcee09e"))
				manitas.setPassword(password);
			manitas.setNombre(nombre);
			manitas.setApellidos(apellidos);
			manitas.setTelefono(telefono);
			manitas.setDireccion(direccion);
			manitas.setDescripcion(descripcion);
			manitas.setRadioAccion(Integer.parseInt(radio));
			ArrayList<Empleo> listaEmpleos=new ArrayList<Empleo>();
			for(int i=0;i<idsempleos.size();i++)
				listaEmpleos.add(REmpleo.findOne(idsempleos.get(i)));
			manitas.setEmpleos(listaEmpleos);
			RManitas.save(manitas);
		}
		return ((session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("manitas")&&((Manitas)session.getAttribute("user")).getId().equals(id)))?"redirect:/login/logout":"redirect:/manitas/listar";
	}
	@GetMapping("/manitas/borrar")
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m) {
		if(id==null && session.getAttribute("user")!=null&&session.getAttribute("user").getClass().getName().equals("com.manitas_home.domain.Manitas")) id=((Manitas)session.getAttribute("user")).getId();
		if(permisos(id,session)){
			if(RManitas.exists(id)&&permisos(id,session))
				RManitas.delete(id);
		}
		return ((session.getAttribute("user")!=null&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("manitas")&&((Cliente)session.getAttribute("user")).getId().equals(id)))?"redirect:/login/logout":"redirect:/manitas/listar";
	}
	@GetMapping("/manitas/ver")
	public String ver(@RequestParam(value="id", defaultValue="")Long id,HttpSession session,ModelMap m){
		boolean existeMan=false;
		if(id!=null){
			existeMan=RManitas.exists(id);
			if(existeMan){
				m.put("usuarioactivo", session.getAttribute("user"));
				if(session.getAttribute("user")!=null)
				m.put("usuarioemails",RMensaje.countByDestinatarioAndLeido(((Usuario)session.getAttribute("user")).getEmail(),false));
				m.put("manitas", RManitas.findOne(id));
				m.put("view","manitas/ver");
			}
		}
		return hayadmin((id!=null&&existeMan)?"views/_t/main":"redirect:/manitas/listar", "redirect:/administrador/crear");
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
	private String permisos(String destinoOk,String destinoFail,Long id,HttpSession s){
		String salida=destinoFail;
		if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("administrador"))
			salida=destinoOk;
		else if(id!=null&&s.getAttribute("tipo")!=null&&s.getAttribute("tipo").equals("manitas")&&s.getAttribute("user")!=null&&((Manitas)s.getAttribute("user")).getId().equals(id))
			salida=destinoOk;
		return salida;
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

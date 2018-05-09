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
import com.manitas_home.domain.Opinion;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.OpinionRepository;
import com.manitas_home.funciones.funcionstart;
@Controller
public class OpinionController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private OpinionRepository ROpinion;
	
	@PostMapping("/opinion/crear")//TODO por probar
	public String crear(@RequestParam("idmanitas")Long idManitas,@RequestParam("valoracion")Double valoracion,@RequestParam(value = "comentario", defaultValue="")String comentario,HttpSession session) {
		if(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&session.getAttribute("user")!=null){
			Long idCliente=((Cliente)session.getAttribute("user")).getId();
			Cliente cli=RCliente.findOne(idCliente);
			Manitas man=RManitas.findOne(idManitas);
			Opinion opinion=null;
			if(cli!=null&&man!=null)
				opinion=ROpinion.findOneByClienteAndManitas(cli,man);
			if(opinion==null){
				ROpinion.save(new Opinion(cli,man,valoracion,comentario));
			}
		}
		return "redirect:/manitas/ver?id="+idManitas;
	}
	@PostMapping("/opinion/modificar")//TODO por probar
	public String modificar(@RequestParam("id")Long id,@RequestParam("valoracion")Double valoracion,@RequestParam(value = "comentario", defaultValue="")String comentario,HttpSession session) {
		Long idmanitas=new Long(0);
		if(session.getAttribute("user")!=null){
			Opinion opinion=ROpinion.findOne(id);
			if(opinion!=null&&(opinion.getCliente().equals(session.getAttribute("user"))||(session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("administrador")))){
				opinion.setValoracion(valoracion);
				opinion.setComentario(comentario);
				idmanitas=opinion.getManitas().getId();
				ROpinion.save(opinion);
			}
		}
		return "redirect:/manitas/ver?id="+idmanitas;
	}
	
	
}

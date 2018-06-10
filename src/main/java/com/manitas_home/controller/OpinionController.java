package com.manitas_home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.domain.Opinion;
import com.manitas_home.domain.Usuario;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.ManitasRepository;
import com.manitas_home.repositories.OpinionRepository;
@Controller
public class OpinionController {
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private OpinionRepository ROpinion;
	
	@PostMapping("/opinion/crear")//TODO por probar
	public String crear(@RequestParam(value="idmanitas", defaultValue="")Long idManitas,@RequestParam(value="valoracion", defaultValue="")Double valoracion,@RequestParam(value = "titulo", defaultValue="")String titulo,@RequestParam(value = "comentario", defaultValue="")String comentario,HttpSession session) {
		titulo=titulo.trim();
		if(!titulo.equals("")&&session.getAttribute("tipo")!=null&&session.getAttribute("tipo").equals("cliente")&&session.getAttribute("user")!=null){
			Long idCliente=((Cliente)session.getAttribute("user")).getId();
			Cliente cli=RCliente.findOne(idCliente);
			Manitas man=RManitas.findOne(idManitas);
			Opinion opinion=null;
			if(cli!=null&&man!=null)
				opinion=ROpinion.findOneByClienteAndManitas(cli,man);
			if(opinion==null){
				if(valoracion==null) valoracion=1.0;
				if(valoracion<1) valoracion=1.0;
				else if(valoracion>5) valoracion=5.0;
				ROpinion.save(new Opinion(cli,man,valoracion,comentario.trim(),titulo));
			}
		}
		return "redirect:/manitas/ver?id="+idManitas;
	}
	@PostMapping("/opinion/modificar")//TODO por probar
	public String modificar(@RequestParam(value="id", defaultValue="")Long id,@RequestParam(value="valoracion", defaultValue="")Double valoracion,@RequestParam(value = "titulo", defaultValue="")String titulo,@RequestParam(value = "comentario", defaultValue="")String comentario,HttpSession session) {
		Long idmanitas=new Long(0);
		titulo=titulo.trim();
		if(id!=null&&!titulo.equals("")&&session.getAttribute("user")!=null){
			Opinion opinion=ROpinion.findOne(id);
			if(opinion!=null&&session.getAttribute("tipo")!=null&&(session.getAttribute("tipo").equals("administrador")||session.getAttribute("tipo").equals("cliente")&&opinion.getCliente().getId().equals(((Usuario)session.getAttribute("user")).getId()))){
				if(valoracion==null) valoracion=1.0;
				if(valoracion<1) valoracion=1.0;
				else if(valoracion>5) valoracion=5.0;
				opinion.setTitulo(titulo);
				opinion.setValoracion(valoracion);
				opinion.setComentario(comentario);
				opinion.setFecha(System.currentTimeMillis());
				idmanitas=opinion.getManitas().getId();
				ROpinion.save(opinion);
			}
		}
		return "redirect:/manitas/ver?id="+idmanitas;
	}
	@RequestMapping("/opinion/borrar")//TODO por probar
	public String borrar(@RequestParam(value="id", defaultValue="")Long id,HttpSession session) {
		Long idmanitas=new Long(0);
		if(id!=null&&session.getAttribute("user")!=null){
			Opinion opinion=ROpinion.findOne(id);
			if(opinion!=null&&session.getAttribute("tipo")!=null&&(session.getAttribute("tipo").equals("administrador")||(session.getAttribute("tipo").equals("cliente")&&opinion.getCliente().getId().equals(((Usuario)session.getAttribute("user")).getId())))){
				idmanitas=opinion.getManitas().getId();
				ROpinion.delete(opinion);
			}
		}
		return "redirect:/manitas/ver?id="+idmanitas;
	}
}

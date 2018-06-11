package com.manitas_home.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Manitas;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;

import funciones.funcionstart;

@Controller
public class LoginController {
	@Autowired
	private AdministradorRepository RAdministrador;
	@Autowired
	private ManitasRepository RManitas;
	@Autowired
	private ClienteRepository RCliente;
	@Autowired
	private EmpleoRepository REmpleo;
	
	@GetMapping("/login/login")
	public String login(HttpSession session,ModelMap m,HttpServletRequest r) {
			System.out.println(r.getRemoteHost());
			if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
				System.out.println("peticion ajax");
			}
			
			m.put("view","login/login");
			
			if(funcionstart.getFirst(RAdministrador)) System.out.println("primera vez sin admins");
		
		//return funciones.funcionstart.funcionArranque(session,m,this.RAdministrador,"views/_t/main");
			return (permisos("redirect:/",hayadmin("views/_t/main", "redirect:/administrador/crear"),session));
	}
	@PostMapping("/login/login")
	public String login(@RequestParam(value="email", defaultValue="")String email,@RequestParam(value="password", defaultValue="")String password,HttpSession session,ModelMap m,HttpServletRequest r) {
		email=email.trim();
		if(!email.equals("")&&password.length()==254&&Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)){
		if(RCliente.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RCliente.findOneByEmailAndPassword(email, password));
			session.setAttribute("tipo","cliente");
		}
		else if(RManitas.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RManitas.findOneByEmailAndPassword(email, password));
			session.setAttribute("tipo","manitas");
		}
		else if(RAdministrador.findOneByEmailAndPassword(email, password)!=null){
			session.setAttribute("user", RAdministrador.findOneByEmailAndPassword(email, password));
			session.setAttribute("tipo","administrador");
		}
		}
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest")){
			if(session.getAttribute("tipo")!=null&&session.getAttribute("user")!=null){
				m.put("resultado", "OK");
			}
			else{
				m.put("resultado", "ERROR - Usuario y/o contraseña incorrectos.");
			}
			return "result";
		}
		else
			return "redirect:/login/login";
	}
	@GetMapping("/login/logout")
	public String logout(HttpSession session) {
		session.setAttribute("user",null);
		session.setAttribute("tipo",null);
		return "redirect:/";
	}
	@GetMapping("/login/crear")
	public String crear(HttpSession session,ModelMap m) {
		//TODO revisar
		m.put("view","login/crear");
		m.put("empleos", REmpleo.findAll());
		return (permisos("redirect:/",hayadmin("views/_t/main", "redirect:/administrador/crear"),session));
	}
	@PostMapping("/login/crear")//TODO modificar añadir el empleo a manitas
	public String crear(@RequestParam(value="tipo", defaultValue="")String tipo ,@RequestParam(value="nombre", defaultValue="")String nombre ,@RequestParam(value="apellidos", defaultValue="")String apellidos ,@RequestParam(value="telefono", defaultValue="")String telefono ,@RequestParam(value="email", defaultValue="")String email ,@RequestParam(value="coordenadas", defaultValue="")String direccion ,@RequestParam(value = "descripcion", defaultValue="")String descripcion,@RequestParam(value="password", defaultValue="")String password,@RequestParam(value = "radio", defaultValue="10")int radio, @RequestParam(value = "idempleo", defaultValue="")ArrayList <Long> idsempleos ,HttpSession session,ModelMap m,HttpServletRequest r) {
		if (session.getAttribute("user") == null) {
			tipo = tipo.trim().toLowerCase();
			nombre = nombre.trim().toLowerCase();
			email = email.trim().toLowerCase();
			password = password.trim();
			apellidos = apellidos.trim().toLowerCase();
			telefono = telefono.trim();
			direccion = direccion.trim();
			if (!tipo.equals("") && !nombre.equals("") && !email.equals("") && password.length() == 254&& !telefono.equals("") && !direccion.equals("")) {
				if(Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", nombre)&& Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9_-]+([.]([a-zA-Z0-9_-]+[a-zA-Z0-9]|[a-zA-Z0-9]))+$", email)&& Pattern.matches("^[9|6]{1}([\\d]{2}[-|\\s]*){3}[\\d]{2}$", telefono)){
					if (RCliente.findOneByEmail(email) == null && RManitas.findOneByEmail(email) == null&& RAdministrador.findOneByEmail(email) == null) {
						if (tipo.equals("cliente") && !apellidos.equals("") && Pattern.matches("^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\\s|\\ç|\\Ç|\\-]*)+$", apellidos)&& envioMail(email)) {
							RCliente.save(new Cliente(nombre, apellidos, telefono, email, password, direccion));// TODO
							m.put("resultado", "OK");
						}
						else if (tipo.equals("manitas") && !idsempleos.isEmpty() && envioMail(email)) {
							Manitas manitas = new Manitas(nombre, apellidos, telefono, email, password, direccion,descripcion, radio);
							for (int i = 0; i < idsempleos.size(); i++)
								manitas.getEmpleos().add(REmpleo.findOne(idsempleos.get(i)));
							RManitas.save(manitas);// TODO
							m.put("resultado", "OK");
						}
					}
					else m.put("resultado", "ERROR - El email ya está en uso.");
				}
			}
		}
		if(r.getHeader("X-Requested-With")!=null&&r.getHeader("X-Requested-With").toString().toLowerCase().equals("xmlhttprequest"))
			return "result";
		else
			return "redirect:/";
	}
	@GetMapping("/login/activation")
	public String activation(@RequestParam(value = "valor", defaultValue="")String valor,HttpSession session,HttpServletRequest r) {
		if(!valor.equals("")){
			valor=funcionstart.decrypt(valor);
			String []valores=valor.split("---");
			if(System.currentTimeMillis()<Long.parseLong(valores[1])+1800000){
				if(RManitas.findOneByEmail(valores[0])!=null){
					Manitas usuario=RManitas.findOneByEmail(valores[0]);
					usuario.setHabilitado(true);
					RManitas.save(usuario);
				}
				else if(RCliente.findOneByEmail(valores[0])!=null){
					Cliente usuario=RCliente.findOneByEmail(valores[0]);
					usuario.setHabilitado(true);
					RCliente.save(usuario);
				}
			}
		}
		
		return "redirect:/login/login";
	}
	private String hayadmin(String existeDestino,String noExisteDestino){
		String pagina=existeDestino;
		if(funcionstart.getFirst(RAdministrador))
			pagina=noExisteDestino;
		return pagina;
	}
	private String permisos(String destinoOk,String destinoFail,HttpSession s){
		String salida=destinoFail;
		if(s.getAttribute("tipo")!=null)
				salida=destinoOk;
		return salida;
	}
	
	private boolean envioMail(String emailDestino){
		boolean resultado=true;
	String host="manitashome.ddns.net";  
	String user="no-reply@"+host;
	String password="R7wbBoGzcyCdcNMf";
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.socketFactory.port", "587");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "587");

    Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() 
                     {
                           return new PasswordAuthentication(user, password);
                     }
                });

    session.setDebug(true);

    try {

          Message message = new MimeMessage(session);
          message.setFrom(new InternetAddress("\"mensaje generico\" <"+user+">")); //Remetente

          message.setRecipients(Message.RecipientType.TO, 
                            InternetAddress.parse(emailDestino));
          message.setSubject("Email de confirmacion");
          message.setText("Por favor verifique su email para completar el registro");
          message.setText("http://"+host+"/login/activation?valor="+URLEncoder.encode(funcionstart.encrypt(emailDestino+"---"+System.currentTimeMillis()))+"\nno responda a este email para contactar dirijase a la pagina seccion contactanos indicando su email y su problema");
          Transport.send(message);

          System.out.println("email enviado a al gestor con exito");

     } catch (MessagingException e) {
          String username="homemanitas@gmail.com";
          String passwordgmail="R7wbBoGzcyCdcNMf";

  		Properties props1 = new Properties();
  		props1.put("mail.smtp.auth", "true");
  		props1.put("mail.smtp.starttls.enable", "true");
  		props1.put("mail.smtp.host", "smtp.gmail.com");
  		props1.put("mail.smtp.port", "587");
  		props1.put("mail.smtp.socketFactory.port", "587");
  		props1.put("mail.smtp.socketFactory.fallback", "false");

  		Session sessiongmail = Session.getInstance(props1,
  		  new javax.mail.Authenticator() {
  			protected PasswordAuthentication getPasswordAuthentication() {
  				return new PasswordAuthentication(username, passwordgmail);
  			}
  		  });

  		try {

  			Message mensajegmail = new MimeMessage(sessiongmail);
  			mensajegmail.setFrom(new InternetAddress("\"mensaje generico\" <"+username+">"));
  			mensajegmail.setRecipients(Message.RecipientType.TO,
  				InternetAddress.parse(emailDestino));
  			mensajegmail.setSubject("Email de confirmacion");
            mensajegmail.setText("http://"+host+"/login/activation?valor="+URLEncoder.encode(funcionstart.encrypt(emailDestino+"---"+System.currentTimeMillis()))+"\nno responda a este email para contactar dirijase a la pagina seccion contactanos indicando su email y su problema");
            Transport.send(mensajegmail);

  			System.out.println("enviado por gmail");

  		} catch (MessagingException a) {
  			resultado=false;
  			System.out.println("error en el envio");
  		}
    }
	return resultado;
}
	public static void logoutStatic(HttpSession session){
		session.setAttribute("user",null);
		session.setAttribute("tipo",null);
	}
}

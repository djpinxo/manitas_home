package com.manitas_home.funciones;


import java.io.FileReader;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;

import com.manitas_home.repositories.AdministradorRepository;

public class funcionstart {
	private static AdministradorRepository RAdministrador;
	private static HttpSession session;
	private static ModelMap model;
	private static Properties propiedades=funcionProperties();
	private static long lista=0;//TODO
	private static long tiempoUltimaActualizacion;//TODO
	private static long tiempoUltimaActualizacionProperties=System.currentTimeMillis();//TODO
	public static String funcionArranque(HttpSession session,ModelMap model,AdministradorRepository RAdministrador,String vista){
		funcionstart.RAdministrador=RAdministrador;
		funcionstart.session=session;
		funcionstart.model=model;
		//TODO inicio
		if(tiempoUltimaActualizacionProperties+Integer.parseInt(propiedades.getProperty("propierties.reload.time"))<System.currentTimeMillis()){
			tiempoUltimaActualizacionProperties+=60000;
			propiedades=funcionProperties();
			tiempoUltimaActualizacionProperties=System.currentTimeMillis();
		}
		if(lista==0){
			lista=RAdministrador.count();
			tiempoUltimaActualizacion=System.currentTimeMillis();
			System.out.println("ultima lista cacheada:"+tiempoUltimaActualizacion);
		}
		else if(tiempoUltimaActualizacion+Integer.parseInt(propiedades.getProperty("cache.administrador.count.time"))<System.currentTimeMillis()){//TODO tiempo de expiracion de la cache
			tiempoUltimaActualizacion+=Integer.parseInt(propiedades.getProperty("cache.administrador.count.time"));
			lista=RAdministrador.count();
			tiempoUltimaActualizacion=System.currentTimeMillis();
			System.out.println("ultima lista cacheada:"+tiempoUltimaActualizacion);
		}
		
		//TODO final
		
		if(primerInicio()&&model.containsAttribute("view")&&model.get("view").equals("administrador/crear"));
		else if(primerInicio())vista="redirect:/administrador/crear";
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("home/index"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("login/login"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("crear/select"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("manitas/crear"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("cliente/crear"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("user/crear"));
		else if(!isLogin()&&model.containsAttribute("view")&&model.get("view").equals("login/crear"));
		else if(isLogin()&&model.containsAttribute("view")&&model.get("view").equals("user/crear"))  vista="redirect:/";
		else if(isLogin()&&model.containsAttribute("view")&&model.get("view").equals("crear/select")) vista="redirect:/";
		else if(isLogin()&&model.containsAttribute("view")&&model.get("view").equals("crear/formmanitas")) vista="redirect:/";
		else if(isLogin()&&model.containsAttribute("view")&&model.get("view").equals("crear/formcliente")) vista="redirect:/";
		else if(isLogin()&&model.containsAttribute("view")&&model.get("view").equals("login/login"))  vista="redirect:/";
		else if(!isLogin()) vista="redirect:/";
		
		
		return vista;
	}
	
	private static boolean primerInicio(){
		return (lista<=0);
	}
	private static boolean isLogin(){
		return (session.getAttribute("user")!=null);
	}
	
	private static Properties funcionProperties() {
		Properties p=new Properties();
		try {
			p.load(new FileReader("src/main/resources/application.properties"));
			System.out.println("propiedades actualizadas");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			p.setProperty("cache.administrador.count.time", "60000");
			p.setProperty("propierties.reload.time", "60000");
			System.out.println("no se pudo leer el fichero");
		}
		return p;
	}
	
	private static boolean first=true;
	
	public static boolean getFirst(AdministradorRepository RAdministrador){
			if(first&&RAdministrador.count()>0){
				first=false;
			}
		return first;
	}
	public static String suscriptionCoder(String emailremitente,String emaildestino){
		String codificado="",param1="",param2="";
		char [] codes={'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'};
		int i=0;
		while(param1==""&&param2==""){
			if(emailremitente.equals(emaildestino)){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emailremitente.length()==i){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emaildestino.length()==i){
				param1=emaildestino;
				param2=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)>emaildestino.toLowerCase().charAt(i)){
				param1=emaildestino;
				param2=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)<emaildestino.toLowerCase().charAt(i)){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)==emaildestino.toLowerCase().charAt(i)){
				i++;
			}
			
		}
		for(i=param1.length()-1;i>=0;i--){
			int a=(int)param1.charAt(i)*(i+1);
			int b=a+i;
			int c=codes[b%26];
			codificado+=b+""+c+""+a;
		}
		for(i=0;i<param2.length();i++){
			int a=(int)param2.charAt(i)*(i+1)+i;
			int b=a+i;
			int c=codes[b%26];
			codificado+=a+""+c+""+b;
		}
		return codificado;
	}
	
	
}

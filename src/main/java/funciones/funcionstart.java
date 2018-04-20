package funciones;


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
	
	
}
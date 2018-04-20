package funciones;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.manitas_home.domain.Administrador;
import com.manitas_home.domain.Categoria;
import com.manitas_home.domain.Cliente;
import com.manitas_home.domain.Empleo;
import com.manitas_home.domain.Manitas;
import com.manitas_home.repositories.AdministradorRepository;
import com.manitas_home.repositories.CategoriaRepository;
import com.manitas_home.repositories.ClienteRepository;
import com.manitas_home.repositories.EmpleoRepository;
import com.manitas_home.repositories.ManitasRepository;

public class Repositories{
	static CategoriaRepository CategoriaR;
	static List <Categoria> categorias;
	static EmpleoRepository EmpleoR;
	static List <Empleo> empleos;
	static ClienteRepository ClienteR;
	static List <Cliente> clientes;
	static AdministradorRepository AdministradorR;
	static List <Administrador> administradores;
	static ManitasRepository ManitasR;
	static List <Manitas> manitas;
	
	
	public static void forceUpdate(String nombreRepositorio) {
		try {
			System.out.println("hola");
			Field f = Repositories.class.getField("EmpleoR");
			//Class<?> t = f.getType();
			//System.out.println(t);
			System.out.println(f.get(null).getClass().getMethod("findAll", null).invoke("findAll", null));
			System.out.println("fin hola");
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println("error se pasa a actualizar todas las caches activas");
			if(categorias!=null) categorias=CategoriaR.findAll();
			if(empleos!=null) empleos=EmpleoR.findAll();
			if(clientes!=null) clientes=ClienteR.findAll();
			if(administradores!=null) administradores=AdministradorR.findAll();
			if(manitas!=null) manitas=ManitasR.findAll();
		}
	}
	public static void RepositoriesStart(CategoriaRepository CR) {
		CategoriaR=CR;
		if(categorias==null)
		categorias=CategoriaR.findAll();
	}
	public static void RepositoriesStart(EmpleoRepository ER) {
		EmpleoR=ER;
		if(empleos==null)
		empleos=EmpleoR.findAll();
	}
	public static void RepositoriesStart(ClienteRepository CR) {
		ClienteR=CR;
		if(clientes==null)
		clientes=ClienteR.findAll();
	}
	public static void RepositoriesStart(AdministradorRepository AR) {
		AdministradorR=AR;
		if(administradores==null)
		administradores=AdministradorR.findAll();
	}
	public static void RepositoriesStart(ManitasRepository MR) {
		ManitasR=MR;
		if(manitas==null)
			manitas=ManitasR.findAll();
	}
	public static void RepositoriesStart(CategoriaRepository cRepository, EmpleoRepository eRepository) {
		RepositoriesStart(cRepository);
		RepositoriesStart(eRepository);
	}
	
	

}

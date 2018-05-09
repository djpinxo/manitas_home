package com.manitas_home.funciones;

import java.util.List;

import com.manitas_home.domain.Categoria;

public class RepositoriesCategoria extends Repositories{
	
	
	//----------------------------------------------------Categoria-------------------------------------------////
	public static List<Categoria> findAll() {
		return categorias;
	}
	public static <S extends Categoria> S save(S entity) {
		CategoriaR.save(entity);
		if(findOne(entity.getId())==null){
			categorias.add(entity);
		}
		/*else{
			boolean exito=false;
			for(int i=0;i<categorias.size()&&!exito;i++){
				if(categorias.get(i).equals(entity)){
					categorias.set(i, entity);
					exito=true;
				}
			}
		}*/
		return entity;
	}
	public static Categoria findOne(Long id) {
		Categoria categoria =null;
		for(int i=0;i<categorias.size()&&categoria==null;i++){
			if(categorias.get(i).getId().equals(id))
			categoria=categorias.get(i);
		}
		return categoria;
	}
	public static void delete(Long id) {
		CategoriaR.delete(id);
		categorias=CategoriaR.findAll();
	}
	public static void delete(Categoria entity) {
		CategoriaR.delete(entity);
		categorias=CategoriaR.findAll();
	}
	public static Categoria findOneByNombre(String nombre){
		Categoria categoria =null;
		for(int i=0;i<categorias.size()&&categoria==null;i++){
			if(categorias.get(i).getNombre().equals(nombre))
			categoria=categorias.get(i);
		}
		return categoria;
	}
	//----------------------------------------------------FinCategoria-------------------------------------------////	

}

package com.manitas_home.funciones;

import java.util.List;

import com.manitas_home.domain.Empleo;

public class RepositoriesEmpleo extends Repositories{
	
	//----------------------------------------------------Empleo-------------------------------------------////
		public static List<Empleo> findAll() {
			return empleos;
		}
		public static <S extends Empleo> S save(S entity) {
			EmpleoR.save(entity);
			empleos=EmpleoR.findAll();
			return entity;
		}
		public static Empleo findOne(Long id) {
			Empleo empleo =null;
			for(int i=0;i<empleos.size()&&empleo==null;i++){
				if(empleos.get(i).getId().equals(id))
					empleo=empleos.get(i);
			}
			return empleo;
		}
		public static void delete(Long id) {
			EmpleoR.delete(id);
			empleos=EmpleoR.findAll();
		}
		public static void delete(Empleo entity) {
			EmpleoR.delete(entity);
			empleos=EmpleoR.findAll();
		}
		public static Empleo findOneByNombre(String nombre){
			Empleo empleo =null;
			for(int i=0;i<empleos.size()&&empleo==null;i++){
				if(empleos.get(i).getNombre().equals(nombre))
					empleo=empleos.get(i);
			}
			return empleo;
		}
		//----------------------------------------------------FinEmpleo-------------------------------------------////	

}

function validar(form) {
	var salida=false;
	if(trim(form)){
		codContraseña();
		salida=true;
	}
	return salida;
}
function codContraseña() {//modificar para que acepte varios campos password en una pagina
	var campo = document.getElementsByName("passwordsin")[0];
	document.getElementsByName("password")[0].value = shake_256(campo.value,1023);
	campo.disabled = true;
}
function trim(form){
	var salida=true;
	for(i=0;i<form.getElementsByTagName("input").length&&salida==true;i++)
		if(!form.getElementsByTagName("input")[i].disabled&&(form.getElementsByTagName("input")[i].type=="text"||form.getElementsByTagName("input")[i].type=="password"||form.getElementsByTagName("input")[i].type=="number"))
			if(form.getElementsByTagName("input")[i].name!="password"){
				form.getElementsByTagName("input")[i].value=form.getElementsByTagName("input")[i].value.trim();
				if(form.getElementsByTagName("input")[i].value.trim().length>0);
				else{
					
					salida=false;
				}
			}
	return salida;
}

window.onload=setInterval('location.reload()',901000);
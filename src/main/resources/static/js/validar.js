function validar(form){
	
	
	/* Divs para mostrar los errores */
	divsError=form.getElementsByClassName('form-control-feedback');
	
	/* VALIDAR NOMBRE */
	nombre = document.getElementById("nombre").value;
	nombre=nombre.trim();
	nombreCorrecto=false;
	expRegNombre = /^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\s|\ç|\Ç|\-]*)+$/;
	
		if (nombre.length>=3 && nombre.length<30) {
			if(expRegNombre.test(nombre)){
				nombreCorrecto=true;
				divsError[0].hidden=true;
				//document.getElementById("nombre").classList.remove("alert-danger");
			}
			else {
				
			divsError[0].hidden=false;
			//document.getElementById("nombre").classList.add("alert-danger");
			divsError[0].getElementsByTagName('span')[0].innerHTML="Nombre incorrecto, ha introducido caracteres no permitidos.";
				
			}
		}
		else {
			divsError[0].hidden=false;
			//document.getElementById("nombre").classList.add("alert-danger");
			divsError[0].getElementsByTagName('span')[0].innerHTML="Nombre incorrecto, el nombre debe contener entre 3 y 30 caracteres.";
			
		}
		
	
		
	/* VALIDAR APELLIDOS */
	apellidos = document.getElementById("apellidos").value;
	apellidos=apellidos.trim();
	apellidosCorrectos=false;
	expRegApellidos = /^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\s|\ç|\Ç|\-]*)+$/;
	tipo=document.getElementById("tipo").value;
	
	/* Para que manitas pueda dejar los apellidos vacios si asi lo desea" */
	/* PROBLEMA, EL CAMPO ESTÁ EN REQUERIDO EN EL HTML Y NO SE PUEDE DEJAR EN VACIO
	* SE PODRIA SOLUCIONAR FACILMENTE CON THYMELEAF
	*/
	if(tipo=="manitas"){
		if(expRegApellidos.test(apellidos) || apellidos==""){
				apellidosCorrectos=true;
				//document.getElementById("nombre").classList.remove("alert-danger");
				divsError[1].hidden=true;
		}
		
		else {
				
			divsError[1].hidden=false;
			//document.getElementById("apellidos").classList.add("alert-danger");
			divsError[1].innerHTML="Apellidos incorrectos, ha introducido caracteres no permitidos.";	
			}
			
	}
	else {
		if (apellidos.length>=8 && apellidos.length<50) {
				if(expRegApellidos.test(apellidos)){
					apellidosCorrectos=true;
					divsError[1].hidden=true;
					//document.getElementById("nombre").classList.remove("alert-danger");
				}
				else {
					
				divsError[1].hidden=false;
				//document.getElementById("apellidos").classList.add("alert-danger");
				divsError[1].getElementsByTagName('span')[0].innerHTML="Apellidos incorrectos, ha introducido caracteres no permitidos.";
					
				}
			}
			else {
				divsError[1].hidden=false;
				//document.getElementById("apellidos").classList.add("alert-danger");
				divsError[1].getElementsByTagName('span')[0].innerHTML="Apellidos incorrectos, los apellidos deben contener entre 8 y 50 caracteres.";
				
			}
	}
	
	/* VALIDAR EMAIL */
	email = document.getElementById("email").value;
	emailCorrecto=false;
	expRegEmail= /^([\w]+[\.|\_|\-|\@]*)+@{1}[\w]+(\.+[a-z]{2,3})+$/;
	if(expRegEmail.test(email)){
				emailCorrecto=true;
				//document.getElementById("email").classList.remove("alert-danger");
				divsError[2].hidden=true;
			}
			else {
				
			divsError[2].hidden=false;
			//document.getElementById("email").classList.add("alert-danger");
			divsError[2].getElementsByTagName('span')[0].innerHTML="Email incorrecto.";
				
			}
	
	/* VALIDAR TELEFONO */
	tlf = document.getElementById("tlf").value;
	telefonoCorrecto=false;
	expRegTelefono =/^[9|6]{1}([\d]{2}[-|\s]*){3}[\d]{2}$/;
	
	if (expRegTelefono.test(tlf)){
		telefonoCorrecto=true;
		//document.getElementById("tlf").classList.remove("alert-danger");
		divsError[3].getElementsByTagName('span')[0].hidden=true;
	}
	else {
		divsError[3].hidden=false;
		//document.getElementById("tlf").classList.add("alert-danger");
		divsError[3].getElementsByTagName('span')[0].innerHTML="Telefono incorrecto. Debe comenzar por 9 ó 6 y debe contener 9 cifras";
		
	}
	
	
	/* VALIDAR CONTRASEÑA */
	/* Aqui pedimos una contraseña con mayuscula, caracter especial y numero pero
	* podría ser buena opcion no exigirlo sino ir sumando "seguridad" segun se van introduciendo esos caracteres
	* e informar al usuario del nivel de seguridad de su contraseña
	*/
	pwd = document.getElementById("password").value;
	pwdCorrecta=false;
	expRegPwd = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])([A-Za-z\d$@$!%*?&]|[^ ]){8,15}$/;
	
	if (expRegPwd.test(pwd)){
		pwdCorrecta=true;
		//document.getElementById("password").classList.remove("alert-danger");
		divsError[4].hidden=true;
	}
	else {	
		divsError[4].hidden=false;
		//document.getElementById("password").classList.add("alert-danger");
		divsError[4].getElementsByTagName('span')[0].innerHTML="Contraseña incorrecta. Debe contener entre 8 y 15 caracteres y al menos mayusculas, cifras y caracteres especiales.";
		
	}
	
	/* CONFIRMAR PWD */
	pwd2 = document.getElementById("password-confirm").value;
	pwdConfirm=false;

	if(pwd == pwd2){
		divsError[5].hidden=true;
		//document.getElementById("password-confirm").classList.remove("alert-danger");
		pwdConfirm=true;
	}
	else {
		
		divsError[5].hidden=false;
		//document.getElementById("password-confirm").classList.add("alert-danger");
		divsError[5].getElementsByTagName('span')[0].innerHTML="Las contraseñas no coinciden.";

	}
	
	if (nombreCorrecto && apellidosCorrectos && emailCorrecto && telefonoCorrecto && pwdCorrecta && pwdConfirm){
		return true;                                                                         
	}
	else {
		if (nombreCorrecto==false){
			document.getElementById("nombre").focus();
		}
		else {
			if(apellidosCorrectos==false){
				document.getElementById("apellidos").focus();
			}
			else {
				if(emailCorrecto==false){
					document.getElementById("email").focus();
				}
				else {
					if(telefonoCorrecto==false){
						document.getElementById("tlf").focus();
					}
					else {
						if(pwdCorrecta==false){
							document.getElementById("password").focus();
						}
						else {
							document.getElementById("password-confirm").focus();
						}
					}
				}
			}
		}
		return false;
	}
	
	return false;
}
function validar(form) {
	var salida=false;
	/*
	 * CON EL TRIM NO FUNCIONA AL REGISTRAR!!
	 if(validarCampos(form)&&trim(form)&&comparacontraseña(form)){
		codContraseña();
		salida=true;
	}
	 */
	if(validarCampos(form)&&comparacontraseña(form)){
		codContraseña();
		salida=true;
	}
	return salida;
}


function codContraseña() {//modificar para que acepte varios campos password en una pagina
	var campo = document.getElementsByName("passwordsin")[0];
	var campo2 = document.getElementsByName("passwordactual")[0];
	if(campo!=null){
		document.getElementsByName("password")[0].value = shake_256(campo.value,1023);
		campo.disabled = true;
	}
	if(campo2!=null){
		document.getElementsByName("passwordactualhash")[0].value = shake_256(campo2.value,1023);
		campo2.disabled = true;
	}
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
function comparacontraseña(form){
	var salidacontraseña=false;
	var confirmacion=document.getElementsByName("password-confirmation");
	var passwordsin=document.getElementsByName("passwordsin");
	if(confirmacion[0]==null)
		salidacontraseña=true;
	else if(confirmacion[0].value==passwordsin[0].value){
		salidacontraseña=true;
		confirmacion[0].disabled="true";
	}
	return salidacontraseña;
}

var recarga=setInterval('location.reload()',901000);

window.onload=recarga;

/*maps*/


var geocoder;
var map;
var markers = [];

function initialize() {
   var query="España";
   geocoder = new google.maps.Geocoder;
   var infowindow = new google.maps.InfoWindow;

   
   var mapOptions = {
    zoom:5,
    mapTypeId:google.maps.MapTypeId.roadmap  ,
    streetViewControl:false
  }
  
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  codeAddress(query);
  google.maps.event.addListener(map, 'click', function(event) {       
});

        map.addListener('click', function(event) {
            setMapOnAll(null);
            markers = [];
          addMarker(event.latLng);
        map.panTo(event.latLng);
		document.getElementById("direccion").disabled = false;
        });
      function addMarker(location) {
        var marker = new google.maps.Marker({
          position: location,
          map: map
        });
        markers.push(marker);

        document.getElementById("coordenadas").value=markers[0].position;
      }

      
      function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(map);
        }
      }

     
}
function addMarker(location) {
	  setMapOnAll(null);
      markers = [];
  var marker = new google.maps.Marker({
    position: location,
    map: map
  });
  markers.push(marker);
}
function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  }
function codeAddress(direccion) {
	  
	  var address = direccion;
	  geocoder.geocode( { 'address': address}, function(results, status) {
	    if (status == google.maps.GeocoderStatus.OK) {
	      map.setCenter(results[0].geometry.location);
	      addMarker(results[0].geometry.location);
	    } else {
	      errorPosicionar(status);
	    }
	  });
	}
function codeAddress2(direccion) {
  
  var address = direccion;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      addMarker(results[0].geometry.location);
      document.getElementById("coordenadas").value=markers[0].position;
      document.getElementById("direccion").disabled = false;
    } else {
      errorPosicionar(status);
    }
  });
  
}
function buscarCalle(){
    codeAddress2(document.getElementById("direccion").value);
    map.zoom=15;
}
function errorPosicionar(error) {
    switch(error.code)  
    {  
        case error.TIMEOUT:  
            alert('Request timeout');  
        break;  
        case error.POSITION_UNAVAILABLE:  
            alert('Tu posición no está disponible');  
        break;  
        case error.PERMISSION_DENIED:  
            alert('Tu navegador ha bloqueado la solicitud de geolocalización');  
        break;  
        case error.UNKNOWN_ERROR:  
            alert('Error desconocido');  
        break;  
    }  
}



/* Convertir coordenadas a una dirección en formato texto */
function convertirDireccion(coordenadas,campo) {
	var direccion = "";
	
	  var latlngStr = coordenadas.split(',', 2);
	  latlngStr[0] = latlngStr[0].substr(1,);
	  latlngStr[1] = parseFloat(latlngStr[1].substr(0,latlngStr[1].length-1));
	  var geocoder = new google.maps.Geocoder;
	  var point = new google.maps.LatLng(latlngStr[0], latlngStr[1]);
	  geocoder.geocode({'latLng': point}, function(results, status) {
	    if (status === 'OK') {
	    	
	    	direccion=results[1].formatted_address;
    		sacarDatos(direccion,campo);
	    	if (results[1]) {
	      } 
	    } 
	  });
}

/* Sacar datos de ConvertirDireccion y guardarlos en el input que enviará
 * la dirección
 */
function sacarDatos(datos,campo){
	 escribirDireccion(datos,campo);
}

function escribirDireccion(datos,campo){
	if(campo) {
		campo.innerHTML = "<i class='fa fa-map-marker ' aria-hidden='true'></i>" + " Ubicación: " + datos;
	}
}


/* ordenacion de tablas */
function sortTable(objeto,n) {
	var tipo;
	var ths=document.getElementById("myTable").getElementsByTagName("th");
	for(var a=0;a<ths.length;a++){
		sada=ths[a].getElementsByTagName("a").length;
		if(ths[a].getElementsByTagName("a").length>0){
			ths[a].getElementsByTagName("a")[0].className="fa fa-sort";
			if(a == n)
				ths[a].getElementsByTagName("a")[0].className="dropdown-toggle";
		}
	}
	var valor=objeto.attributes.getNamedItem("aria-expanded");
	if(valor.value=="false"){
		valor.value="true";
		tipo='desc';
	}
	else{
		valor.value="false";
		tipo='asc';
	}
	sortTable2(tipo, n);
}

function sortTable2(tipo, n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("myTable");
    switching = true;
  //Set the sorting direction to ascending:
  if(tipo=="asc"){
    dir = "asc"; 
  }
  else if(tipo=="desc"){
    dir = "desc"; 
  }

  while (switching) {
    
    switching = false;
    rows = table.getElementsByTagName("TR");
    
    for (i=1;i<rows.length-1;i++) {
      
      shouldSwitch = false;
     
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];
      
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
          
          shouldSwitch= true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
         
          shouldSwitch= true;
          break;
        }
      }
    }
    if (shouldSwitch) {
      
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
     
      switchcount ++;      
    } 
  }
}

/*filtrado de filas pruebas*/
function filtrarFila(input,columna){
	 var table = document.getElementById("myTable");
	 var textofiltro =input.value.trim().toLowerCase();
	 var arrayFiltros = textofiltro.split(" ");
	 var fin=true;
	 var rows = table.getElementsByTagName("TR");
		    for (var i=1;i<rows.length;i++) {
		    	for(var a=0;a<arrayFiltros.length;a++){
		    	if(rows[i].getElementsByTagName("TD")[columna].innerHTML.toLowerCase().includes(arrayFiltros[a].trim())){
		    		rows[i].style="";
		    		a=arrayFiltros.length;
		    	}
		    	else
		    		rows[i].style="display:none";
		    	}
		    }
}


//Opción de búsqueda en las tablas
$(document).ready(function(){
  $(".form-control.busqueda").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $(".myTable tr").filter(function() {
      $(this).toggle($(this).find("td:not(:last-child)").text().toLowerCase().indexOf(value.trim()) > -1)
    });
  });
});


/*ajax*/
function peticionAjax(direccion,method="GET",funcionRespuesta=null,formulario=null){
	var sFormulario="";
	if(formulario!=null){
		validar(formulario)
		if(method=="GET") sFormulario="?";
		sFormulario+=$(formulario).serialize();
	}
	var conexion;
	if (window.XMLHttpRequest) {
	    conexion = new XMLHttpRequest();
	 }
	else {
	    conexion = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if(method=="GET")
		conexion.open('GET', direccion+sFormulario, true);
	else if(method=="POST")
		conexion.open('POST', direccion, true);
	conexion.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
	conexion.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	if(formulario==null)
		conexion.send();
	else if(method=="GET"&&formulario!=null&&validar(formulario))
		conexion.send();
	else if(method=="POST"&&formulario!=null&&validar(formulario))
		conexion.send(sFormulario);
	conexion.onreadystatechange = function() {
		if (conexion.readyState==4 && conexion.status==200) {
			clearInterval(recarga);
			recarga=setInterval('location.reload()',901000);
			if(funcionRespuesta!=null)
				funcionRespuesta(conexion);
		}
	}
}

/*no en uso*/
function mostrarJSONConsola(conexion){
	var arraymensajes=JSON.parse(conexion.responseText.replace(/\n/g,"/n").replace(/\r/g,"/r").replace(/[,][{][}]/g,"").replace(/[{][}]/g,""));
	console.log(arraymensajes[arraymensajes.length-1]);
	console.log(JSON.stringify(arraymensajes));
}
/*para hacer pruebas*/
function mostrarDatosConsola(conexion){
	console.log(conexion.responseText);
}/*para hacer pruebas*/
function mostrarXMLConsola(conexion){
	console.log(conexion.responseXML);
}
/*funcionando*/
function crearTablaMensajes(conexion){
	mensajesSinLeer=0;
	var xml=new DOMParser().parseFromString(conexion.responseText,"text/xml");
	var mensajes=xml.getElementsByTagName("mensajes")[0];
	if(mensajes.children.length<1){
		sTable="<h3>No hay ningun mensaje</h3>";
	}
	else{
	sTable="<table class='table table-striped'>";
	sTable+="<tr>";
	for(var i=2;i< mensajes.children[0].children.length-1;i++){
		sTable+="<th>"+ mensajes.children[0].children[i].nodeName.charAt(0).toUpperCase()+mensajes.children[0].children[i].nodeName.slice(1)+"</th>";
	}
	sTable+="<th>Acciones</th>";
	sTable+="</tr>";
	for(var a=0;a< mensajes.children.length;a++){
		sTable+="<tr>";
		for(var i=2;i< mensajes.children[a].children.length;i++){
			if(mensajes.children[a].children[i].nodeName=="mensaje"||mensajes.children[a].children[i].nodeName=="remitente"){
				if(mensajes.children[a].children[i].innerHTML.length>10)
					sTable+="<td>"+ mensajes.children[a].children[i].innerHTML.substr(0,7)+"...</td>";
				else
					sTable+="<td>"+ mensajes.children[a].children[i].innerHTML+"</td>";
			}
			else if(mensajes.children[a].children[i].nodeName=="leido"){
				if(mensajes.children[a].children[i].innerHTML=="false"){
					mensajesSinLeer++;
				}
			}
			else
				sTable+="<td>"+ mensajes.children[a].children[i].innerHTML+"</td>";
		}
		sTable+='<td class="acciones">';
		sTable+='<a href="/mensaje/ver?id='+mensajes.children[a].children[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-envelope"></span></a>';
		sTable+='</td>';
		sTable+="</tr>";
	}
	sTable+="</table>";
	}
	document.getElementById("tablaDatos").innerHTML=sTable;
	document.getElementById("botonRotatorio").children[0].className='fa fa-refresh';
	try {
		clearInterval(cambiotitulo);
	}catch(err) {}
	cambiotitulo=setInterval(function(){if(document.title=="Manitas Home")document.title="Lista De Mensajes"+((mensajesSinLeer>0)?"("+mensajesSinLeer+" sin leer)":"");else document.title="Manitas Home";},1500);
}

function crearTablaCategoriaOEmpleo(conexion){
	var xml=new DOMParser().parseFromString(conexion.responseText,"text/xml");
	var catemp=xml.getElementsByTagName("categorias")[0];
	if(catemp==null)
	catemp=xml.getElementsByTagName("empleos")[0];
	if(catemp.children.length<1){
		sTable="<h3>No hay "+catemp.nodeName+"</h3>";
	}
	else{
	sTable="<table class='table table-striped'>";
	sTable+="<tr>";
	for(var i=1;i< catemp.children[0].children.length;i++){
		sTable+="<th>"+ catemp.children[0].children[i].nodeName.charAt(0).toUpperCase() + catemp.children[0].children[i].nodeName.slice(1)+"</th>";
	}
	sTable+="<th>Acciones</th>";
	sTable+="</tr>";
	for(var a=0;a< catemp.children.length;a++){
		sTable+="<tr>";
		for(var i=1;i< catemp.children[a].children.length;i++){
			sTable+="<td>"+ catemp.children[a].children[i].innerHTML+"</td>";
		}
		sTable+='<td class="acciones">';

		sTable+='<a href="/'+catemp.children[0].nodeName+'/modificar?id='+catemp.children[a].children[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-pencil"></span></a> ';
		sTable+='<a href="/'+catemp.children[0].nodeName+'/borrar?id='+catemp.children[a].children[0].innerHTML+'" onclick="return confirm(\'¿Desea borrarlo?\')" class="btn btn-info" role="button"><span class="fa fa-trash"></span></a>';
		sTable+='</td>';
		sTable+="</tr>";
	}
	sTable+="</table>";
	}
	document.getElementById("tablaDatos").innerHTML=sTable;
	document.getElementById("botonRotatorio").children[0].className='fa fa-refresh';
	try {
		clearInterval(cambiotitulo);
	}catch(err) {}
	cambiotitulo=setInterval(function(){if(document.title=="Manitas Home")document.title="Lista De "+catemp.nodeName;else document.title="Manitas Home";},1500);
}

function crearTablaAdministradores(conexion){
	var xml=new DOMParser().parseFromString(conexion.responseText,"text/xml");
	var administradores=xml.getElementsByTagName("administradores")[0];
	var emailactual=xml.getElementsByTagName("emailactual")[0].innerHTML;
	if(administradores.children.length<1){
		sTable="<h3>No hay "+administradores.nodeName+"</h3>";
	}
	else{
	sTable="<table class='table table-striped'>";
	sTable+="<tr>";
	for(var i=1;i< administradores.children[0].children.length;i++){
		sTable+="<th>"+ administradores.children[0].children[i].nodeName.charAt(0).toUpperCase() + administradores.children[0].children[i].nodeName.slice(1)+"</th>";
	}
	sTable+="<th>Acciones</th>";
	sTable+="</tr>";
	for(var a=0;a< administradores.children.length;a++){
		if(administradores.children[a].getElementsByTagName("email")[0].innerHTML!=emailactual){
			sTable+="<tr>";
			for(var i=1;i< administradores.children[a].children.length;i++){
				sTable+="<td>"+ administradores.children[a].children[i].innerHTML+"</td>";
			}
			sTable+='<td class="acciones">';
			sTable+='<a href="/mensaje/crear?emaildestinatario='+administradores.children[a].getElementsByTagName("email")[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-envelope"></span></a> ';
			if(xml.getElementsByTagName("superadmin")[0].innerHTML=="true"){
				sTable+='<a href="/'+administradores.children[0].nodeName+'/modificar?id='+administradores.children[a].children[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-pencil"></span></a> ';
				sTable+='<a href="/'+administradores.children[0].nodeName+'/borrar?id='+administradores.children[a].children[0].innerHTML+'" onclick="return confirm(\'¿Desea borrarlo?\')" class="btn btn-info" role="button"><span class="fa fa-trash"></span></a>';
			}
			sTable+='</td>';
			sTable+="</tr>";
		}
	}
	sTable+="</table>";
	}
	document.getElementById("tablaDatos").innerHTML=sTable;
	document.getElementById("botonRotatorio").children[0].className='fa fa-refresh';
	try {
		clearInterval(cambiotitulo);
	}catch(err) {}
	cambiotitulo=setInterval(function(){if(document.title=="Manitas Home")document.title="Lista De "+administradores.nodeName;else document.title="Manitas Home";},1500);
}
function crearTablaClientes(conexion){
	var xml=new DOMParser().parseFromString(conexion.responseText,"text/xml");
	var clientes=xml.getElementsByTagName("clientes")[0];
	if(clientes.children.length<1){
		sTable="<h3>No hay "+clientes.nodeName+"</h3>";
	}
	else{
	sTable="<table class='table table-striped'>";
	sTable+="<tr>";
	for(var i=1;i< clientes.children[0].children.length;i++){
		sTable+="<th>"+ clientes.children[0].children[i].nodeName.charAt(0).toUpperCase() + clientes.children[0].children[i].nodeName.slice(1)+"</th>";
	}
	sTable+="<th>Acciones</th>";
	sTable+="</tr>";
	for(var a=0;a< clientes.children.length;a++){
			sTable+="<tr>";
			for(var i=1;i< clientes.children[a].children.length;i++){
				sTable+="<td>"+ clientes.children[a].children[i].innerHTML+"</td>";
			}
			sTable+='<td class="acciones">';
			sTable+='<a href="/mensaje/crear?emaildestinatario='+clientes.children[a].getElementsByTagName("email")[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-envelope"></span></a> ';
			sTable+='<a href="/'+clientes.children[0].nodeName+'/modificar?id='+clientes.children[a].children[0].innerHTML+'" class="btn btn-info" role="button"><span class="fa fa-pencil"></span></a> ';
			sTable+='<a href="/'+clientes.children[0].nodeName+'/borrar?id='+clientes.children[a].children[0].innerHTML+'" onclick="return confirm(\'¿Desea borrarlo?\')" class="btn btn-info" role="button"><span class="fa fa-trash"></span></a>';
			sTable+='</td>';
			sTable+="</tr>";
	}
	sTable+="</table>";
	}
	document.getElementById("tablaDatos").innerHTML=sTable;
	document.getElementById("botonRotatorio").children[0].className='fa fa-refresh';
	try {
		clearInterval(cambiotitulo);
	}catch(err) {}
	cambiotitulo=setInterval(function(){if(document.title=="Manitas Home")document.title="Lista De "+clientes.nodeName;else document.title="Manitas Home";},1500);
}
function resultadoCrearCategoriaOEmpleo(conexion){
	var respuesta=conexion.responseText;
	var divrespuesta=document.getElementById("resultado");
	if (respuesta=="OK"){
		divrespuesta.innerHTML="La operación se ha efectuado correctamente";
		divrespuesta.className="alert alert-success";
	}
	else if(respuesta.includes("ERROR")){
		divrespuesta.innerHTML=respuesta;
		divrespuesta.className="alert alert-danger";
	}
	setTimeout(function(){ divrespuesta.innerHTML=""; divrespuesta.className=""; }, 5000);	
	if(respuesta=="OK"){
		window.setTimeout(function () {
		      $('#crear').modal('hide');
		}, 2000);
		window.setTimeout(function () {
			window.location.replace("listar");
		}, 2000);
	
	}
	             
}
function resultadoModificarCategoriaOEmpleo(conexion){
	var respuesta=conexion.responseText;
	
	if (respuesta=="OK"){
		$('.resultadoModificar').removeClass("alert alert-danger");
		$('.resultadoModificar').html('La operación se ha efectuado correctamente');
		$('.resultadoModificar').addClass("alert alert-success");
		
	}
	else if(respuesta.includes("ERROR")){
		$(".resultadoModificar").removeClass("alert alert-success");
		$('.resultadoModificar').html(respuesta);
		$('.resultadoModificar').addClass("alert alert-danger");
	}
	setTimeout(function(){ 
			$('.resultadoModificar').html(""); 
			$(".resultadoModificar").removeClass("alert alert-success");
			$('.resultadoModificar').removeClass("alert alert-danger");
		}, 5000);
	
	if(respuesta=="OK"){
		window.setTimeout(function () {
		      $('.modalesModificar').modal('hide');
		}, 2000);
		window.setTimeout(function () {
			window.location.replace("listar");
		}, 2000);
	}

	             
}
function resultadoLogin(conexion){
	var respuesta=conexion.responseText;
	var divrespuesta=document.getElementById("resultado");
	if(respuesta=="OK")
		location.reload();
	else if(respuesta.includes("ERROR")){
		divrespuesta.innerHTML=respuesta;
		divrespuesta.className="alert alert-danger";
		formLogin.passwordsin.disabled=false;
	}
	setTimeout(function(){ divrespuesta.innerHTML=""; divrespuesta.className=""; }, 5000);
}

/*Validaciones*/
function validarCampos(form) {
	/* Divs para mostrar los errores */
	divsError=form.getElementsByClassName('form-control-feedback');
	
	
	
	/* VALIDAR NOMBRE */
	
	if (form['nombre']){
	nombre = form['nombre'].value;
	nombre=nombre.trim();
	form['nombre'].value = nombre;
	nombreCorrecto=false;
	expRegNombre = /^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\s|\ç|\Ç|\-]*)+$/;
	
		if (nombre.length>=3 && nombre.length<30) {
			if(expRegNombre.test(nombre)){
				nombreCorrecto=true;
				if(divsError['errorNombre']){
					divsError['errorNombre'].hidden=true;
				}
			}
			else {
				if(divsError['errorNombre']){
					divsError['errorNombre'].hidden=false;
					divsError['errorNombre'].getElementsByTagName('span')[0].innerHTML="Nombre incorrecto, ha introducido caracteres no permitidos.";
				}
			}
		}
		else {
			if(divsError['errorNombre']){
				divsError['errorNombre'].hidden=false;
				divsError['errorNombre'].getElementsByTagName('span')[0].innerHTML="Nombre incorrecto, el nombre debe contener entre 3 y 30 caracteres.";
			}
		}
		
	}
	
		
	/* VALIDAR APELLIDOS */
	if (form['apellidos']){
	apellidos = form['apellidos'].value;
	apellidos=apellidos.trim();
	form['apellidos'].value = apellidos;
	apellidosCorrectos=false;
	expRegApellidos = /^(([A-ZÑÁÉÍÓÚ]|[a-zñáéíóú]|[ÄËÏÖÜäëïöü]){3,}[\s|\ç|\Ç|\-]*)+$/;
	tipo="";
	if(form['tipo']) {
		tipo=form['tipo'].value;
	}
	
	
	/* Para que manitas pueda dejar los apellidos vacios si asi lo desea" */
	/* PROBLEMA, EL CAMPO ESTÁ EN REQUERIDO EN EL HTML Y NO SE PUEDE DEJAR EN VACIO
	* SE PODRIA SOLUCIONAR FACILMENTE CON THYMELEAF
	*/
	if(tipo=="manitas"){
		if(expRegApellidos.test(apellidos) || apellidos==""){
				apellidosCorrectos=true;
				if(divsError['errorApellidos']){
					divsError['errorApellidos'].hidden=true;
				}
		}
		
		else {
			if(divsError['errorApellidos']){
				divsError['errorApellidos'].hidden=false;
				divsError['errorApellidos'].innerHTML="Apellidos incorrectos, ha introducido caracteres no permitidos.";	
				}
			}
			
	}
	else {
		if (apellidos.length>=8 && apellidos.length<50) {
				if(expRegApellidos.test(apellidos)){
					apellidosCorrectos=true;
					if(divsError['errorApellidos']){
						divsError['errorApellidos'].hidden=true;
					}
				}
				else {
					if(divsError['errorApellidos']){
						divsError['errorApellidos'].hidden=false;
						divsError['errorApellidos'].getElementsByTagName('span')[0].innerHTML="Apellidos incorrectos, ha introducido caracteres no permitidos.";
					}
				}
			}
			else {
				if(divsError['errorApellidos']){
					divsError['errorApellidos'].hidden=false;
					divsError['errorApellidos'].getElementsByTagName('span')[0].innerHTML="Apellidos incorrectos, los apellidos deben contener entre 8 y 50 caracteres.";
				}
			}
	}
	}
	
	
	/* VALIDAR EMAIL */
	if (form['email']){
	email = form['email'].value;
	emailCorrecto=false;
	expRegEmail= /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if(expRegEmail.test(email)){
				emailCorrecto=true;
				if(divsError['errorEmail']){
					divsError['errorEmail'].hidden=true;
				}
				
			}
			else {
			if(divsError['errorEmail']){	
				divsError['errorEmail'].hidden=false;
				divsError['errorEmail'].getElementsByTagName('span')[0].innerHTML="Email incorrecto.";
				}
			}
	}
	
	
	/* VALIDAR TELEFONO */
	if (form['telefono']){
	tlf = form['telefono'].value;
	telefonoCorrecto=false;
	expRegTelefono =/^[9|6]{1}([\d]{2}[-|\s]*){3}[\d]{2}$/;
	
	if (expRegTelefono.test(tlf)){
		telefonoCorrecto=true;
		if(divsError['errorTelefono']){
			divsError['errorTelefono'].hidden=true;
		}
	}
	else {
		if(divsError['errorTelefono']){
			divsError['errorTelefono'].hidden=false;
			divsError['errorTelefono'].getElementsByTagName('span')[0].innerHTML="Telefono incorrecto. Debe comenzar por 9 ó 6 y debe contener 9 cifras";
		}
	}
	}
	
	
	/* VALIDAR CONTRASEÑA */
	/* Aqui pedimos una contraseña con mayuscula, caracter especial y numero pero
	* podría ser buena opcion no exigirlo sino ir sumando "seguridad" segun se van introduciendo esos caracteres
	* e informar al usuario del nivel de seguridad de su contraseña
	*/
	expRegPwd = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])([A-Za-z\d$@$!%*?&]|[^ ]){8,15}$/;
	if (form['passwordsin']){
	pwd = form['passwordsin'].value;
	pwdCorrecta=false;
	
	
	if (expRegPwd.test(pwd)){
		pwdCorrecta=true;
		if(divsError['errorPwd']){
			divsError['errorPwd'].hidden=true;
		}
		
	}
	else {	
		if(divsError['errorPwd']){
			divsError['errorPwd'].hidden=false;
			divsError['errorPwd'].getElementsByTagName('span')[0].innerHTML="Contraseña incorrecta. Debe contener entre 8 y 15 caracteres y al menos mayusculas, cifras y caracteres especiales.";
		}
	}
	}
	
	/* CONFIRMAR PWD */
	if (form['password-confirmation']){
	pwd2 = form['password-confirmation'].value;
	pwdConfirm=false;
	
	

	if(pwd == pwd2){
		if(divsError['errorPwdConfirm']){
			divsError['errorPwdConfirm'].hidden=true;
		}
		pwdConfirm=true;
	}
	else {
		if(divsError['errorPwdConfirm']){
			divsError['errorPwdConfirm'].hidden=false;
		divsError['errorPwdConfirm'].getElementsByTagName('span')[0].innerHTML="Las contraseñas no coinciden.";
		}
		
	}
	}
	
	//COINCIDIR PASSWORD ACTUAL
	if (form['passwordactual']){
		pwdActual = form['passwordactual'].value;
		pwdNueva=false;
		
		if(pwdActual == pwd || pwdActual == pwd2){
			if(divsError['errorPwdActual']){
				divsError['errorPwd'].hidden=false;
				divsError['errorPwd'].getElementsByTagName('span')[0].innerHTML="La nueva contraseña no puede coincidir con la anterior.";
			}
		
		}
		else {
			if (expRegPwd.test(pwdActual)){
				pwdNueva=true;
				if(divsError['errorPwdActual']){
					divsError['errorPwdActual'].hidden=true;
				}
			}
			
			else {
				if(divsError['errorPwdActual']){
					divsError['errorPwdActual'].hidden=false;
					divsError['errorPwdActual'].getElementsByTagName('span')[0].innerHTML="Contraseña incorrecta. Debe contener entre 8 y 15 caracteres y al menos mayusculas, cifras y caracteres especiales.";
				}
			}
		}
	}
	
	
	// RADIO DE ACCION
	if (form['tipo']){
		if(form['tipo'].value=="cliente") {
			
			divsError['errorRadioAccion'].hidden=true;
		}
		else {
			if (form['radio']){
				radio = form['radio'].value;
				radioCorrecto=false;
				if(radio<1 || radio>1000){
						if(divsError['errorRadioAccion']){
							if(radio<1){
								divsError['errorRadioAccion'].hidden=false;
								divsError['errorRadioAccion'].getElementsByTagName('span')[0].innerHTML="Rango incorrecto, no puedo introducir un rango inferior a 1.";
							}
							else {
								divsError['errorRadioAccion'].hidden=false;
								divsError['errorRadioAccion'].getElementsByTagName('span')[0].innerHTML="Rango incorrecto, no puedo introducir un rango superior a 1000.";
							}
					}
				}
				
				else{
					if(divsError['errorRadioAccion']){
						divsError['errorRadioAccion'].hidden=true;
						radioCorrecto=true;
					}
				}
			}
		}
	}
	

	
	
	
	
	if ( ( (typeof nombreCorrecto == 'undefined') || (nombreCorrecto == true) ) &&
		( (typeof apellidosCorrectos == 'undefined') || (apellidosCorrectos == true) ) &&
		( (typeof emailCorrecto == 'undefined') || (emailCorrecto == true) ) &&
		( (typeof telefonoCorrecto == 'undefined') || (telefonoCorrecto == true) ) && 
		( (typeof pwdCorrecta == 'undefined') || (pwdCorrecta == true) ) &&
		( (typeof pwdConfirm == 'undefined') || (pwdConfirm == true) ) &&
		( (typeof radioCorrecto == 'undefined') || (radioCorrecto == true) ) &&
		( (typeof pwdNueva == 'undefined') || (pwdNueva == true) )
	){
		return true;                                                                         
	}
	else {
		if ((typeof nombreCorrecto !== 'undefined') && (nombreCorrecto==false)){
			form['nombre'].focus();
		}
		else {
			if( (typeof apellidosCorrectos !== 'undefined') && (apellidosCorrectos==false)){
				form['apellidos'].focus();
			}
			else {
				if((typeof emailCorrecto !== 'undefined') && emailCorrecto==false){
					form['email'].focus();
				}
				else {
					if((typeof telefonoCorrecto !== 'undefined') && telefonoCorrecto==false){
						form['tlf'].focus();
					}
					else {
						if((typeof pwdCorrecta !== 'undefined') && pwdCorrecta==false){
							form['passwordsin'].focus();
						}
						else {
							if((typeof pwdConfirm !== 'undefined') && pwdConfirm==false){
								form['password-confirmation'].focus();
							}
							else {
								if((typeof pwdNueva !== 'undefined') && pwdNueva==false){
									form['passwordactual'].focus();
								}
								else {
									form['radio'].focus();
								}
								
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	return false;
}


/*--------------------------------------------------------------OFUSCADOR DE JS---------------------------https://javascriptobfuscator.com/Javascript-Obfuscator.aspx-------------*/
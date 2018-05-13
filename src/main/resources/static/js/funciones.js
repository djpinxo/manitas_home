function validar(form) {
	var salida=false;
	if(trim(form)&&comparacontraseña(form)){
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

window.onload=setInterval('location.reload()',901000);




/*maps*/


var geocoder;
var map;
var markers = [];

function initialize() {
   var query="España";
   geocoder = new google.maps.Geocoder();
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

/*ajax*/
function peticionAjax(direccion,method="GET",funcionRespuesta=null,formulario=null){
	var sFormulario="";
	if(formulario!=null){
		if(method="GET") sFormulario="?";
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
		conexion.open('GET', direccion, true);
	conexion.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
	conexion.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	if(method=="GET")
		conexion.send();
	else if(method=="POST")
		conexion.send(sFormulario);
	conexion.onreadystatechange = function() {
		if (conexion.readyState==4 && conexion.status==200) {
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
/*--------------------------------------------------------------OFUSCADOR DE JS---------------------------https://javascriptobfuscator.com/Javascript-Obfuscator.aspx-------------*/
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
			ths[a].getElementsByTagName("a")[0].className="fa fa-minus";
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
/*--------------------------------------------------------------OFUSCADOR DE JS---------------------------https://javascriptobfuscator.com/Javascript-Obfuscator.aspx-------------*/
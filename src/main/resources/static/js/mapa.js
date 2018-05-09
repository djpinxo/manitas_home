

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
      }

      
      function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(map);
        }
      }

     
}

function codeAddress(direccion) {
  
  var address = direccion;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
    } else {
      errorPosicionar(status);
    }
  });
}
function buscar(){
    codeAddress(document.getElementById("direccion").value);
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
google.maps.event.addDomListener(window, 'load', initialize);
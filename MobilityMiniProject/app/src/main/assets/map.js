function getLocations(id){
  if (Array.isArray(id)){
      var res = [];
      id.forEach(function(entry) {
        if(Array.isArray(entry)){
            entry.forEach(function(subentry){
              res.push(featureGroup.getLayer(subentry+1000).getLatLng());
          });
        }else{
            res.push(featureGroup.getLayer(entry+1000).getLatLng());
        }
      });
      console.log(res);
      return res;
  }else{
    console.log("nop");
      return featureGroup.getLayer(id+1000).getLatLng();
  }
}


var map = L.map('mapid',{attributionControl: false}).setView([47.6409845,6.8616205], 13);


var mapboxlayer = L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibW90aXVyaW5mbyIsImEiOiJja2p6anUzN2owODJtMnFvNWVzZWdlMms5In0.xMNsZfdaIAV8n81W0TIreQ', {
  maxZoom: 10,
  id: 'mapbox/streets-v11'
});


var mapboxlightlayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibW90aXVyaW5mbyIsImEiOiJja2p6anUzN2owODJtMnFvNWVzZWdlMms5In0.xMNsZfdaIAV8n81W0TIreQ', {
      maxZoom: 10,
      id: 'mapbox.light'
  });

var osmlayer = L.tileLayer('https://{s}.tile.osm.org/{z}/{x}/{y}.png', {
          maxZoom: 10
  });

//map.addLayer(mapboxlightlayer);
map.addLayer(mapboxlayer);
//map.addLayer(osmlayer)
var featureGroup = L.geoJSON(features,{
onEachFeature: function (feature, layer) {
  layer.bindPopup('<h1>'+feature.properties.id+'</h1><p>name: '+feature.properties.name+'</p>');
  layer._leaflet_id = feature.properties.id + 1000;
  }
}).addTo(map);





function updateItineraries(start1, start2, start3, dest){

console.log(start1, start2, start3);

console.log(start1, start2, start3);

  var routing = L.Routing.control({
      waypoints: getLocations([start1,dest]),
      lineOptions: {styles: [{color: color1, opacity: .5, weight: 5}]},
      createMarker: function() { return null; },
       fitSelectedRoutes: false,
      show: false
  });

  var routing2 = L.Routing.control({
      waypoints: getLocations([start2,dest]),
      lineOptions: {styles: [{color: color2, opacity: .5, weight: 5}]},
      createMarker: function() { return null; },
       fitSelectedRoutes: false,
      show: false
  });

  var routing3 = L.Routing.control({
      waypoints: getLocations([start3,dest]),
      lineOptions: {styles: [{color: color3, opacity: .5, weight: 5}]},
      createMarker: function() { return null; },
       fitSelectedRoutes: false,
      show: false
  });

  routing.addTo(map);
  routing2.addTo(map);
  routing3.addTo(map);
}

//updateItineraries(1,[5,6],[1,8,5],4);
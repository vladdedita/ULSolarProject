<template>

  <div class="google-map" id="mapName"></div>
</template>

<script>
  import jQuery from 'jquery'
  export default {
    name: "googleMap",
    props: ['name'],

    methods :{
      loadJs(url, callback) {
        jQuery.ajax({
          url: url,
          dataType: 'script',
          success: callback,
          async: true
        })
      },
      initMap (){

        var self=this;
        this.loadJs("https://maps.googleapis.com/maps/api/js?key=AIzaSyCEa7UB7Y4uQpvQvD7a0SBwo7y8p91h6P8",function(){

          console.log("In callback...");
          const element = document.getElementById('mapName')
          const options = {
            zoom:14,
            center: new google.maps.LatLng(self.pos)
          }

          console.log("Instantiating map...");
          const map = new google.maps.Map (element,options);
          console.log("Instantiating infoWindow...");
          var infoWindow = new google.maps.InfoWindow;

          if(navigator.geolocation){
            console.log("Getting position...");
            navigator.geolocation.getCurrentPosition(function(position){
              self.pos = {
                  lat: position.coords.latitude,
                  lng: position.coords.longitude
                };
                console.log("Setting position..");
                // infoWindow.setPosition(self.pos);
                // infoWindow.setContent('Current location');
                // infoWindow.open(map);
                map.setCenter(self.pos);
                var marker = new google.maps.Marker ({
                  position: self.pos,
                  map:map,
                  draggable:true
                })
              },
              function() {
                console.log("Success handling..");
                this.handleLocationError(true,infoWindow,map.getCenter());
              });
          } else {

            console.log("Fail handling..");
            this.handleLocationError(false,infoWindow,map.getCenter());
          }
        })},

      handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
          'Error: The Geolocation service failed.' :
          'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
      }

    },

    mounted() {

      this.initMap()


    },
    data() {
      return {
        mapName: this.name+ "-map",
        apiKey : 'AIzaSyCEa7UB7Y4uQpvQvD7a0SBwo7y8p91h6P8',
        pos: {
          lat: 0,
          lng : 0
        }
      }
    }
  }
</script>

<style scoped>
  .google-map {
    width: 800px;
    height: 600px;

    margin: 0 auto;
    background: gray;
    border-radius: 3px;


  }
</style>

<template>
  <pageLayout>
    <!--<div id="divText">-->

    <!--</div>-->
    <div id="divMap">
      <p>Please choose your location:</p>
      <google-map @updatedLocation="updateLocation"></google-map>
          <button @submit.prevent="submitForm" @click="registerLocation()" id="registerButton">Register location</button>
        <input type="checkbox" id="checkbox" v-model="checked">
        <span>Make your location public?</span>


    </div>



  </pageLayout>
</template>

<script>
  import pageLayout from "../modals/page"
  import googleMap from "../modals/googleMap"
  import axios from 'axios'

  export default {
    name: "location-page",
    methods: {
      showMap: function () {
        console.log("Trying to show map ...");
      },
      showLocation() {
        console.log(this.latitude +"   "+ this.longitude)
      },
      registerLocation(){
        axios.post(window.ApiUrl + "setlocation/",{
          lat:this.latitude,
          lon:this.longitude,
          public:this.checked
        },{
          headers: {
            'Authorization': this.$store.getters.getKey
          }
        })
          .then(response => {
            if(response.data.succes)
              console.log(response.data.succes);
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
      updateLocation(value) {
        console.log(value);
        this.latitude=value.lat;
        this.longitude=value.lng;

        // //axios.get()
        // axios.get(/getSolarData/+ this.latitude + "/" + this.longitude)
        //   .then(response=>{
        //       console.log(response.data);
        //   })
        //   .catch(e=>{console.log("ERROR:",e);
        //   })

      }
    },
    mounted() {

    },
    components: {
      pageLayout,
      googleMap
    },
   data() {
      return {
        locations: [],
        longitude:0,
        latitude:0,
        checked:false
      }
  }
  }
</script>

<style scoped>
  p {
    font-family: 'Metrophobe',sans-serif;
    font-size:24px;
    color:white;

  }
  #divMap {
    /*float:right;*/
    margin:auto;
    overflow: visible;
  }
  #divMap > p {
    margin: auto;
    text-align: center;
  }
  #divText{
    float:left;

  }
  #registerButton {

    width:250px;
    height: 50px;
    display:block;
    margin: auto;
    margin-top: 20px;

    background: transparent;
    color:white;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    border-radius: 5px;
    font-size: 15px;
  }
  #registerButton:hover{
    background: lightblue;
    color:darkblue;
  }
  #checkbox {
    /*width:250px;*/
    /*height: 50px;*/
    /*margin: auto;*/
    /*margin-top: 20px;*/
    margin-left:40%;

  }

</style>

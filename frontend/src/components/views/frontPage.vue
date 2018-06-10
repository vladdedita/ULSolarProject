<template>

  <!--<div>-->
  <!--<full-page :options="options">-->
  <!--<div class = "section">-->
  <!--Home-->
  <!--</div>-->
  <!--<div class = "section">-->
  <!--Location-->
  <!--</div>-->
  <!--<div class = "section">-->
  <!--Statistics-->
  <!--</div>-->

  <!--</full-page>-->


  <!--</div>-->


 <pageLayout v-if="this.toBeShown">
    <div id="customInputDiv">
      <input v-model = "customDimension" placeholder="Desired dimension">
      <button v-on:click = "showCustomInputPanel">CHECK</button>
    </div>

    <solarpanel style="width:10%; height: 10%;" :dimension='0.014' :energy="this.energy" :lastEnergy="this.lastEnergy"></solarpanel>
    <solarpanel style="width:15%; height: 15%;" :dimension='8' :energy="this.energy" :lastEnergy="this.lastEnergy"></solarpanel>
    <solarpanel style="width:20%; height: 20%;" :dimension='14' :energy="this.energy" :lastEnergy="this.lastEnergy"></solarpanel>
    <solarpanel style="width:30%; height: 30%;" :dimension='21' :energy="this.energy" :lastEnergy="this.lastEnergy"></solarpanel>
    <solarpanel style="width:40%; height: 40%;" :dimension='28' :energy="this.energy" :lastEnergy="this.lastEnergy"></solarpanel>

  </pageLayout>

</template>

<script>

  //import Alert from '../Modals/alert.vue';

  import axios from 'axios';
  //import navbar from '../Modals/navbar.vue';
  import pageLayout from '../modals/page.vue'
  import solarpanel from "../modals/solarPanel";


  export default {
    name: 'front',

    // mounted(){
    //   this.authorize();
    // },
    components: {
      solarpanel,
      pageLayout
    },
    methods: {
      getMeasurements() {
        console.log("Inside measurements...")
        axios.post(window.ApiUrl + 'measurements/',{},{
        headers: {
          'Authorization': this.$store.getters.getKey,
        }
        })
          .then(response=>{
          //   console.log(response.data);
          //   let data = JSON.parse(response.data)
            console.log("Success1")
            this.energy=response.data.average;
            console.log("Success2")
            this.lastEnergy = response.data.last;
            console.log("Success3")
            this.toBeShown = true;
            console.log(this.energy)
          })
          .catch(e=>{console.log("ERROR:",e);
          })
      },
      showCustomInputPanel(){
        if(!isNaN(this.customDimension)) {
          this.$swal({
            title: "Solar panel",
            html:
            '<p>Dimension: ' + this.customDimension + ' m<sup>2</sup><br/>' +
            '<p>Last recorded energy output: ' + (((this.lastEnergy / 10000) * this.customDimension) / 0.014) * 60 + 'Wh</p>',
            // '<p>Estimated average per day: '+ this.getDailyEnergy() + ' KWh/day/m<sup>2</sup><br/>',
            // '<p>Monthly power output: '+ this.getMonthlyEnergy() + ' MWh/month/m<sup>2</sup><br/>' +
            // '<p>Yearly power output: '+ this.getYearlyEnergy() + ' MWh/year/m<sup>2</sup><br/>',
            showCloseButton: true,
            showCancelButton: true,
            focusConfirm: false,
          });
        }
        else
        {
          this.$swal(
            'Error',
            'Invalid dimension',
            'error'
          );
        }
        }
    },

    mounted() {
      //this.authorize();
      this.getMeasurements();
    },
    data() {
      return {
        customDimension: "",
        authorized: false,
        toBeShown:false,
        energy: 0,
        lastEnergy: 0,
        posts: [],
        errors: [],
        options: {
          paddingTop: '30px'
        }
      };
    }
  };
</script>

<style>
  #customInputDiv{
    display:block;
    width:50%;
    height:50px;
    margin:auto;
    overflow:visible;
    padding-right:40px;
  }
  #customInputDiv > input {
    float:left;
    width:49%;
    height: 40px;
    -webkit-border-radius: 2px;
    -moz-border-radius: 2px;
    border-radius: 2px;
  }
  #customInputDiv > button {
    margin-top:5px;
    float:right;
    width:40%;
    height: 40px;
    -webkit-border-radius: 2px;
    -moz-border-radius: 2px;
    border-radius: 2px;
    color:grey;
    font-family: 'Metrophobic', sans-serif;
  }

</style>

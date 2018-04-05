<template>

  <img id="panel" src="../../utils/solar_panel_tr.png" v-on:click="alertDisplay">
  <!--<p class="dimension">{{this.dimension}}</p>-->


</template>

<script>
  export default {
    name: 'solarpanel',
    props:[
      'dimension',
      'energy', //average
      'lastEnergy'
    ],
    mounted() {
      this.energy = this.energy/10000; // to W
      this.energy = (this.energy * this.dimension)/0.014;
    },
    methods: {
      getEnergy(){
        return this.energy.toFixed(3)
      },
      getDailyEnergy(){
        let e = (this.energy * 60 * 10/* hours of sunlight*/)/1000;
        return e.toFixed(3)
      },
      getMonthlyEnergy(){
        let e = (this.getDailyEnergy() * 30) / 1000;
        return e.toFixed(3)
      },
      getYearlyEnergy(){
        let e = this.getMonthlyEnergy() * 12;
        return e.toFixed(3)
      },
      alertDisplay() {
        this.$swal({
          title: "Solar panel",
          html:
          '<p>Dimension: '+ this.dimension +' m<sup>2</sup><br/>' +
          '<p>Energy produced right now: ' + (this.lastEnergy*this.dimension)/0.014*60/10000 + 'Wh/m<sup>2</sup></p>' +
          '<p>Estimated daily energy output: '+ this.getDailyEnergy() + ' KWh/day/m<sup>2</sup><br/>' +
          '<p>Monthly power output: '+ this.getMonthlyEnergy() + ' MWh/month/m<sup>2</sup><br/>' +
          '<p>Yearly power output: '+ this.getYearlyEnergy() + ' MWh/year/m<sup>2</sup><br/>',

          showCloseButton: true,
          showCancelButton: true,
          focusConfirm: false,
        });
      }
    }
  }
</script>

<style>
  .dimension {
    z-index:99;
    text-align: center;
    font-size: 25px;
    color:white;
  }
</style>

<template>


  <img id="panel" src="../../utils/solar_panel_tr.png" v-on:click="alertDisplay">

</template>

<script>
  export default {
    name: 'solarpanel',
    props:[
      'dimension',
      'energy', //average
      'lastEnergy',
      'width',
      'height'
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
        let e = (this.energy * 60 * 12/* hours of sunlight*/) / 1000;
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
          '<p>Last recorded energy output: ' + (( (this.lastEnergy/10000) *this.dimension)/0.014 )*60 + 'Wh</p>',
         // '<p>Estimated average per day: '+ this.getDailyEnergy() + ' KWh/day/m<sup>2</sup><br/>',
          // '<p>Monthly power output: '+ this.getMonthlyEnergy() + ' MWh/month/m<sup>2</sup><br/>' +
          // '<p>Yearly power output: '+ this.getYearlyEnergy() + ' MWh/year/m<sup>2</sup><br/>',

          showCloseButton: true,
          showCancelButton: true,
          focusConfirm: false,
        });
      }
    },
    data () {
      return {
        styleObject: {
          width: this.width + '%',
          height: this.height + '%',
          margin: 'auto'
        }
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

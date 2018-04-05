<template>
  <pageLayout>
    <!--<button @submit.prevent="submitForm" @click="requestData()"></button>-->
    <v-select id="dropdown" label="value" @changedV v-model="selected" :options="options" ></v-select>
    <line-chart id="chart" v-if="visible" :chart-data="chartData" :options="this.chartOptions" ></line-chart>
  </pageLayout>
</template>

<script>
  import LineChart from '../Modals/lineChart'
  import pageLayout from '../Modals/page'
  import axios from 'axios'

  import Pusher from 'pusher-js' // import Pusher
  import vSelect from 'vue-select'

  Pusher.logToConsole = true;


  export default {
    name: 'statistics',
    components: {
      pageLayout,
      LineChart,
      vSelect
    },
    watch : {
      selected : function (){
        this.requestData()
      }
    },
    created() {
      this.subscribe()
    },
    methods: {
      subscribe() {
        this.pusher = new Pusher('43efaf697390e4298a8f', {
          encrypted: true,
          cluster: 'eu'
        });
        let that = this;
        this.channel = this.pusher.subscribe('chart');
        this.channel.bind('chartData', function (data) {
          //that.incomingChartData(data)
          let theData = JSON.parse(data)
          that.$emit('incoming_chart_data', theData.data)
          console.log("Was here")
        });

        this.$on('incoming_chart_data', function (data) {
          this.incomingChartData(data)
        })
      },

      incomingChartData(data) {
        console.log("Updating data...")
        for (let i = 0; i < data.length; i++) {
          this.msLabels.push(data[i].time);
          this.msData.push(data[i].power);
          console.log(data[i].power)
        }

        this.chartData = {
          labels: this.msLabels,
          datasets: [
            {
              label: 'Radiation',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: this.msData
            }
          ]
        }

        console.log("OK")
        console.log(data);
      },
      requestData(){
        this.msLabels=[];
        this.msLabels=['0'];
        this.msData=[];
        this.msData=['0'];

        this.chartData = {
          labels:this.msLabel=['0'],
          datasets: [
            {
              label: 'Radiation',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: this.msData
            }
          ]
        };

        if(this.selected.unit ==null)
        {
          this.selected.value="1 day";
          this.selected.unit="day";
        }
        let unit = this.selected.unit;

        if(this.selected.unit === "all")
        {
         unit="all";
         value="0 "
        }
        let value = this.selected.value.split(" ")[0];


        axios.get(window.ApiUrl + /getdata/ + unit + "/" + value)
          .then(response => {
            this.visible = true;
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      }
    },
    mounted() {
    this.requestData();
    },

    data() {
      return {
        options: [
          {unit:"minute", value:"30 minutes"},
          {unit:"hour", value:"1 hour"},
          {unit:"hour", value:"2 hour"},
          {unit:"hour", value:"6 hour"},
          {unit:"hour", value:"12 hour"},
          {unit:"day", value:"1 day"},
          {unit:"day", value:"7 day"},
          {unit:"day", value:"1 month"},
          {unit:"month", value:"6 month"},
          {unit:"year", value:"1 year"},
          {unit:"all", value:"All time"}
        ],
        selected: {
          unit:null,
          value:null
        },
        pusher: null,
        channel: null,
        visible: false,
        msLabels: ['0'],
        msData: ['0'],
        chartOptions: {
          legend: {
            labels: {
              fontColor: 'white'
            }
          },
          responsive: true,
          scales: {
            yAxes: [{
              ticks: {
                fontColor: "white",
                fontSize: 10,
                beginAtZero: true,
                stepSize: 100,
                autoSkip: true
              }
            }],
            xAxes: [{
              ticks: {
                fontColor: "white",
                fontSize: 10,
                stepSize: 10,
                beginAtZero: true
              }
            }]
          }
        },
        chartData: {
          labels: ['0'],
          datasets: [
            {
              label: 'Radiation',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: ['0']
            }
          ]
        }


      }
    }
  }

</script>
<style>
  #dropdown{
    background-color:white;
    margin:auto;
    margin-top:15px;
    width:350px;
  }
</style>

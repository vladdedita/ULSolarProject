<template>
  <pageLayout>
    <line-chart id="chart" v-if="visible" :chart-data="chartData" :options="this.chartOptions"></line-chart>
  </pageLayout>
</template>

<script>
  import LineChart from '../Modals/lineChart'
  import pageLayout from '../Modals/page'
  import axios from 'axios'

  import Pusher from 'pusher-js' // import Pusher

  Pusher.logToConsole = true;


  export default {
    name: 'statistics',
    components : {
      pageLayout,
      LineChart
    },
    created(){
      this.subscribe()
    },
    methods:{
      subscribe() {
        this.pusher = new Pusher('43efaf697390e4298a8f', {
          encrypted:true,
          cluster: 'eu'
        })
        let that=this

        this.channel = this.pusher.subscribe('chart')
        this.channel.bind('chartData',function(data) {
          //that.incomingChartData(data)
          let theData = JSON.parse(data)
          that.$emit('incoming_chart_data', theData.data)
          console.log("Was here")
        })

        this.$on('incoming_chart_data', function(data){

          this.incomingChartData(data)
        })
      },
      incomingChartData(data){
        console.log("Updating data...")
        for(let i=0; i< data.length;i++) {
          this.msLabels.push(data[i].time);
          this.msData.push(data[i].power);
          console.log(data[i].power)
        }

        this.chartData={
          labels:this.msLabels,
            datasets:[
            {
              label:'Radiation',
              // backgroundColor: '#dace69',
              borderColor:'white',
              pointBackgroundColor: 'red',
              data: this.msData
            }
          ]
        }

        console.log("OK")
        console.log(data);
      }
    },
    mounted(){
      axios.get(window.ApiUrl + /getdata/)
        .then(response=>{
          this.visible=true;
        })
        .catch(e=>{console.log("ERROR:",e);
        })
    },

    data() {
      return {
        pusher:null,
        channel:null,
        visible:false,
        msLabels:['0'],
        msData:['0'],
        chartOptions:{
          legend: {
            labels: {
              fontColor: 'white'
            }
          },
          responsive:true,
            scales: {
              yAxes: [{
                ticks: {
                  fontColor: "white",
                  fontSize: 10,
                  beginAtZero: true,
                  stepSize: 100,
                  suggestedMax:2000,
                  autoSkip:true
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
        chartData:{
          labels:['0'],
          datasets:[
            {
              label:'Radiation',
              // backgroundColor: '#dace69',
              borderColor:'white',
              pointBackgroundColor: 'red',
              data: ['0']
            }
          ]
        }


      }
    }
  }

</script>
<style></style>

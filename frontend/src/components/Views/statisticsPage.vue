<template>
  <pageLayout>
    <line-chart :chart-data="this.datacollection" :options="this.chartOptions"></line-chart>
  </pageLayout>
</template>

<script>
  import LineChart from '../Modals/lineChart'
  import pageLayout from '../Modals/page'

  export default {
    name: 'statistics',
    components : {
      pageLayout,
      LineChart
    },
    data() {
      return {
        chartOptions:{
          legend: {
            labels: {
              fontColor: 'white'
            }
          },
            scales: {
              yAxes: [{
                ticks: {
                  fontColor: "#921500",
                  fontSize: 10,
                  stepSize: 5,
                  beginAtZero: true
                }
              }],
              xAxes: [{
                ticks: {
                  fontColor: "#921500",
                  fontSize: 10,
                  stepSize: 5,
                  beginAtZero: true
                }
              }]
            }
          },
        datacollection: {

          labels: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24],
          datasets:[
            {
              label:'Radiation',
              // backgroundColor: '#dace69',
              borderColor:'darkred',
              pointBackgroundColor: 'red',
              data: [20, 40, 60, 30 ,10 ,5 ,15, 26, 39, 12, 49, 12, 59, 73, 24, 44, 26,20, 40, 60, 30 ,10 ,5]
            }
          ]
        }
      }
    }
  }

</script>
<style></style>

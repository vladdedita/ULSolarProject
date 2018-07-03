<template>
  <pageLayout>
    <!--<button @submit.prevent="submitForm" @click="requestData()"></button>-->
    <div>
    <v-select  style="float:left;" id="dropdown" label="value" v-model="selected" :options="options" ></v-select>
      <button id="buttonShowCalendar" @click="showCalendar">SHOW CALENDAR</button>
      <date-picker @close="requestDateData" v-if="calShow" v-model="date" color="#94baf7"></date-picker>
    </div>
    <line-chart id="chart" v-if="visible" :chart-data="chartData" :options="this.chartOptions" ></line-chart>
    <pie-chart id="pieChart":chart-data="pieChartData" :options="this.pieChartOptions"></pie-chart>
  </pageLayout>
</template>

<script>
  import LineChart from '../modals/lineChart'
  import PieChart from '../modals/pieChart'
  import pageLayout from '../modals/page'
  import axios from 'axios'

  import Pusher from 'pusher-js' // import Pusher
  import vSelect from 'vue-select'
  import DatePicker from 'vue-md-date-picker'
  Pusher.logToConsole = true;
  export default {
    name: 'statistics',
    components: {
      PieChart,
      pageLayout,
      LineChart,
      vSelect,
      DatePicker
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
        let channelName = 'chart' + this.$store.getters.getKey;
        this.channel = this.pusher.subscribe(channelName);
        console.log("Pusher subscribed to: " + channelName);

        this.channel.bind('chartData', function (data) {
          //that.incomingChartData(data)
          let theData = JSON.parse(data)
          console.log("Trying to emit:");
          console.log(theData.data)
          that.$emit('incoming_chart_data', theData.data)

        });

        this.$on('incoming_chart_data', function (data) {
          console.log("Emitted..calling function:");
          console.log("Emit:"+data)
          this.incomingChartData(data)
        })
      },
      incomingChartData(data) {
        console.log("Updating data...")
        console.log(data)
        let self=this;
        for (let i = 0; i < data.length; i++) {
          {

            let value = (parseFloat(data[i].power) / 10000/*square meter*/ /0.014 )* 60 ;

              console.log("Data: " + value.toFixed(3) + " - " + data[i].time);
              self.msData.push(value.toFixed(3));
              self.msLabels.push(data[i].time);

            if(data[i].direction == 0)
            {
              this.pieData[0]++;
            }
            else
              this.pieData[1]++;

          }
      }
      console.log("Teoretic e bine, vedem ce facem cu chartul");

        this.chartData = {
          labels: self.msLabels,
          datasets: [
            {
              label: 'Solar Insolation (Wh / square meter)',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: self.msData
            }
          ]
        }
        console.log("Ar fi trebuit sa isi dea update");

       this.pieChartData= {
          labels: ['East','West'],
            datasets: [
            {
              backgroundColor: ['orange','red'],
              label: 'Solar Insolation (Wh / square meter)',
              // backgroundColor: '#dace69',
              data: self.pieData
            },
          ]
        }

        console.log("OK")

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
              label: 'Solar Insolation (Wh / square meter)',
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
        axios.post(window.ApiUrl + "getdata/" + unit + "/" + value,{},{
          headers: {
            'Authorization': this.$store.getters.getKey
          }
        })
          .then(response => {
            this.visible = true;
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
      requestDateData() {
        this.calShow=false;
        this.msLabels=[];
        this.msLabels=['0'];
        this.msData=[];
        this.msData=['0'];

        this.chartData = {
          labels:this.msLabel=['0'],
          datasets: [
            {
              label: 'Solar Insolation (Wh / square meter)',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: this.msData
            }
          ]
        };

        axios.post(window.ApiUrl + "/getdata/date/"+this.date,{},{
          headers: {
            'Authorization': this.$store.getters.getKey
          }
        })
          .then(response => {
            this.visible = true;
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
      showCalendar(){
        this.calShow=true;
      }

    },
    mounted() {
    //this.requestData();
     // this.requestDateData();
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
          {unit:"day", value:"2 day"},
          {unit:"day", value:"7 day"},
          {unit:"month", value:"1 month"},
          {unit:"month", value:"6 month"},
          {unit:"year", value:"1 year"},
          {unit:"all", value:"All time"}
        ],
        selected: {
          unit:"day",
          value:"1 day"
        },
        pusher: null,
        channel: null,
        visible: false,
        msLabels: ['0'],
        msData: ['0'],
        pieLabels: ['East','West'],
        pieData:[0,0],
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
                stepSize: 10,
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
              label: 'Solar Insolation (Wh / square meter)',
              // backgroundColor: '#dace69',
              borderColor: 'white',
              pointBackgroundColor: 'red',
              data: ['0']
            }
          ]
        },
        pieChartData: {
          labels: ['East','West'],
          datasets: [
            {
              backgroundColor: ['orange','red'],
              label: 'Solar Insolation (Wh / square meter)',
              // backgroundColor: '#dace69',
              data: [0,0]
            },

          ]
        },
        pieChartOptions: {
          legend: {
            labels: {
              fontColor: 'white'
            }
          },
          responsive: true,
          },
        date: null,
        calShow:true
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
  #buttonShowCalendar {
    margin-top:15px;
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

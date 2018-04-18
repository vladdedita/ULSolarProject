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
    name: 'index',

    // mounted(){
    //   this.authorize();
    // },
    components: {
      solarpanel,
      pageLayout
    },
    methods: {
      async authorize() {
        if (this.$store.getters.isAuthorized !== true) {
          if (!this.$cookies.isKey('devName') && !this.$cookies.isKey('appKey')) {
            const {value: formValues} = await this.$swal({
              title: 'Authorization',
              allowOutsideClick: false,
              confirmButtonText: 'Submit',
              confirmButtonClass: 'btn btn-success',
              html:
              '<input id="swal-input1" class="swal2-input" placeholder="Application ID">' +
              '<input id="swal-input2" class="swal2-input" placeholder="Application Access Key">' +
              '<input id="swal-input3" class="swal2-input" placeholder="Device ID">' +
              '<input id="swal-checkbox1" class="swal2-checkbox" type="checkbox" value="1"><span>' +
              'Remember me</span>',
              focusConfirm: false,
              preConfirm: () => {
                return [
                  document.getElementById('swal-input1').value,
                  document.getElementById('swal-input2').value,
                  document.getElementById('swal-checkbox1').checked
                ]
              }
            });
            if (formValues) {
              await this.checkCredentials(formValues[0], formValues[1]);

              if (this.authorized == '1') {
                await this.$swal({
                    title: 'Authorized!',
                    text: 'Your app has been successfully authorized!',
                    type: 'success'

                  }
                );

                this.$store.commit('authorize');

                console.log("Checkbox:" + formValues[2]);
                if (formValues[2] == 1) {
                  this.$cookies.set('devName', formValues[0]);
                  this.$cookies.set('appKey', formValues[1]);
                  console.log("Cookies set");
                }
              }
              else {
                await this.$swal(
                  'Error',
                  'Invalid credentials',
                  'error'
                );
                console.log("Actually here");
                this.authorize();
              }
            }


          }
          else {
            await this.checkCredentials(this.$cookies.get('devName'), this.$cookies.get('appKey'));
            if (!this.authorized) {
              this.$cookies.remove('devName');
              this.$cookies.remove('appKey');
              this.authorize();
            }
            else {
              await this.$swal({
                  title: 'Authorized by cookies!',
                  text: 'Your app has been successfully authorized!',
                  type: 'success',
                  position: 'top-end'
                }
              );
              this.$store.commit('authorize');
            }
          }
        }
        console.log("State authorized:"+this.$store.getters.isAuthorized);
      },
      async checkCredentials(_name, _key) {
        await axios.post(/auth/,
          {
            name: _name,
            key: _key
          })
          .then(response => {

            this.authorized = response.data;
            console.log("Authorization response:" + this.authorized)
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
      async getMeasurements() {
       await axios.get(window.ApiUrl + /measurements/)
          .then(response=>{
          //   console.log(response.data);
          //   let data = JSON.parse(response.data)
            this.energy=response.data.average;
            this.lastEnergy = response.data.last;
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
      this.authorize();
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

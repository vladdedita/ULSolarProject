<template>
  <page-layout>
    <div class="optionDiv">
      <h1> Update Time: </h1>
      <button @submit.prevent="submitForm" class="submitButton" @click="changeTime()"> SUBMIT</button>
      <v-select class="dropdown" label="Update period" v-model="selectedTime" :options="this.optionsTime"></v-select>

    </div>
    <div class="optionDiv">
      <h1> Power state: </h1>
      <button @submit.prevent="submitForm" class="submitButton" @click="changePowerState()"> SUBMIT</button>
      <v-select class="dropdown" label="Update period" v-model="selectedPower" :options="this.optionsPower"></v-select>
    </div>
    <div class="optionDiv">
      <h1> Position: </h1>
      <button @submit.prevent="submitForm" class="submitButton" @click="changePosition()"> SUBMIT</button>
      <v-select class="dropdown" label="Update period" v-model="selectedPosition"
                :options="this.optionsPosition"></v-select>
    </div>
  </page-layout>
</template>

<script>
  import PageLayout from "../Modals/page";
  import vSelect from 'vue-select'
  import axios from 'axios'

  export default {
    components: {PageLayout, vSelect},
    name: "management-page",
    mounted() {
      this.getProcessId()
    },
    methods: {
      async getProcessId() {
        if (!this.$cookies.isKey('processId')) {
          const {value: formValues} = await this.$swal({
            title: 'Authorization',
            confirmButtonText: 'Submit',
            confirmButtonClass: 'btn btn-success',
            text: "Please enter your HTTP Integration Process ID",
            html:
            '<input id="swal-input1" class="swal2-input" placeholder="Process Id">' +
            '<input id="swal-checkbox1" class="swal2-checkbox" type="checkbox" value="1"><span>' +
            'Remember me</span>',
            focusConfirm: false,
            preConfirm: () => {
              return [
                document.getElementById('swal-input1').value,
                document.getElementById('swal-checkbox1').value
              ]
            }
          });

          if (formValues[0]) {
            await this.$swal({
                title: 'OK!',
                text: '',
                type: 'success'
              }
            );
            this.processId = formValues[0]

            /*  await axios.post(/setProcessId/,
                {
                  processId:this.processId
                })
                .then(response => {

                  this.authorized=response.data;
                  console.log("Authorization response:" + this.authorized)
                })
                .catch(e => {
                  console.log("ERROR:", e);
                })
*/
            console.log("Here");
            this.$cookies.set('processId', formValues[0])
          }
        }
        else {
          await this.$swal({
              title: 'OK',
              text: 'Process ID fetched from cookies!',
              type: 'success',
              position: 'top-end'
            }
          );
          this.processId = this.$cookies.get('processId');
        }
      },
      changeTime() {
        if (this.processId) {
          if (this.selectedTime != null) {
            console.log("Attempting to change time to:" + this.selectedTime)
            axios.post(/changeTime/,
              {
                processId: this.processId,
                time: this.selectedTime
              })
              .then(response => {
              })
              .catch(e => {
                console.log("ERROR:", e);
              })
          }
        }
      },
      changePosition() {
        if (this.processId) {
          if (this.selectedPosition != null) {
            console.log("Attempting to change position to:" + this.selectedPosition)
            axios.post(/changePosition/,
              {
                processId: this.processId,
                position: this.selectedPosition
              })
              .then(response => {
              })
              .catch(e => {
                console.log("ERROR:", e);
              })
          }
        }
      },
      changePowerState() {
        if (this.processId) {
          if (this.selectedPower != null) {
            console.log("Attempting to change state to:" + this.selectedPower)
            axios.post(/changeState/,
              {
                processId: this.processId,
                state: this.selectedPower
              })
              .then(response => {
              })
              .catch(e => {
                console.log("ERROR:", e);
              })
          }
        }
      }
    },

    data() {
      return {
        processId: null,
        selectedTime: null,
        optionsTime: ['1', '15', '30', '60', '120', '240'],
        selectedPower: null,
        optionsPower: ['RESET', 'RECALIBRATE', 'OFF'],
        selectedPosition: null,
        optionsPosition: ['EAST', 'WEST']
      }
    }
  }
</script>

<style scoped>
  .dropdown {
    min-width: 300px;
    max-width: 300px;
    max-height: 37px;
    margin: auto;
    background-color: #fdfff0;
    float: right;
    margin-top: 15px;

  }

  .optionDiv {
    width: 100%;
    height: 100px;
    margin: auto;
    text-align: left;
    display: block;

  }

  h1 {
    width: 30%;
    font-family: 'Metrophobic', serif;
    font-size: 26px;
    font-weight: 100;
    color: white;
    float: left;

  }

  .submitButton {
    float: right;
    display: block;
    margin-top: 15px;
    height: 36px;
    width: 100px;
    margin-left: 10px;
    background-color: transparent;
    -webkit-border-radius: 0px;
    -moz-border-radius: 0px;
    border-radius: 0px;
    color: white;
    border-color: white;

    font-family: 'Metrophobic';

  }
</style>

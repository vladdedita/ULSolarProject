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

</template>

<script>

  //import Alert from '../Modals/alert.vue';

  import axios from 'axios';



  export default {
    name: 'auth',

    // mounted(){
    //   this.authorize();
    // },
    components: {
    },
    methods: {
      async authorize() {
        if (this.$store.getters.isAuthorized !== true) {
          if (!this.$cookies.isKey('appID') && !this.$cookies.isKey('appKey') && !this.$cookies.isKey('devID')) {
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
                  document.getElementById('swal-input3').value,
                  document.getElementById('swal-checkbox1').checked
                ]
              }
            });
            if (formValues) {
              await this.checkCredentials(formValues[0], formValues[1],formValues[2]);
              if (this.authorized == '1') {
                await this.$swal({
                    title: 'Authorized!',
                    text: 'Your app has been successfully authorized!',
                    type: 'success'
                  }
                );

                this.$store.commit('authorize');

                console.log("Remember me:",formValues[3]);
                if (formValues[3] === true) {
                  this.$cookies.set('appID', formValues[0]);
                  this.$cookies.set('appKey', formValues[1]);
                  this.$cookies.set('devID', formValues[2]);
                  console.log("Cookies set");
                }
                this.$router.push("/front");
              }
              else {
                await this.$swal({
                  type: 'error',
                  title: 'Error',
                  text: 'Invalid credentials!'

                });
                this.authorize();
              }
            }
          }
          else {
            await this.checkCredentials(this.$cookies.get('appID'), this.$cookies.get('appKey'),this.$cookies.get('devID'));
            if (!this.authorized) {
              this.$cookies.remove('appID');
              this.$cookies.remove('appKey');
              this.$cookies.remove('devID');
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
              this.$router.push("/front");
            }
          }
        }
        console.log("State authorized:"+this.$store.getters.isAuthorized);
      },
      async checkCredentials(_appID, _appKey, _devID) {
        await axios.post(window.ApiUrl + 'auth/',
          {
            appID: _appID,
            appKey: _appKey,
            devID: _devID
          })
          .then(response => {
            let error = response.data.error;
            if(error)
            {
              this.authorized= 0;
            }
            else {
              let r = response.data.key;
              if (r !== null && r !== "") {
                this.authorized = 1;
                console.log("Authorization response:" + r);
                this.$store.commit('setKey', r);
              }
            }

          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
    },

    mounted() {
      this.authorize();
    },
    data() {
      return {

        authorized: false,
        options: {
          paddingTop: '30px'
        }
      };
    }
  };
</script>

<style>

</style>

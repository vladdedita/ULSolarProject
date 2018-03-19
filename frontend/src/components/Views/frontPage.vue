<template>

      <pageLayout v-if="authorized">

        <solarpanel style="width:10%; height: 10%;"></solarpanel>
        <solarpanel style="width:15%; height: 15%;"></solarpanel>
        <solarpanel style="width:20%; height: 20%;"></solarpanel>
        <solarpanel style="width:30%; height: 30%;"></solarpanel>
        <solarpanel style="width:40%; height: 40%;"></solarpanel>
    </pageLayout>

</template>

<script>

  //import Alert from '../Modals/alert.vue';
  import axios from 'axios';
  //import navbar from '../Modals/navbar.vue';
  import pageLayout from '../Modals/page.vue'
  import solarpanel from "../Modals/solarPanel";


  export default{

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

        if(!this.$cookies.isKey('devName') && !this.$cookies.isKey('appKey')) {

          const {value: formValues} = await this.$swal({
            title: 'Authorization',
            confirmButtonText: 'Submit',
            confirmButtonClass: 'btn btn-success',

            html:
            '<input id="swal-input1" class="swal2-input" placeholder="Device name">' +
            '<input id="swal-input2" class="swal2-input" placeholder="App Access Key">' +
            '<input id="swal-checkbox1" class="swal2-checkbox" type="checkbox" value="1"><span>' +
            'Remember me</span>',
            focusConfirm: false,
            preConfirm: () => {
              return [
                document.getElementById('swal-input1').value,
                document.getElementById('swal-input2').value,
                document.getElementById('swal-checkbox1').value
              ]
            }
          });

          if (formValues) {
            await this.checkCredentials(formValues[0], formValues[1]);

            console.log(this.authorized);

            if (this.authorized=='1') {
              await this.$swal({
                title:'Authorized!',
                text:'Your app has been successfully authorized!',
                type:'success'

              }
              );
              console.log("Here");
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
        else
        {
          await this.checkCredentials(this.$cookies.get('devName'),this.$cookies.get('appKey'));

          if(!this.authorized)
          {
            this.$cookies.remove('devName');
            this.$cookies.remove('appKey');
            this.authorize();
          }
          else {
            await this.$swal({
                title:'Authorized by cookies!',
                text:'Your app has been successfully authorized!',
                type:'success',
                position:'top-end'
              }
            );
          }
        }

      },
      async checkCredentials(_name,_key){
        await axios.post(/auth/,
          {
            name: _name,
            key: _key
          })
          .then(response => {

            this.authorized=response.data;
            console.log("Authorization response:" + this.authorized)
          })
          .catch(e => {
            console.log("ERROR:", e);
          })
      },
      getMeasurements(){

        axios.get(window.ApiUrl + /measurements/)
          .then(response=>{
            this.posts=response.data;
            console.log(this.posts)
          })
          .catch(e=>{console.log("ERROR:",e);
          })
      }
    },

    mounted(){
      this.authorize();
      this.getMeasurements();
    },
    data () {
      return {
        authorized: true,
        posts: [],
        errors: []
      };
    }
  };
</script>

<style>
</style>

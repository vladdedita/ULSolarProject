<template>
  <div id="divPage" v-if="authorized">
    <div id="blur"></div>

    <div id="navBar">

      <div id="navBarIcon">
        <img src="../../utils/icon1.png" />
      </div>
      <div id="navBarMenu">

        <div class="navBarChoice">
          <div class="opac"></div>
          <p>Homepage</p>
        </div>
        <div class="navBarChoice">
          <div class="opac"></div>
          <p>Statistics</p>
        </div>

        <div class="navBarChoice">
          <div class="opac"></div>
          <p>Device Management</p>
        </div>
        <div class="navBarChoice">
          <div class="opac"></div>
          <p>Authorize device</p>
        </div>
      </div>

    </div>

    <div id="divPageDiv">
      <div class="opac"></div>
      <div id="divPageFlexbox">


        <div class="divPageFlexboxDiv">


          <img src="../../utils/solar_panel_tr.png" v-on:click="alertDisplay" style="width:10%; height:10%">


          <img src="../../utils/solar_panel_tr.png"  v-on:click="alertDisplay" style="width:15%; height:15%"/>

          <img src="../../utils/solar_panel_tr.png"  v-on:click="alertDisplay" style="width:20%; height:20%"/>
        </div>

        <div class="divPageFlexboxDiv">
          <img src="../../utils/solar_panel_tr.png"  v-on:click="alertDisplay" style="width:30%; height:30%"/>
        </div>
        <div class="divPageFlexboxDiv">
          <img src="../../utils/solar_panel_tr.png"  v-on:click="alertDisplay" style="width:30%; height:30%"/>
        </div>
        <div class="divPageFlexboxDiv">
          <img src="../../utils/solar_panel_tr.png"  v-on:click="alertDisplay" style="width:30%; height:30%"/>

        </div>

      </div>
    </div>
  </div>
</template>

<script>

  //import Alert from '../Modals/alert.vue';
  import axios from 'axios';


  export default{

    name: 'index',
    // mounted(){
    //   this.authorize();
    // },
    components: { },
    methods: {
      alertDisplay(){
        this.$swal({
          title:"Solar panel",
          html:
          '<p>Dimension <br/>' +
          '<p>Estimated output voltage: ' + this.posts[0].value +
          '<p>Daily power output: <br/>' +
          '<p>Yearly power output <br/>'+
          '<p>Monthly power output: <br/>',
          showCloseButton: true,
          showCancelButton: true,
          focusConfirm: false,
        });
      },
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
              await this.$swal(
                'Authorized!',
                'Your app has been successfully authorized!',
                'success'
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
            await this.$swal(
              'Authorized by cookies!',
              'Your app has been successfully authorized!',
              'success'
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
        authorized:false,
        posts: [],
        errors: []
      };
    }
  };
</script>

<style scoped>



  div{

    /*border-color: white;*/
    /*border-style: solid;*/

  }

  #navBar {

    position: relative;
    width:90%;
    height: 256px;
    margin:auto;
    margin-top:10px;
    margin-bottom:10px;

    display: block;

  }

  #navBarMenu{

    display: block;
    position: relative;
    margin-top:5%;
    width:60%;
    height: 50%;
    float:right;

  }

  #navBarIcon{

    position:relative;
    width:38%;
    height: auto;
    float:left;

  }
  #navBarIcon > img {

    height: 256px;
    width:256px;
    margin-left:30%;
  }
  .navBarChoice{

    position: relative;
    display:block;
    width:24%;
    height: 80%;
    text-align: center;
    float:left;
    border: solid white;
    border-top-width: 0;
    border-left-width: 0;

  }

  .navBarChoice:hover {

    background: lightblue;

  }
  .navBarChoice:hover > p {
    color:darkblue;
  }
  .navBarChoice:hover > .opac {
    display: none;
  }
  .navBarChoice > p {

    text-align: center;
    vertical-align: middle;
    font-size:18px;
    color:white;
    line-height: 65px;
    font-family: "DejaVu Sans";
    z-index: 2;

  }

  #divPage{

    position: relative;
    width:100%;
    height: auto;

    /*border-color:green;*/

  }
  #blur {

    position: absolute;
    background: url("../../utils/3.jpeg") no-repeat;
    width:100%;
    height:100%;
    background-size:cover;
    -webkit-filter: blur(4px);
    -moz-filter: blur(4px);
    -ms-filter: blur(4px);
    -o-filter: blur(4px);
    filter: blur(3px);
    /*border-color:red;*/
    /*border-style: solid;*/
    z-index: -1;
  }
  .opac{
    position: absolute;
    background-color: #86a6b4;
    opacity:0.3;
    width:100%;
    height: 100%;
    z-index:0;
  }

  #divPageDiv{
    position: relative;
    margin:auto;
    width:80%;
    height: auto;
    /*border-color:blue;*/
  }

  #divPageFlexbox{

    position: relative;
    width:80%;
    min-height: 10%;
    height: auto;
    top:5%;
    left:10%;
    right: 10%;
    bottom:5%;
    /*border-style: solid;*/
    /*border-color: #a50003;*/

  }

  .divPageFlexboxDiv {

    position: relative;
    margin:auto;
    width:80%;
    height: auto;
    /*border-color:darkorchid;*/

  }
  .divPageFlexboxDiv > img {

    display: block;
    position: relative;
    margin:auto;

  }
  .divPageFlexboxDiv > img:hover {

    background: lightblue;
    -webkit-border-radius: 10px;
    -moz-border-radius: 10px;
    border-radius: 10px;

  }

</style>

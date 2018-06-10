<template>
  <div id="divPage" v-if="true">
    <!--this.$store.getters.isAuthorized-->
    <div id="blur"></div>
    <navbar></navbar>
    <div id="divPageDiv">
      <div class="opac"></div>
      <div id="divPageFlexbox">
        <div class="divPageFlexboxDiv">
          <slot></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import navbar from './/navbar'

  export default {
    name: 'pageLayout',
    components: {
      navbar
    }
  }
</script>
<style>
  html, body {
    height: 100%;
    margin: 0;
    /*background-color:black;*/
    font-family: 'Metrophobic', sans-serif;
    background: url("../../utils/3.jpeg") no-repeat;
    background-attachment: fixed;
  }

  #divPage {
    /*position: fixed;*/
    /*width: 100%;*/
    /*height: 100%;*/
    /*left: 0;*/
    /*top: 0;*/
    /*background: rgba(51,51,51,0.7);*/
    /*z-index: 10;*/
    width: 100%;
    min-height: 100%;
  }

  #blur {

    position: absolute;

    /*background-color:black;*/
    background-attachment: fixed;
    width: 100%;
    height: 100%;
    background-size: cover;
    -webkit-filter: blur(4px);
    -moz-filter: blur(4px);
    -ms-filter: blur(4px);
    -o-filter: blur(4px);
    filter: blur(3px);
    /*border-color:red;*/
    /*border-style: solid;*/
    z-index: -1;
  }

  .opac {
    position: absolute;
    background-color: #86a6b4;
    opacity: 0.3;
    width: 100%;
    height: 100%;
    z-index: 0;
  }

  #divPageDiv {
    position: relative;
    margin: auto;
    width: 80%;
    height: auto;
    /*border-color:blue;*/
  }

  #divPageFlexbox {

    position: relative;
    width: 80%;
    min-height: 10%;
    height: auto;
    top: 5%;
    left: 10%;
    right: 10%;
    bottom: 5%;
    /*border-style: solid;*/
    /*border-color: #a50003;*/

  }

  .divPageFlexboxDiv {
    position: relative;
    margin: auto;
    width: 80%;
    height: auto;
    /*border-color:darkorchid;*/

  }

  .divPageFlexboxDiv > img {
    margin-top:10px;
    display: block;
    position: relative;
    margin: auto;

  }

  .divPageFlexboxDiv > img:hover {

    background: lightblue;
    -webkit-border-radius: 10px;
    -moz-border-radius: 10px;
    border-radius: 10px;

  }
</style>

<template>

  <div id="navBar">

    <div id="navBarIcon">
      <img src="../../utils/icon1.png" />
    </div>
    <div id="navBarMenu">

      <div class="navBarChoice">
        <div class="opac"></div>
        <p><router-link to="/" class="navBarChoiceLink">Homepage</router-link></p>
      </div>
      <div class="navBarChoice">
        <div class="opac"></div>
        <p><router-link to="/statistics" class="navBarChoiceLink">Statistics</router-link></p>
      </div>
      <div class="navBarChoice">
        <div class="opac"></div>
        <p><router-link to="/management" class="navBarChoiceLink">Device Management</router-link></p>
      </div>
      <div class="navBarChoice">
        <div class="opac"></div>
        <p><router-link to="/location" class="navBarChoiceLink">Location</router-link></p>
      </div>

      <div class="navBarChoice">
        <div class="opac"></div>
        <p>About</p>
      </div>
    </div>

  </div>

</template>
<script>
  export default {
    name:'navbar'
  }
</script>
<style scoped>
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
    width:19%;
    height: 80%;
    text-align: center;
    float:left;
    border: solid white;
    border-top-width: 0;
    border-left-width: 0;


  }
  .navBarChoiceLink{
    text-decoration: none;
    color: white;
    z-index:5;
    width:100%;
    height: 100%;
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
    font-size:15px;
    font-style: bold;
    color:white;
    line-height: 65px;
    font-family: "DejaVu Sans";
    z-index: 2;

  }
</style>

<template>

  <img id="panel" src="../../utils/solar_panel_tr.png" v-on:click="alertDisplay">

</template>

<script>
  export default {
    name: 'solarpanel',
    methods: {
      alertDisplay() {
        this.$swal({
          title: "Solar panel",
          html:
          '<p>Dimension <br/>' +
          '<p>Estimated output voltage: ' +//+ this.posts[0].value +
          '<p>Daily power output: <br/>' +
          '<p>Yearly power output <br/>' +
          '<p>Monthly power output: <br/>',
          showCloseButton: true,
          showCancelButton: true,
          focusConfirm: false,
        });
      }
    }
  }
</script>

<style></style>

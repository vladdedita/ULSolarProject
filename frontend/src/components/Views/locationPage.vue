<template>
  <pageLayout>
    <p>Please choose your location</p>
    <div id="divMap">
      <google-map></google-map>
    </div>
  </pageLayout>
</template>

<script>
  import pageLayout from "../Modals/page"
  import googleMap from "../Modals/googleMap"

  export default {
    name: "location-page",
    methods: {
      showMap: function () {
        console.log("Trying to show map ...");

      }
    },
    components: {
      pageLayout,
      googleMap
    }

  }
</script>

<style scoped>
  #divMap {
    margin-left: 50%;
    overflow: visible;
  }

</style>

<template>
  <pageLayout>
  <googleMap></googleMap>
  </pageLayout>
</template>

<script>
  import pageLayout from "../Modals/page"
  import googleMap from "../Modals/googleMap"
    export default {
        name: "location-page",
        components: {
          pageLayout,
          googleMap
        }

    }
</script>

<style scoped>



</style>

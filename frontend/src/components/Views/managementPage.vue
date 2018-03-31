<template>
<page-layout>
<button @click="changeTime"></button>
</page-layout>
</template>

<script>
    import PageLayout from "../Modals/page";
    import axios from 'axios'
    export default {
      components: {PageLayout},
      methods:{
        changeTime() {

          axios.post(/changeTime/,
            {
              processId: "solar-downlink-schedule",
              time: 15
            })
            .then(response => {
              })
            .catch(e => {
              console.log("ERROR:", e);
            })
        }
      },
      name: "management-page"
    }
</script>

<style scoped>

</style>

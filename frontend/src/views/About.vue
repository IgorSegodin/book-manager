<script>
import AjaxService from "@/service/AjaxService";
export default {
  name: "About",

  data() {
    return {
      ajaxData: null
    }
  },

  mounted() {
    AjaxService.fetch("/temp")
        .then((data) => this.ajaxData = data);
  },
}

</script>

<template>
  <div class="about">
    <h1>This is an about page</h1>
    <div>Ajax: {{ ajaxData }}</div>
  </div>
</template>

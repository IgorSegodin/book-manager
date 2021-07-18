<script>

import AjaxService from "@/service/AjaxService";
import CommonConfig from "@/service/CommonConfig";

export default {
  name: 'TempView',

  data() {
    return {
      ajaxData: null
    }
  },

  mounted() {
    AjaxService.fetch("/temp")
        .then((data) => this.ajaxData = data);
  },

  methods: {
    ctx(input) {
      return CommonConfig.buildContextPath(input);
    },

    api(input) {
      return CommonConfig.buildApiPath(input);
    }
  }
}
</script>


<template>
  <div>Hello: {{ ctx('/ctx') }}; {{ api('/api') }}</div>
  <div>Hello: {{ ajaxData }}</div>
</template>
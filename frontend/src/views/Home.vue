<script>
import CommonConfig from "@/service/CommonConfig";

export default {
  name: 'Home',

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
  <div class="home">
    <img alt="Logo" :src="ctx('logo.svg')">

    <div>Test path: {{ ctx('/ctx') }}; {{ api('/api') }}</div>

  </div>
</template>
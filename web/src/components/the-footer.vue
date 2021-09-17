<template>
  <a-layout-footer style="text-align: center">
    Java知识库  {{user.name}}
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent} from "vue";
import store from "@/store";

export default defineComponent({
  name: "the-footer",
  setup(){
    const user = computed(() => store.state.user);
    return{
      user
    }
  }
})
</script>

<style scoped>

</style>

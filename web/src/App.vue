<template>
  <a-layout>
    <TheHeader/>
      <router-view/>
    <TheFooter/>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import TheHeader from '@/components/the-header.vue';
import TheFooter from '@/components/the-footer.vue';

export default defineComponent({
  name: 'App',
  components: {
      TheHeader,
      TheFooter
  },
});
</script>

<style>

</style>

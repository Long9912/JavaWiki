<template>
  <div>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu mode="inline" :style="{ height: '100%', borderRight: 0 }">
          <a-sub-menu key="sub1">
            <template #title>
              <span>
                <user-outlined/>subnav 1
              </span>
            </template>
            <a-menu-item key="1">option1</a-menu-item>
            <a-menu-item key="2">option2</a-menu-item>
            <a-menu-item key="3">option3</a-menu-item>
            <a-menu-item key="4">option4</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
              <span>
                <laptop-outlined/>subnav 2
              </span>
            </template>
            <a-menu-item key="5">option5</a-menu-item>
            <a-menu-item key="6">option6</a-menu-item>
            <a-menu-item key="7">option7</a-menu-item>
            <a-menu-item key="8">option8</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3">
            <template #title>
              <span>
                <notification-outlined/>subnav 3
              </span>
            </template>
            <a-menu-item key="9">option9</a-menu-item>
            <a-menu-item key="10">option10</a-menu-item>
            <a-menu-item key="11">option11</a-menu-item>
            <a-menu-item key="12">option12</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <a-list item-layout="vertical" size="large" :data-source="ebooks"
                :grid="{ gutter: 45, xs: 1, md: 2, xl: 3}">
              <template #renderItem="{ item }">
                <a-list-item key="item.name">
                  <template #actions>
                    <span v-for="{ type, text } in actions" :key="type">
                      <component v-bind:is="type" style="margin-right: 8px"/>
                      {{ text }}
                    </span>
                  </template>
                  <a-list-item-meta :description="item.description">
                    <template #title>
                      <a :href="item.href">{{ item.name }}</a>
                    </template>
                    <template #avatar>
                      <a-avatar :src="item.cover"/>
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </template>
            </a-list>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";

export default defineComponent({
  name: 'Home',
  setup() {
    //响应式数据
    const ebooks = ref();
    onMounted(() =>
        axios.get('/ebook/all',).then((response => {
          const data = response.data;
          ebooks.value = data.content;
        }))
    )
    return {
      //返回数据
      ebooks,
      actions: [
        {type: 'StarOutlined', text: '156'},
        {type: 'LikeOutlined', text: '156'},
        {type: 'MessageOutlined', text: '2'},
      ]
    }
  },
  components: { },
});
</script>

<style scoped>
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>

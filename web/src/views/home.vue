<template>
  <div>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            mode="inline"
            :style="{ height: '100%', borderRight: 0 }"
            @click="handleClick"
        >
          <a-menu-item key="welcome">
            <HomeOutlined />
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span><BookOutlined />{{item.name}}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <span><ReadOutlined />{{child.name}}</span>
            </a-menu-item>
          </a-sub-menu>
          <a-menu-item key="tip" :disabled="true">
            <span>以上菜单在分类管理配置</span>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <div class="welcome" v-show="isShowWelcome">
              <h1>欢迎使用java知识库</h1>
            </div>
            <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :data-source="ebooks"
                :grid="{ gutter: 45, xs: 1, md: 2, xl: 3}">
              <template #renderItem="{ item }">
                <a-list-item key="item.name">
                  <template #actions>
                    <span>
                      <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                      {{ item.docCount }}
                    </span>
                    <span>
                      <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                      {{ item.viewCount }}
                    </span>
                    <span>
                      <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                      {{ item.voteCount }}
                    </span>
                  </template>
                  <a-list-item-meta :description="item.description">
                    <template #title>
                      <router-link :to="'/doc?ebookId='+item.id+'&name='+item.name">
                        {{ item.name }}
                      </router-link>
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
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'Home',
  setup() {
    //响应式数据
    const ebooks = ref();
    const isShowWelcome = ref(true);
    let categoryId2 = '0';

    const level1 = ref(); // 一级分类树，children属性就是二级分类
    let categorys:any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          categorys = data.content;

          level1.value = [];
          //使用递归将数组转为树形结构
          level1.value = Tool.array2Tree(categorys, 0);
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    };

    const handleQueryEbook = () => {
      axios.get('/ebook/all', {
        params: {
          categoryId2: categoryId2
        }
      }).then((response => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          ebooks.value = data.content;
        } else {
          message.error(data.message);
        }
      }))
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      //返回数据
      ebooks,
      isShowWelcome,
      level1,
      handleClick,
    }
  },
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

<template>
  <a-layout>
    <a-layout-content :style="{ background: '#f5f5f5', padding: '24px', margin: 0, minHeight: '480px' }">
      <div class="search-board">
        <a-input-search
            style="width: 80%;margin-left: 10%;margin-top: 10px;"
            v-model:value="searchText"
            placeholder="搜索文章"
            enter-button
            @search="onSearch"
        />
        <a-list item-layout="horizontal" :data-source="searchList"
                :pagination="pagination" style="margin-left: 10%;margin-right: 10%">
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <a-list-item-meta>
                <template #title>
                  <router-link :to="'/doc?ebookId='+item.ebookId+'&name='+item.name+'&id='+item.id">
                    <h3 v-html="item.name"></h3>
                  </router-link>
                  <p v-html="item.content"></p>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
        <p v-if="pagination.total != null" style="margin-left: 75%;margin-top: 10px">
          查询总数:
          <a-tag color="blue">{{pagination.total}}</a-tag>
        </p>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import { useRoute } from 'vue-router';

export default defineComponent({
  name: 'Search',
  setup() {
    const searchText = ref();
    const searchList = ref();

    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0,
      onChange: (page: number) => {
        handleChange(page);
      },
    });

    /**
     * 数据查询
     */
    const onSearch = (searchValue: string) => {
      let text = searchText.value;
      let page = pagination.value.current;
      let size = pagination.value.pageSize;
      axios.get("/search/HighLight/"+text+"/"+page+"/"+size,
      ).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          searchList.value = data.content.list;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 点击页码时触发
     */
     const handleChange = (page: number) => {
      pagination.value.current = page;
      onSearch("search");
     };

    const route = useRoute();

    onMounted(() => {
      //获取关键词
      searchText.value = route.query.keyword;
      onSearch("search");
    });

    return {
      searchList,
      pagination,
      searchText,
      onSearch,
    }
  }
});
</script>

<style scoped>
  .search-board {
    width: 70%;
    margin-left: 15%;
    border-top:#134f8d 4px solid;
    border-bottom:#134f8d 4px solid;
    background: #fff;
  }
</style>

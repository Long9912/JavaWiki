<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff',marginLeft:'20px',marginRight:'20px', minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <a-page-header
              style="border: 2px solid rgb(235, 237, 240);height: 70px"
              :title="bookName"
              @back="back"
          />
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title:'name', key:'id',value:'id'}"
              :defaultExpandAll="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          文档内容
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import router from "@/router";

export default defineComponent({
  name: 'Doc',

  setup() {
    const route = useRoute();
    const bookName = ref();
    const ebookId = ref();
    const docs = ref();

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档完成文档表基本增删改查功能
    level1.value = [];

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all/"+ebookId.value).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

          level1.value = [];
          //使用递归将数组转为树形结构
          level1.value = Tool.array2Tree(docs.value, 0);
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/findContent/" + ebookId.value).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 返回上一级页面
     */
    const back = () => {
      router.push({path: "/admin/ebook"});
    };

    //----------表单-----------
    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    onMounted(() => {
      bookName.value = route.query.name;
      ebookId.value = route.query.ebookId
      handleQuery();
    });

    return {
      level1,
      bookName,
      back,
    }
  },
});
</script>

<style scoped>

</style>

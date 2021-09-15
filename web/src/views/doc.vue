<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff',marginLeft:'20px',marginRight:'20px', minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <a-page-header
              style="height: 70px"
              :title="bookName"
              @back="back"
          />
          <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title:'name', key:'id',value:'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div class="wangEditor" :innerHTML="html"></div>
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
    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

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
     * 内容查询
     **/
    const handleQueryContent = (id: string) => {
      axios.get("/doc/findContent/" + id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          html.value = data.content;
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all/" + ebookId.value).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

          level1.value = [];
          //使用递归将数组转为树形结构
          level1.value = Tool.array2Tree(docs.value, 0);

          if (Tool.isNotEmpty(level1)) {
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(level1.value[0].id);
          }
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      if (Tool.isNotEmpty(selectedKeys)) {
        //加装内容
        handleQueryContent(selectedKeys[0]);
      }
    }

    /**
     * 返回上一级页面
     */
    const back = () => {
      router.push({path: "/"});
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
      html,
      defaultSelectedKeys,
      back,
      onSelect,
    }
  },
});
</script>

<style>
/* table 样式 */
.wangEditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangEditor table td,
.wangEditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangEditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangEditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* 与ant vue的 p 冲突 覆盖掉 */
.wangEditor blockquote p{
  margin: 20px 10px;
  font-family: "Microsoft Yahei";
  font-weight: 600;
}

/* code 样式 */
.wangEditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangEditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangEditor ul, ol {
  margin: 10px 0 10px 20px;
}
</style>

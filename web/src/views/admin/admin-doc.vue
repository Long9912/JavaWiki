<template>
  <div>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-form layout="inline" :model="queryParam">
          <a-form-item>
            <a-button @click="handleQuery()">
              <template #icon><SearchOutlined /></template>
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">
              <template #icon><PlusSquareOutlined /></template>
              新增
            </a-button>
          </a-form-item>
        </a-form>

        <a-table
            :columns="columns"
            :row-key="record => record.id"
            :data-source="level1"
            :loading="loading"
            :pagination="false"
            @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar"/>
          </template>
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                <template #icon><EditOutlined /></template>
                编辑
              </a-button>
              <a-popconfirm
                  title="确认删除文档?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger" >
                  <template #icon><DeleteOutlined /></template>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-layout-content>
    </a-layout>
    <a-modal
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="id">
          <a-input v-model:value="doc.id" disabled="true"/>
        </a-form-item>
        <a-form-item label="电子书ID">
          <a-input v-model:value="doc.ebookId"/>
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="doc.name"/>
        </a-form-item>
        <a-form-item label="父文档">
          <a-select ref="select" v-model:value="doc.parent">
            <a-select-option value="0">无</a-select-option>
            <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
              {{c.name}}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="顺序">
          <a-input v-model:value="doc.sort"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',

  setup() {
    const queryParam = ref();
    queryParam.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: 'Id',
        dataIndex: 'id'
      },
      {
        title: '电子书Id',
        dataIndex: 'ebookId'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父文档',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

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
      loading.value = true;
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

          level1.value = [];
          //使用递归将数组转为树形结构
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1);
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    //----------表单-----------
    const doc = ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {};
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      //复制当前列的参数
      doc.value = Tool.copy(record);
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          modalVisible.value = false;
          //重新加载列表
          handleQuery();
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const handleDelete = (id:string) => {
      axios.delete("/doc/delete/"+id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          //重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };
    onMounted(() => {
      handleQuery();
    });

    return {
      queryParam,
      docs,
      level1,
      columns,
      loading,
      handleQuery,

      add,
      edit,
      doc,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete
    }
  },
  components: {}
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

<template>
  <div>
    <a-layout>
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-form layout="inline" :model="queryParam">
          <a-form-item>
            <a-input v-model:value="queryParam.loginName" placeholder="登录名">
              <template #prefix><SearchOutlined /></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button @click="handleQuery({page: 1,size: pagination.pageSize})">
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
            :data-source="users"
            :pagination="pagination"
            :loading="loading"
            @change="handleTableChange"
        >
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                <template #icon><EditOutlined /></template>
                编辑
              </a-button>
              <a-popconfirm
                  title="确认删除用户?"
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
        title="用户表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="id">
          <a-input v-model:value="user.id" disabled="true"/>
        </a-form-item>
        <a-form-item label="登录名">
          <a-input v-model:value="user.loginName"/>
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="user.name"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="user.password"/>
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
  name: 'AdminUser',

  setup() {
    const queryParam = ref();
    queryParam.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: 'id',
        dataIndex: 'id'
      },
      {
        title: '登录名',
        dataIndex: 'loginName'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: queryParam.value.loginName
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          users.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    //----------表单-----------
    const user = ref();
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/user/save", user.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          modalVisible.value = false;
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      user.value = {};
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      //复制当前列的参数
      user.value = Tool.copy(record);
    };

    const handleDelete = (id:string) => {
      axios.delete("/user/delete/"+id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      queryParam,
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      add,
      edit,
      user,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete
    }
  },
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

<template>
  <div>
    <a-layout>
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-form layout="inline" :model="queryParam">
          <a-form-item>
            <a-input v-model:value="queryParam.loginName" placeholder="登录名">
              <template #prefix><SearchOutlined/></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button @click="handleQuery({page: 1,size: pagination.pageSize})">
              <template #icon><SearchOutlined/></template>
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">
              <template #icon><PlusSquareOutlined/></template>
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
              <a-button type="primary" @click="reset(record)">
                <template #icon><RedoOutlined /></template>
                重置密码
              </a-button>
              <a-button v-show="record.isAdmin==='false'" type="primary" @click="setAdmin(record.id)">
                <template #icon><UserAddOutlined /></template>
                设为管理员
              </a-button>
              <a-button v-show="record.isAdmin==='true'" type="danger" @click="setAdmin(record.id)">
                <template #icon><UserDeleteOutlined /></template>
                取消管理员
              </a-button>
              <a-button type="primary" @click="edit(record)">
                <template #icon><EditOutlined/></template>
                编辑
              </a-button>
              <a-popconfirm
                  title="确认删除用户?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger">
                  <template #icon><DeleteOutlined/></template>
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
        <a-form-item label="登录名">
          <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="user.name"/>
        </a-form-item>
        <a-form-item label="密码" v-show="!user.id">
          <a-input v-model:value="user.password"/>
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
        title="重置密码"
        v-model:visible="resetModalVisible"
        :confirm-loading="resetModalLoading"
        @ok="handleResetOk"
    >
      <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="新密码">
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

declare let hexMd5 :any;
declare let KEY :any;

export default defineComponent({
  name: 'AdminUser',

  setup() {
    const queryParam = ref();
    queryParam.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);

    const columns = [
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
        title: '操作',
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
      //参数校验
      let value = user.value.password;
      if (value === '') {
        return message.error('请输入密码');
      } else {
        const regex = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}/;
        if (!regex.test(value)) {
          return message.error('密码 至少包含 数字和英文，长度6-20');
        }
      }

      modalLoading.value = true;
      //传输到后端前先md5加密一次
      user.value.password = hexMd5(user.value.password + KEY);
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
          user.value.password = null;
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

    const handleDelete = (id: string) => {
      axios.delete("/user/delete/" + id).then((response) => {
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

    //----------重置密码-----------
    const resetModalVisible = ref<boolean>(false);
    const resetModalLoading = ref<boolean>(false);

    const handleResetOk = () => {
      //参数校验
      let value = user.value.password;
      if (value === '') {
        return message.error('请输入密码');
      } else {
        const regex = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}/;
        if (!regex.test(value)) {
          return message.error('密码 至少包含 数字和英文，长度6-20');
        }
      }

      resetModalLoading.value = true;
      //传输到后端前先md5加密一次
      user.value.password = hexMd5(user.value.password + KEY);
      axios.post("/user/resetPassword", user.value).then((response) => {
        resetModalLoading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          resetModalVisible.value = false;
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.content.respMsg);
          user.value.password = '';
        }
      });
    };

    /**
     * 重置密码
     */
    const reset = (record:any) => {
      //复制当前列的参数
      user.value = Tool.copy(record);
      //清空密码
      user.value.password='';
      resetModalVisible.value = true;
    };

    const setAdmin = (id:any) => {
      axios.post("/user/setAdmin/"+ id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
          message.success(data.content)
        } else {
          message.error(data.content.respMsg);
        }
      });
    }
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
      reset,
      setAdmin,
      resetModalVisible,
      resetModalLoading,
      handleResetOk,
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

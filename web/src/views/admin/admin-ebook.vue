<template>
  <div>
    <a-layout>
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-form layout="inline" :model="queryParam">
          <a-form-item>
            <a-input v-model:value="queryParam.name" placeholder="名称">
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
          <a-form-item>
            <a-popconfirm
                title="确认更新[搜索索引]?"
                ok-text="是"
                cancel-text="否"
                @confirm="importDoc"
            >
              <a-button type="primary">
                <template #icon><DiffOutlined /></template>
                更新[搜索索引]
              </a-button>
            </a-popconfirm>
          </a-form-item>
          <a-form-item>
            <a-popconfirm
                title="确认删除30天前的[统计数据]?"
                ok-text="是"
                cancel-text="否"
                @confirm="delete30DayAgoData"
            >
              <a-button type="primary">
                <template #icon><DiffOutlined /></template>
                删除30天前的[统计数据]
              </a-button>
            </a-popconfirm>
          </a-form-item>
        </a-form>

        <a-table
            :columns="columns"
            :row-key="record => record.id"
            :data-source="ebooks"
            :pagination="pagination"
            :loading="loading"
            @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img class="cover" v-if="cover" :src="getImageUrl(cover)" alt="封面"/>
          </template>
          <template v-slot:category="{ text, record }">
            <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
          </template>
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <router-link :to="'/admin/doc?ebookId=' + record.id+ '&name=' +record.name">
                <a-button type="primary">
                  <template #icon><FileTextOutlined /></template>
                  文档管理
                </a-button>
              </router-link>
              <a-button type="primary" @click="edit(record)">
                <template #icon><EditOutlined /></template>
                编辑
              </a-button>
              <a-popconfirm
                  title="确认删除笔记?"
                  ok-text="是"
                  cancel-text="否"
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
        title="笔记表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="封面">
          <a-upload
              name="avatar"
              list-type="picture-card"
              class="avatar-uploader"
              :show-upload-list="false"
              :before-upload="beforeUpload"
              @change="handleChange"
              :customRequest=uploadLink
          >
            <img  v-if="ebook.cover" :src="getImageUrl(ebook.cover)" alt="封面" style="max-width: 300%"/>
            <div v-else>
              <loading-outlined v-if="fileLoading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="ebook.name"/>
        </a-form-item>
        <a-form-item label="分类">
          <a-cascader
              v-model:value="categoryIds"
              :field-names="{ label: 'name', value: 'id', children: 'children' }"
              :options="level1"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="ebook.description" type="textarea"/>
        </a-form-item>
        <a-form-item label="更新时间">
          <a-input v-model:value="ebook.updateTime" type="textarea" disabled/>
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
  name: 'AdminEbook',

  setup() {
    const queryParam = ref();
    queryParam.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    //文件上传
    const fileLoading = ref<boolean>(false);

    //上传前检查
    const beforeUpload = (file) => {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJpgOrPng) {
        message.error('只能上传png,jpg,jpeg格式图片!');
      }
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isLt1M) {
        message.error('文件必须小于1m!');
      }
      return isJpgOrPng && isLt1M;
    };

    //图片上传状态
    const handleChange = (info) => {
      if (info.file.status === 'uploading') {
        fileLoading.value = true;
        return;
      }
      if (info.file.status === 'done') {
        fileLoading.value = false;
        message.success('上传成功');
      }
      if (info.file.status === 'error') {
        fileLoading.value = false;
        message.error('上传失败');
      }
    };

    //自定义的上传方法
    const uploadLink = (info) => {
      let formData = new FormData();           //创建一个FormData表单对象
      formData.append('file', info.file);//将文件加到formData里面
      //将表单对象作为接口变量传过去
      axios({
        headers: {'Content-Type': 'multipart/form-data'},
        url: '/file/fileUpload',
        method: 'post',
        data: formData
      }).then((response) => {
            const data = response.data;
            if (data.code == process.env.VUE_APP_SUCCESS) {
              message.success("上传成功");
              ebook.value.cover = data.content;
            } else {
              message.error(data.content.respMsg);
            }
          })

    };

    const getImageUrl = (url) => {
      return process.env.VUE_APP_SERVER + url;
    }

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: queryParam.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          ebooks.value = data.content.list;
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
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
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
      ebook.value = {};
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      //复制当前列的参数
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };

    const handleDelete = (id:string) => {
      axios.delete("/ebook/delete/"+id).then((response) => {
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

          // 加载完分类后，再加载笔记，否则如果分类树加载很慢，则笔记渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    const importDoc = () => {
      axios.get("/search/importDoc").then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          let text = data.content;
          message.success(text);
        } else {
          message.error(data.content.respMsg);
        }
      });
    }

    const delete30DayAgoData = () => {
      axios.get("/ebookSnapshot/delete30DayAgoData").then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          let text = data.content;
          message.success(text);
        } else {
          message.error(data.content.respMsg);
        }
      });
    }


    onMounted(() => {
      handleQueryCategory();
    });

    return {
      queryParam,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      fileLoading,
      uploadLink,
      handleChange,
      beforeUpload,
      getImageUrl,

      importDoc,
      delete30DayAgoData,
      add,
      edit,
      ebook,
      categoryIds,
      level1,
      modalVisible,
      modalLoading,
      getCategoryName,
      handleModalOk,
      handleDelete
    }
  },
});
</script>

<style>
.avatar-uploader > .ant-upload {
  width: 50px;
  height: 50px;
}
</style>

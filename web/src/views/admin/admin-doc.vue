<template>
  <div>
    <a-layout>
      <a-row>
        <a-col :span="8">
          <a-page-header
              style="height: 60px"
              title="文档管理"
              :sub-title="bookName"
              @back="back"
          />
          <a-layout-content :style="{ background: '#fff', padding: '24px', minHeight: '280px' }">
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
                v-if="level1.length > 0"
                :columns="columns"
                :row-key="record => record.id"
                :data-source="level1"
                :loading="loading"
                :pagination="false"
                @change="handleTableChange"
                size="small"
                :default-expand-all-rows="true"
            >
              <template #name="{ text, record }">
                {{record.sort}} - {{text}}
              </template>
              <template v-slot:action="{ text, record }">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    <template #icon><EditOutlined /></template>
                    编辑
                  </a-button>
                  <a-popconfirm
                      title="确认删除文档及其子文档?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      <template #icon><DeleteOutlined /></template>
                      删除
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>
          </a-layout-content>
        </a-col>
        <a-col :span="16">
          <a-form layout="inline" style="margin-top:10px;margin-left: 10px">
            <a-form-item>
              <a-button v-if="addStatus" type="primary" @click="handleAdd()">
                <template #icon><FileAddOutlined /></template>
                新增文档
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button v-if="!addStatus" type="primary" @click="handleSave()">
                <template #icon><SaveOutlined/></template>
                更新
              </a-button>
            </a-form-item>
          </a-form>
          <a-form :model="doc" layout="vertical" style="margin-top:10px;margin-left: 10px" >

            <a-form-item >
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key:'id',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangEditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout>
  </div>
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message, Modal} from 'ant-design-vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import router from "@/router";
import Editor from "wangeditor"

export default defineComponent({
  name: 'AdminDoc',

  setup() {
    const route =useRoute();
    const bookName =ref();
    const ebookId =ref();
    const queryParam = ref();
    queryParam.value = {};
    const docs = ref();
    const loading = ref(false);
    const addStatus =ref(false);
    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value=[];

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: '操作',
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
      axios.get("/doc/all/"+ebookId.value).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

          level1.value = [];
          //使用递归将数组转为树形结构
          level1.value = Tool.array2Tree(docs.value, 0);

          //父文档下拉框初始化
          treeSelectData.value = Tool.copy(level1.value);
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);
          //设置为新增按钮
          addStatus.value = true;
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        }
      });
    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/findContent/"+doc.value.id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          docs.value = data.content;

          editor.txt.html(data.content);
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
    const doc = ref();
    doc.value = {};
    let editor;

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id == id) {
          // 如果当前节点就是目标节点,将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    /**
     * 删除时查找整根树枝,删除当前节点及其子节点
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标ID放入结果集ids
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 新增
     */
    const add = () => {
      addStatus.value=true;
      //清空内容
      editor.txt.clear();
      doc.value={};
      doc.value.ebookId=ebookId.value;
      treeSelectData.value = Tool.copy(level1.value);
      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      addStatus.value=false;
      //复制当前列的参数
      doc.value = Tool.copy(record);
      //查询文档内容
      handleQueryContent();
      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    const handleSave = () => {
      doc.value.content =editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          message.success(data.content);
          //重新加载列表
          handleQuery();
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const handleAdd = () => {
      handleSave();
      //保存后调用add方法清空数据防止重复添加
      add();
    };

    const handleDelete = (id:string) => {
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value,id);
      Modal.confirm({
        title: () => { return '重要提醒' },
        icon: () => { return createVNode(ExclamationCircleOutlined); },
        content: () => { return createVNode('div', {style: 'color:red;'},
            '将删除:[' + deleteNames.join(",") + "],确认删除?");},
        okText: () => { return '确认' },
        cancelText: () => { return '取消' },
        onOk() {
          axios.delete("/doc/delete/"+deleteIds.join(",")).then((response) => {
            const data = response.data;
            if (data.code == process.env.VUE_APP_SUCCESS) {
              message.success("已删除:["+deleteNames.join(",")+"]");
              //重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
      });
    };

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    onMounted(() => {
      bookName.value=route.query.name;
      ebookId.value=route.query.ebookId;
      handleQuery();
      doc.value.ebookId=ebookId.value;
      //初始化富文本框
      editor = new Editor('#content');
      editor.config.zIndex=0;
      editor.create();
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
      back,
      addStatus,
      doc,
      bookName,
      treeSelectData,
      handleAdd,
      handleSave,
      handleDelete,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    }
  },
});
</script>

<style>

</style>

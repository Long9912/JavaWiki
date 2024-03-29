<template>
  <div>
    <a-layout>
      <a-row>
        <a-col :span="7">
          <a-back-top />
          <a-page-header
              style="height: 60px"
              title="文章管理"
              :sub-title="bookName"
              @back="back"
          />
          <a-layout-content :style="{ background: '#fff', padding: '0px 24px', minHeight: '280px' }">
            <a-form layout="inline" :model="queryParam">
              <a-form-item>
                <a-button type="primary" @click="add" size="small">
                  <template #icon><PlusSquareOutlined /></template>
                  新增文章
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleOpen" size="small">
                  <template #icon><DownSquareOutlined /></template>
                  展开
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleClose" size="small">
                  <template #icon><UpSquareOutlined /></template>
                  折叠
                </a-button>
              </a-form-item>
            </a-form>

            <a-table
                v-if="level1.length > 0 && refreshTable"
                :columns="columns"
                :row-key="record => record.id"
                :data-source="level1"
                :loading="loading"
                :pagination="pagination"
                @change="handleTableChange"
                size="small"
                :default-expand-all-rows="expand"
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
                      title="确认删除文章及其子文章?"
                      ok-text="是"
                      cancel-text="否"
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
        <a-col :span="17">
          <div style="margin-left: 20px;margin-top: 20px" v-show="doc.createTime!=null">
            <span><CalendarOutlined /> <a-tag color="blue">{{doc.createTime}}</a-tag></span>
            <span v-show="doc.updateTime != null"><FieldTimeOutlined /> <a-tag color="green">{{doc.updateTime}}</a-tag></span>&nbsp;
            <span><EyeOutlined /> <a-tag color="orange">{{doc.viewCount}}</a-tag></span>
            <span><LikeOutlined/><a-tag color="red">{{doc.voteCount}}</a-tag></span>
          </div>
          <a-form :model="doc" layout="vertical" style="margin-top:30px;margin-left: 10px" >
            <a-form-item >
              <a-tag color="red">标题:</a-tag>
              <a-input v-model:value="doc.name" placeholder="标题" style="width: 90%;margin-left: 10px"/>
            </a-form-item>
            <a-form-item>
              <a-tag color="cyan">父文章:</a-tag>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 90%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文章"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key:'id',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-tag color="blue">顺序:</a-tag>
              <a-input-number id="inputNumber" v-model:value="doc.sort" :min="1" :max="1000" placeholder="顺序" style="margin-left: 10px"/>
            </a-form-item>
            <a-form-item>
                <a-button v-show="addStatus" type="primary" @click="handleAdd()">
                  <template #icon><FileAddOutlined /></template>
                  新增文章
                </a-button>
                <a-button v-show="!addStatus" type="primary" @click="handleSave()">
                  <template #icon><SaveOutlined/></template>
                  更新
                </a-button>
              <a-button type="primary" @click="handlePreviewContent()" style="margin-left: 20px">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>

          <a-form layout="inline" style="margin-left: 80%">
            <a-form-item>
              <a-button v-show="addStatus" type="primary" @click="handleAdd()">
                <template #icon><FileAddOutlined /></template>
                新增文章
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button v-show="!addStatus" type="primary" @click="handleSave()">
                <template #icon><SaveOutlined/></template>
                更新
              </a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="1000" placement="right" :closable="true" :visible="drawerVisible" @close="onDrawerClose">
        <a-row>
          <a-col :span="18">
            <div class="wangEditor" ref="docContent"  :innerHTML="previewHtml"></div>
          </a-col>
          <a-col :span="6">
            <a-affix :offset-top="top">
              <div class="catalogs">
                <div class="catalog-title">文章目录</div>
                <a-anchor>
                  <template v-for="(item,index) in catalog" :key="index">
                    <a-anchor-link
                        :style="{ paddingLeft:item.level * 15 -10 +'px'}"
                        :href="'#'+item.id"
                        :title=item.title
                    />
                  </template>
                </a-anchor>
              </div>
            </a-affix>
          </a-col>
        </a-row>
      </a-drawer>

    </a-layout>
  </div>
</template>

<script lang="ts">
import {createVNode, defineComponent, nextTick, onMounted, ref} from 'vue';
import axios from "axios";
import {message, Modal} from 'ant-design-vue';
import {ExclamationCircleOutlined} from '@ant-design/icons-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import router from "@/router";
import Editor from "wangeditor"

declare let hljs :any;

export default defineComponent({
  name: 'AdminDoc',

  setup() {
    const route =useRoute();
    const bookName =ref();
    const ebookId =ref();
    const pagination = ref({
      pageSize: 10
    });
    const queryParam = ref();
    queryParam.value = {};
    const docs = ref();
    const loading = ref(false);
    const addStatus =ref(true);
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
     * 一级文章树，children属性就是二级文章
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文章树，children属性就是二级文章完成文章表基本增删改查功能
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

          //父文章下拉框初始化
          treeSelectData.value = Tool.copy(level1.value);
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        }
      });
    };

    /**
     * 文章查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/editContent/"+doc.value.id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          editor.txt.html(data.content);
        } else {
          editor.txt.clear();
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
          // console.log("delete", node);
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

    //设置富文本框
    const editor = new Editor('#content');
    // 取消自动 focus
    editor.config.focus = false
    // 设置编辑区域高度为 450px
    editor.config.height = 450
    editor.config.zIndex=0;
    //自定义上传方法
    editor.config.customUploadImg = function (resultFiles, insertImgFn) {
      // insertImgFn 是获取图片 url 后，插入到编辑器的方法
      //创建一个FormData表单对象
      let imgUrl =null;
      let formData = new FormData();
      //将文件加到formData里面
      formData.append('file', resultFiles[0]);
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
          imgUrl = data.content;
          //增加服务器前缀
          imgUrl = process.env.VUE_APP_SERVER + imgUrl;
          // 上传图片，返回结果，将图片插入到编辑器中
          insertImgFn(imgUrl);
        } else {
          message.error(data.content.respMsg);
        }
      })
    }
    // 挂载代码高亮插件
    editor.highlight = hljs;
    //插入代码语言配置
    editor.config.languageType = [
      'Java',
      'JavaScript',
      'C',
      'JSON',
      'Html',
      'XML',
      'SQL',
      'Go',
      'Markdown',
      'Python',
      'Shell Session'
    ]

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
      generateCatalog();
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    /**
     * 新增
     */
    const add = () => {
      addStatus.value=true;
      //清空内容
      editor.txt.clear();
      //记录父文章
      const parent = doc.value.parent;
      //console.log(parent)
      doc.value= {};

      doc.value.ebookId=ebookId.value;
      treeSelectData.value = Tool.copy(level1.value) || [];
      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});

      //添加子文章
      if (parent != 0 && parent != null) {
        //回填父文章id
        doc.value.parent = parent;
        //获取子文档数
        let count = countChildren(treeSelectData.value,parent);
        //根据子文档数自动填入顺序
        doc.value.sort = count + 1;
      }
      else {  //添加父文档
        //父文档自动选择 "无"
        doc.value.parent = 0;
        //根据父文档数自动填入顺序
        doc.value.sort = (level1.value.length+1);
      }
    };

    /**
     * 计算子文档节点个数
     */
    const countChildren = (treeSelectData: any, id: any) => {
      let count = 0;
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id == id) {
          // 获取子节点个数
          count = node.children.length;
        }
      }
      return count;
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

    let result = false;
    const handleSave = () => {
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          message.success(data.content);
          //重新加载列表
          handleQuery();
          result = true;
        } else {
          message.error(data.content.respMsg);
          result = false;
        }
      });
    };

    const handleAdd = () => {
      handleSave();
      //保存成功后调用add方法清空数据防止重复添加
      //因为axios是异步方法,需要等待一会后获取结果
      setTimeout(()=> {
        if (result) {
          add();
        }
      }, 500);
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

    const expand = ref(true); //是否展开
    const refreshTable = ref(true); //重新渲染表格
    //表格展开
    const handleOpen = () => {
      refreshTable.value = false;
      expand.value = true;
      nextTick(() => {
        refreshTable.value = true;
      });
    }
   //表格收起
   const handleClose = () => {
      refreshTable.value = false;
      expand.value = false;
      nextTick(() => {
        refreshTable.value = true;
      });
    }

    const docContent:any = ref(null);//获取文档内容
    const catalog = ref();//生成的目录
    /**
     * 生成文档目录
     **/
    const generateCatalog = () => {
      nextTick(() => { //等待视图渲染
        //获取DOM元素
        const article_content = docContent.value.childNodes;
        const titleTag = ["H1", "H2", "H3"];
        let titles:any = [];
        //正则表达式，获取html标签
        const reg = new RegExp("<.*?(>|/>)","g");
        let text:any = "";

        article_content.forEach((e, index) => {
          if (titleTag.includes(e.nodeName)) {
            const id = "header-" + index;
            //获取标签id,设置跳转
            e.setAttribute("id", id);
            //替换标签
            text = e.innerHTML.replace(reg, '');
            text = text.replace("&nbsp;", '');
            //判断非空字符串
            if (text != 'undefined' && text && /[^\s]/.test(text)) {
              titles.push({
                id: id,
                title: text,
                level: Number(e.nodeName.substring(1, 2)),
              });
            }
          }
        })
        catalog.value = titles;
      });
    }

    onMounted(() => {
      bookName.value=route.query.name;
      ebookId.value=route.query.ebookId;
      handleQuery();
      doc.value.ebookId=ebookId.value;
      editor.create();
    });

    return {
      pagination,
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

      expand,
      refreshTable,
      handleOpen,
      handleClose,
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
      catalog,
      docContent,
    }
  },
});
</script>

<style>

</style>

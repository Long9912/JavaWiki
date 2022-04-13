<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', minHeight: '280px' }">
      <a-row>
        <a-col :span="5">
          <a-back-top />
          <a-affix :offset-top="top">
            <div class="scrollable-container">
              <a-page-header
                  style="height: 70px"
                  :title="bookName"
                  @back="back"
              />
              <h3 v-if="level1.length === 0">对不起，找不到相关文章！</h3>
              <a-tree
                  v-if="level1.length > 0"
                  :tree-data="level1"
                  @select="onSelect"
                  :replaceFields="{title:'name', key:'id',value:'id'}"
                  :defaultExpandAll="true"
                  :defaultSelectedKeys="defaultSelectedKeys"
              >
              </a-tree>
            </div>
          </a-affix>
        </a-col>
        <a-col :span="15" v-if="level1.length !== 0" :style="{ paddingLeft:'15px' }">
          <h2>{{doc.name}}</h2>
          <div>
            <div>
              <span><CalendarOutlined /> <a-tag color="cyan">{{doc.createTime}}</a-tag></span>
              <span><FieldTimeOutlined /> <a-tag color="green">{{doc.updateTime}}</a-tag></span>&nbsp;
              <span><EyeOutlined /> <a-tag color="blue">{{doc.viewCount}}</a-tag></span>
              <span><LikeOutlined/><a-tag color="red">{{doc.voteCount}}</a-tag></span>
            </div>
            <a-divider style="height: 2px; background-color: #1890ff"/>
          </div>
          <div class="wangEditor" ref="docContent" :innerHTML="html"></div>
          <div class="vote">
            <a-button size="large" type="primary" shape="round" @click="vote">
                <template #icon><LikeOutlined/>&nbsp;点赞:{{doc.voteCount}}</template>
            </a-button>
          </div>
        </a-col>
        <a-col :span="4">
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
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, nextTick, onMounted, ref} from 'vue';
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
    // 当前选中的文章
    const doc = ref();
    doc.value = {};

    /**
     * 一级文章树，children属性就是二级文档
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
     * 文档内容查询
     **/
    const handleQueryContent = (id: any) => {
      axios.get("/doc/findContent/" + id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          html.value = data.content;
          //生成文档目录
          generateCatalog();
          //回到页面顶部
          scrollToTop();
        } else {
          html.value = null;
          message.error(data.content.respMsg);
        }
      });
    };

    const scrollToTop = () => {
      //增加计时器实现顺滑过度
      let timer = setInterval(() => {
        let distance = Math.floor(document.documentElement.scrollTop / 5); //每次移动剩余高度的5分之一距离
        document.documentElement.scrollTop -= distance;
        if (document.documentElement.scrollTop < 5) {   //在剩余高度为5以下时停止，避免死循环
          clearInterval(timer);
        }
      }, 20)
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
    /**
     * 获取专栏列表，没有指定文档时显示第一个文档
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
            //没有指定文档时显示第一个文档
            if (docID.value == null){
              handleQueryContent(level1.value[0].id);
              // 初始显示文档信息
              doc.value = level1.value[0];
            }
          }
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      if (Tool.isNotEmpty(selectedKeys)) {
        // 选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
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

    /**
     * 点赞
     */
    const vote = () => {
      axios.get("/doc/vote/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          message.success("点赞成功");
          doc.value.voteCount++;
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    /**
     * 指定ID获取文档数据
     */
    const handleQueryDoc = (id:any) => {
      axios.get("/doc/findDoc/" + id).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          doc.value = data.content;
        } else {
          message.error(data.content.respMsg);
        }
      });
    };

    const docID = ref();

    onMounted(() => {
      bookName.value = route.query.name;
      bookName.value = bookName.value.replaceAll(/<.*?>/ig,"");
      ebookId.value = route.query.ebookId;
      handleQuery();
      docID.value = route.query.id;
      //指定文档ID时查询指定文档
      if (docID.value != null){
        handleQueryContent(docID.value);
        //查询文档消息
        handleQueryDoc(docID.value)
        //生成文档目录
        generateCatalog();
      }
    });

    return {
      level1,
      bookName,
      html,
      defaultSelectedKeys,
      doc,
      back,
      onSelect,
      vote,
      docContent,
      catalog,
    }
  },
});
</script>

<style>
/* table 样式 */
.wangEditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
  max-width: 80%;
}

.wangEditor table td,
.wangEditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
  text-align: center;
}

.wangEditor table th {
  text-align: center;
  background-color: #eee;
}

.wangEditor table tbody tr:nth-child(even){
  background-color: #eee;
}

.wangEditor table tbody tr:hover{
  background-color: #ccc;
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

/* 图片自适应 */
.wangEditor img {
  max-width: 100%;
  height: auto;
}

.vote {
  padding: 15px;
  text-align: center;
}

.scrollable-container {
  position:fixed;
  height: 100%;
  width: 20%;
  overflow:scroll;
  background-color: #f7f7f7;
}

.catalog-title {
  margin-bottom: 0.5rem;
  border-bottom: 0.1rem solid #0184bb;
  min-height: 1.625rem;
  font-weight: bold;
  font-size: 16px;
}

.catalogs {
  position: sticky;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  margin: 0.25rem;
  padding: 0.5rem;
  box-shadow: 0 0 1rem lightgrey;
}

</style>

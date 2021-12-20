import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios';
import 'ant-design-vue/dist/antd.css';
import {Tool} from "@/util/tool";
//按需引入图标
import {
    EditOutlined,
    DeleteOutlined,
    DownSquareOutlined,
    UpSquareOutlined,
    FileAddOutlined,
    SaveOutlined,
    UserOutlined,
    PlusSquareOutlined,
    CalendarOutlined,
    FieldTimeOutlined,
    EyeOutlined,
    LikeOutlined,
    SearchOutlined,
    DiffOutlined,
    FileTextOutlined,
    RedoOutlined,
    UserAddOutlined,
    UserDeleteOutlined,
    HomeOutlined,
    BookOutlined,
    ReadOutlined,
    FileOutlined,
} from '@ant-design/icons-vue';

import {
    Button,
    Layout,
    Menu,
    message,
    Input,
    Space,
    Dropdown,
    Divider,
    Form,
    Modal,
    Tree,
    Drawer,
    Row,
    Col,
    Select,
    Popconfirm,
    InputNumber,
    Table,
    Pagination,
    List,
    Affix,
    PageHeader,
    Cascader,
    Upload,
    Card,
    Statistic,
    Tag,
    Anchor,
    BackTop,
    TreeSelect,
} from 'ant-design-vue'

const app = createApp(App);
app.use(store).use(router)
    .use(Button)
    .use(Layout)
    .use(Menu)
    .use(Input)
    .use(Space)
    .use(Dropdown)
    .use(Divider)
    .use(Form)
    .use(Modal)
    .use(Tree)
    .use(Drawer)
    .use(Row)
    .use(Col)
    .use(Select)
    .use(Popconfirm)
    .use(InputNumber)
    .use(Table)
    .use(Pagination)
    .use(router)
    .use(List)
    .use(Affix)
    .use(PageHeader)
    .use(Cascader)
    .use(Upload)
    .use(Card)
    .use(Statistic)
    .use(Tag)
    .use(Anchor)
    .use(BackTop)
    .use(TreeSelect)
    .mount('#app')

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    //请求头增加token
    const token=store.state.user.token;
    if (Tool.isNotEmpty(token)){
        config.headers.token = token;
        console.log("前端请求增加token:",token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER);

//使用图标
app.component("LikeOutlined", LikeOutlined);
app.component("EditOutlined", EditOutlined);
app.component("DeleteOutlined", DeleteOutlined);
app.component("DownSquareOutlined", DownSquareOutlined);
app.component("UpSquareOutlined", UpSquareOutlined);
app.component("FileAddOutlined", FileAddOutlined);
app.component("SaveOutlined", SaveOutlined);
app.component("UserOutlined", UserOutlined);
app.component("PlusSquareOutlined", PlusSquareOutlined);
app.component("CalendarOutlined", CalendarOutlined);
app.component("FieldTimeOutlined", FieldTimeOutlined);
app.component("EyeOutlined", EyeOutlined);
app.component("SearchOutlined", SearchOutlined);
app.component("DiffOutlined", DiffOutlined);
app.component("FileTextOutlined", FileTextOutlined);
app.component("RedoOutlined", RedoOutlined);
app.component("UserAddOutlined", UserAddOutlined);
app.component("UserDeleteOutlined", UserDeleteOutlined);
app.component("HomeOutlined", HomeOutlined);
app.component("BookOutlined", BookOutlined);
app.component("ReadOutlined", ReadOutlined);
app.component("FileOutlined", FileOutlined);

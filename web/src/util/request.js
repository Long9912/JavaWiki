import axios from "axios";

// 初始化一些参数
const service = axios.create({
    baseURL: process.env.VUE_APP_SERVER,
});
// 下面可以添加拦截器

export default service;

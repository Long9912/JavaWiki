<template>
  <a-layout-header class="header" style="padding-left: 20px">
    <div class="logo">
      <img class="logoImg" src="../../public/image/logo.jpg">
    </div>
    <a-popconfirm
        title="确认退出?"
        ok-text="是"
        cancel-text="否"
        @confirm="logout"
    >
      <a class="login-menu" v-show="user.id">
        <span>退出登录</span>
      </a>
    </a-popconfirm>
    <a class="login-menu" v-show="user.id">
      <span>欢迎: {{ user.name }}</span>
    </a>
    <a class="login-menu" v-show="!user.id" @click="showLoginModal">
      <span>登录</span>
    </a>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="home">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">项目介绍</router-link>
      </a-menu-item>
      <a-menu-item key="admin-user" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin-ebook" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin-category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>

  <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
  >
    <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="用户名">
        <a-input v-model:value="loginUser.loginName"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";

declare let hexMd5 :any;
declare let KEY :any;

export default defineComponent({
  name: "the-header",
  setup() {
    //登录后信息
    const user = computed(() => store.state.user);
    //登录
    const loginUser = ref({
      loginName: 'test',
      password: 'test123'
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true
    }
    const login = () => {
      //参数校验
      let value = loginUser.value.password;
      if (value === '') {
        return message.error('请输入密码');
      } else {
        const regex = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}/;
        if (!regex.test(value)) {
          return message.error('密码 至少包含 数字和英文，长度6-20');
        }
      }

      loginModalLoading.value = true;
      //传输到后端前先md5加密一次
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          loginModalVisible.value = false;
          message.success("登录成功");
          //用户信息存入vuex
          store.commit("setUser",data.content);

          loginUser.value.password = '';
        } else {
          loginUser.value.password = '';
          message.error(data.content.respMsg);
        }
      });
    }

    const logout = () => {
      axios.delete("/user/logout/" + user.value.token).then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          message.success("退出登录成功");
          //vuex清空用户信息
          store.commit("setUser",{});
        } else {
          message.error(data.content.respMsg);
        }
      });
    }

    return{
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      user,
      login,
      logout,
    }
  }
})
</script>

<style>
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}

.logo {
  width: 160px;
  height: 40px;
  float: left;
}

.logoImg {
  width: 160px;
  height: 40px;
}
</style>

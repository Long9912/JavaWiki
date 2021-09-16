<template>
  <a-layout-header class="header">
    <div class="logo">Java知识库</div>
    <a class="login-menu" @click="showLoginModal">
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
      <a-menu-item key="admin-user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin-ebook">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin-category">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">关于我们</router-link>
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
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: "the-header",
  setup() {
    const loginUser = ref({
      loginName: 'test',
      password: 'test'
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true
    }
    const login = () => {
      console.log("开始登录")
    }
    return{
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
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
  width: 120px;
  height: 31px;
  float: left;
  color: white;
  font-size: 18px;
}
</style>

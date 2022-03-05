<template>
  <a-layout-header class="header" style="padding-left: 20px">
    <div class="logo">
      <img class="logoImg" src="../../public/image/logo.jpg" alt="logo">
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
    <a-dropdown>
      <a class="login-menu" @click.prevent v-show="user.id">
        <span>欢迎: {{ user.name }}</span>
        <UserOutlined />
      </a>
      <template #overlay>
        <a-menu v-show="user.isAdmin === 'true' ">
          <a-menu-item>
            <a-popconfirm
                title="确认更新[搜索索引]?"
                ok-text="是"
                cancel-text="否"
                @confirm="importDoc"
            >
              <DiffOutlined />更新[搜索索引]
            </a-popconfirm>
          </a-menu-item>
          <a-menu-item>
            <a-popconfirm
                title="确认删除30天前的[统计数据]?"
                ok-text="是"
                cancel-text="否"
                @confirm="delete30DayAgoData"
            >
              <DeleteOutlined /> 删除30天前的[统计数据]
            </a-popconfirm>
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <a class="login-menu" v-show="!user.id" @click="showLoginModal">
      <span>登录</span>
    </a>
    <div class="search-box">
      <a-input-search
          v-model:value="searchText"
          placeholder="搜索文章"
          @search="onSearch"
      />
    </div>
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
        <router-link to="/admin/ebook">专栏管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin-category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category">专栏分类管理</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>

  <a-modal
      title="登录"
      okText="登录"
      cancelText="取消"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
  >
    <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="用户名">
        <a-input v-model:value="loginUser.loginName" placeholder="测试用户:test"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password" placeholder="测试密码:test123"/>
      </a-form-item>
      <a-form-item label="验证码">
        <a-input v-model:value="loginUser.code" style="width: 30%" />
        <img width="100" height="50" v-show="image!=null" :src="image" alt="验证码"/>
        <a style="margin-left: 30px" @click="getCaptchaCode">
          <span>获取验证码</span>
        </a>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";
import {useRouter} from "vue-router";

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
      password: 'test123',
      code: '',
      captchaKey: ''
    });
    const image = ref();
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
          loginUser.value.code = '';
        } else {
          //测试用户登录失败时自动重填密码
          if (loginUser.value.loginName == 'test') {
            loginUser.value.password = 'test123';
          } else {
            loginUser.value.password = '';
          }
          //刷新验证码
          getCaptchaCode();
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
    //获取验证码
    const getCaptchaCode = () => {
      axios.get("/user/captcha").then((response) => {
        const data = response.data;
        if (data.code == process.env.VUE_APP_SUCCESS) {
          loginUser.value.code = '';
          loginUser.value.captchaKey = data.content.captchaKey;
          image.value = data.content.image;
        } else {
          message.error(data.content.respMsg);
        }
      });
    }

    const searchText = ref<string>();
    const router = useRouter();
    const onSearch = (searchValue: string) => {
      router.push({path: '/search', query: { keyword: searchValue}})
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

    return{
      image,
      getCaptchaCode,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      user,
      login,
      logout,
      searchText,
      onSearch,
      importDoc,
      delete30DayAgoData
    }
  }
})
</script>

<style>
.search-box {
  width: 120px;
  position: relative;
  display: flex;
  float: right;
  margin-bottom:-50px;
  padding-top: 18px;
}

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

<template>
  <a-layout-footer style="text-align: center">
    Java知识库  <span v-show="user.id">欢迎 : {{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted} from "vue";
import store from "@/store";
import {Tool} from "@/util/tool";
import { notification } from "ant-design-vue";

export default defineComponent({
  name: "the-footer",
  setup(){
    const user = computed(() => store.state.user);

    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log('WebSocket连接成功，状态码：', websocket.readyState)
    };
    const onMessage = (event: any) => {
      notification['info']({
        message: '收到消息',
        description: event.data,
      });
    };
    const onError = () => {
      console.log('WebSocket连接错误，状态码：', websocket.readyState)
    };
    const onClose = () => {
      console.log('WebSocket连接关闭，状态码：', websocket.readyState)
    };
    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(() => {
      // WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        // 连接地址：ws://127.0.0.1:8090/webSocket/xxx
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/webSocket/' + token);
        initWebSocket()

        // 关闭
        // websocket.close();
      } else {
        alert('当前浏览器 不支持')
      }
    });

    return{
      user
    }
  }
})
</script>

<style scoped>

</style>

<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <div class="about">
        <h2>JavaWiki</h2>
        <p>可以管理电子书,支持无限级树文档的wiki知识库系统</p>
        <p>使用Spring Boot + Vue3 &amp; Ant Design Vue 搭建</p>
        <h2>技术点</h2>
        <ul>
          <li><strong>全局统一接口返回值处理</strong> 通过 <strong>@RestControllerAdvice</strong> 进行全局统一接口响应处理,用标准响应类包装返回值</li>
          <li><strong>全局统一异常处理</strong> 通过参数校验,自定义异常枚举类,自定义业务异常,自定义异常封装,统一处理异常</li>
          <li><strong>无限级树设计</strong> 在前端通过递归将数组转为有父子结构的树形结构,并在编辑时禁止将子树作为父类</li>
          <li><strong>AOP</strong> 通过切面打印接口耗时,请求返回参数在日志中,在点赞时获取远程ip</li>
          <li><strong>ECharts</strong> 首页30天数据统计展示</li>
          <li><strong>定时任务设计</strong> 定时执行复杂SQL统计电子书数据</li>
          <li><strong>登录校验</strong>  用户登录后,在Redis 存储用户 Token 用于Shiro的接口校验</li>
          <li><strong>权限校验</strong>  登录后,对增删改接口进行拦截,使用Shiro的授权来验证当前用户是否管理员</li>
          <li><strong>接口防重</strong> 点赞时通过AOP获取到用户远程IP,保存在ThreadLocal中,在Redis中将文章id与远程ip作为key,一天只能对一篇文章点赞一次</li>
          <li><strong>WebSocket</strong> 实现向浏览器发送消息通知,点赞后会通知所有连接会话的用户</li>
          <li><strong>异步处理</strong> 点赞后,通知功能异步处理,解耦点赞与通知功能</li>
          <li><strong>日志流水号</strong> 生成唯一ID,在日志中加入日志流水号,在关键业务中打日志,方便问题追踪</li>
          <li><strong>删除服务器不再使用的图片</strong> 通过正则工具类,在文章删除时获取其中图片名,在更新时前后对比得到被删除的图片名,然后删除服务器中对应的图片</li>
          <li><strong>代码生成器</strong> 使用MyBatisPlus的代码生成器,快速生成基础代码</li>
          <li><strong>Json格式转换</strong> 序列化成json时,将所有的Long变成String 因为js中得数字类型为16位,解决精度丢失问题</li>
          <li><strong>密码加密传输和储存</strong> 前端登录时先md5加密一次传输,后端再md5加密一次进行数据校验</li>
          <li><strong>验证码校验</strong> 使用Hutool生成验证码,并生成唯一ID保存在Redis中</li>
          <li><strong>接口文档</strong> Knife4j:Swagger 的增强工具 作为后端 API 接口文档  地址: /doc.html</li>
          <li><strong>监控统计</strong>  Druid 的监控统计页  地址: /druid/index.html</li>
          <li><strong>多环境</strong> 配置文件分别用于开发和生产</li>
        </ul>

        <h2>依赖</h2>
        <table style="margin-left: 80px">
          <thead>
          <tr>
            <th>后端依赖项</th>
            <th>版本</th>
            <th>说明</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>Spring Boot</td>
            <td>2.5.4</td>
            <td>MVC 框架，容器，切面</td>
          </tr>
          <tr>
            <td>MySQL</td>
            <td>8.0</td>
            <td>数据库</td>
          </tr>
          <tr>
            <td>MyBatisPlus</td>
            <td>3.4.3.3</td>
            <td>持久层框架,简化单表操作,分页</td>
          </tr>
          <tr>
            <td>MyBatisPlusGenerator</td>
            <td>3.4.1</td>
            <td>代码生成器</td>
          </tr>
          <tr>
            <td>Velocity</td>
            <td>2.3</td>
            <td>用于代码生成</td>
          </tr>
          <tr>
            <td>Alibaba Druid</td>
            <td>1.2.6</td>
            <td>数据库连接池</td>
          </tr>
          <tr>
            <td>knife4j</td>
            <td>3.0.3</td>
            <td>接口文档生成工具,Swagger的增强版</td>
          </tr>
          <tr>
            <td>Fastjson</td>
            <td>1.2.76</td>
            <td>处理json格式,解决js中Long精度问题</td>
          </tr>
          <tr>
            <td>Shiro</td>
            <td>1.8.0</td>
            <td>安全框架,用于登录校验与权限校验</td>
          </tr>
          <tr>
            <td>hutool-captcha</td>
            <td>5.7.15</td>
            <td>验证码生成工具</td>
          </tr>
          <tr>
            <td>Redis</td>
            <td>---</td>
            <td>用于登录校验,接口防重设计</td>
          </tr>
          <tr>
            <td>Devtools</td>
            <td>---</td>
            <td>热部署工具</td>
          </tr>
          <tr>
            <td>Log4j2</td>
            <td>---</td>
            <td>Log日志</td>
          </tr>
          <tr>
            <td>Lombok</td>
            <td>---</td>
            <td>减少模板代码工具类</td>
          </tr>
          <tr>
            <td>Validation</td>
            <td>---</td>
            <td>数据验证</td>
          </tr>
          <tr>
            <td>WebSocket</td>
            <td>---</td>
            <td>向浏览器发送通知</td>
          </tr>
          <tr>
            <td>Aop</td>
            <td>---</td>
            <td>开启Aop功能,日志记录</td>
          </tr>
          </tbody>
        </table>
        <br/>
        <table style="margin-left: 80px">
          <thead>
          <tr>
            <th>前端依赖项</th>
            <th>版本</th>
            <th>说明</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>Vue</td>
            <td>3.0</td>
            <td>渐进式 JavaScript 框架</td>
          </tr>
          <tr>
            <td>Ant Design Vue</td>
            <td>2.2.6</td>
            <td>UI组件库</td>
          </tr>
          <tr>
            <td>Axios</td>
            <td>0.21.4</td>
            <td>前后端通信</td>
          </tr>
          <tr>
            <td>Vue-router</td>
            <td>4.0</td>
            <td>前端路由</td>
          </tr>
          <tr>
            <td>Vuex</td>
            <td>4.0</td>
            <td>保存用户状态</td>
          </tr>
          <tr>
            <td>WangEditor</td>
            <td>4.6.3</td>
            <td>富文本编辑器</td>
          </tr>
          </tbody>
        </table>

        <h2>Long9912 </h2>
        <div><b>QQ:792516830</b></div>
        <div><b>微信：longlong991231</b></div>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<style scoped>
.about {
  line-height: 30px;
  padding: 40px;
  background-color: #f5f5f5;
  font-size: 16px;
}

.about h2 {
  margin-bottom: 30px;
  font-weight: 700;
}

.about img {
  margin-top: 10px;
}

.about table{
  width: 80%;
  text-align: center;
  border-collapse: collapse;
}

.about th,td{
  border: 1px solid #999;
  text-align: center;
  padding: 5px 0;
}

.about table thead tr{
  background-color: #eee;
}

.about table tbody tr:nth-child(even){
  background-color: #eee;
}

.about table tbody tr:hover{
  background-color: #ccc;
}

.about table tbody tr td:first-child{
  color: #f40;
}

.about table tfoot tr td{
  text-align: right;
  padding-right: 20px;
}

</style>

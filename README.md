# JavaWiki
可以管理笔记,支持无限级树文档的wiki知识库系统

使用Spring Boot + Vue3 & Ant Design Vue 搭建

## 项目启动

- 后端
    - 安装Lombok插件
    - 更新Maven依赖
    - 执行doc目录下的wiki.sql脚本创建数据库,并创建好wiki用户
    - 在application.yml中修改数据库配置
    - 在application.yml中修改file下的localUrl作为本地文件上传地址
    - 安装并启动Redis
    - 安装并启动ElasticSearch
    - 启动项目:默认端口8090
    - 登录系统后进入笔记管理页面,更新搜索索引

- 前端

    - 安装Node.js
    - 在IDEA中打开终端,输入指令
    ```
    cd web
    npm install
    npm run serve-dev
    ```

## 技术点

- **全局统一接口返回值处理** 通过 **@RestControllerAdvice** 进行全局统一接口响应处理,用标准响应类包装返回值
- **全局统一异常处理** 通过参数校验,自定义异常枚举类,自定义业务异常,自定义异常封装,统一处理异常
- **无限级树设计** 在前端通过递归将数组转为有父子结构的树形结构,并在编辑时禁止将子树作为父类
- **AOP** 通过切面打印接口耗时,请求返回参数在日志中,在点赞时获取远程ip
- **ECharts** 首页30天数据统计展示
- **文档搜索** 使用ElasticSearch对文档索引进行高亮全文搜索
- **定时任务设计** 定时执行复杂SQL统计笔记数据
- **登录校验**  用户登录后,在Redis 存储用户 Token 用于Shiro的接口校验
- **权限校验**  登录后,对增删改接口进行拦截,使用Shiro的授权来验证当前用户是否管理员
- **接口防重** 点赞时通过AOP获取到用户远程IP,保存在ThreadLocal中,在Redis中将文章id与远程ip作为key,一天只能对一篇文章点赞一次
- **WebSocket** 实现向浏览器发送消息通知,点赞后会通知所有连接会话的用户
- **异步处理** 点赞后,通知功能异步处理,解耦点赞与通知功能
- **日志流水号** 生成唯一ID,在日志中加入日志流水号,在关键业务中打日志,方便问题追踪
- **删除服务器不再使用的图片** 通过正则工具类,在文章删除时获取其中图片名,在更新时前后对比得到被删除的图片名,然后删除服务器中对应的图片
- **代码生成器** 使用MyBatisPlus的代码生成器,快速生成基础代码
- **Json格式转换** 序列化成json时,将所有的Long变成String 因为js中得数字类型为16位,解决精度丢失问题
- **密码加密传输和储存** 前端登录时先md5加密一次传输,后端再md5加密一次进行数据校验
- **验证码校验** 使用Hutool生成验证码,并生成唯一ID保存在Redis中
- **接口文档** Knife4j:Swagger 的增强工具 作为后端 API 接口文档  地址: /doc.html
- **监控统计**  Druid 的监控统计页  地址: /druid/index.html
- **多环境** 配置文件分别用于开发和生产

## 依赖

| 后端依赖项           | 版本    | 说明                              |
| -------------------- | ------- | --------------------------------- |
| Spring Boot          | 2.5.4   | MVC 框架，容器，切面              |
| MySQL                | 8.0     | 数据库                            |
| MyBatisPlus          | 3.4.3.3 | 持久层框架,简化单表操作,分页      |
| MyBatisPlusGenerator | 3.4.1   | 代码生成器                        |
| Velocity             | 2.3     | 用于代码生成                      |
| Alibaba Druid        | 1.2.6   | 数据库连接池                      |
| knife4j              | 3.0.3   | 接口文档生成工具,Swagger的增强版   |
| Fastjson             | 1.2.76  | 处理json格式,解决js中Long精度问题 |
| Shiro                | 1.8.0   | 安全框架,用于登录校验与权限校验    |
| hutool-captcha       | 5.7.15  | 验证码生成工具                  |
| ElasticSearch        | 7.15.1  | 搜索引擎                       |
| Redis                | ---     | 用于登录校验,接口防重设计         |
| Devtools             | ---     | 热部署工具                        |
| Log4j2               | 2.15    | Log日志                           |
| Lombok               | ---     | 减少模板代码工具类                |
| Validation           | ---     | 接口参数校验                          |
| WebSocket            | ---     | 向浏览器发送通知                  |
| Aop                  | ---     | 开启Aop功能,用于日志记录              |


| 前端依赖项     | 版本   | 说明                   |
| -------------- | ------ | ---------------------- |
| Vue            | 3.0    | 渐进式 JavaScript 框架 |
| Ant Design Vue | 2.2.6  | UI组件库               |
| Axios          | 0.21.4 | 前后端通信             |
| Vue-router     | 4.0    | 前端路由               |
| Vuex           | 4.0    | 保存用户状态           |
| WangEditor     | 4.6.3  | 富文本编辑器           |
| Echarts        | 5.2.2  | 图表展示              |
| Highlightjs    | 11.3.1 | 代码高亮              |

## 结构

- 后端
    - **aspect**：AOP切面,打印请求信息日志,点赞时获取远程ip,防止重复点赞,一天只能点赞相同文章一次
    - **config**：配置:跨域,json转换,SpringMvc静态资源映射,MyBatisPlus,Swagger配置类
    - **controller**：控制层
    - **entity**：实体类
    - **service**:服务层
    - **exception**：全局异常统一处理模块,自定义异常枚举类,自定义业务异常,自定义异常封装,用于异常处理
    - **interceptor**：Spring拦截器:登录校验,获取前端请求header的Token参数,在Redis中获取是否有效;权限校验,对增删改接口进行拦截,验证当前用户是否管理员,已被Shiro代替
    - **job**:定时任务 定时更新笔记数据,定时更新首页统计数据
    - **mapper**:MyBatis Mapper 接口
    - **elasticsearch**:Elasticsearch配置类,导入数据库数据到搜索索引,高亮搜索
    - **request**:封装接口请求信息,使用**javax.validation**做后端参数校验
    - **response**:封装接口响应信息,定义标准响应类,通过 **@RestControllerAdvice**进行全局统一接口响应处理,对所有控制器中，被 **@RequestMapping**注解标注的方法，进行增强,用标准响应类包装返回值
    - **webSocket**:webSocket配置类,异步调用webSocket连接,点赞时,向所有连接的WebSocket发送通知
    - **shiro**:shiro配置类,shiro的权限过滤器,自定义Realm,自定义Token和获取yml中路径的配置类
    - **util**:
        - **CodeGenerator**:*MyBatisPlus的代码生成器*,快速生成一张新表的所有基础代码,方便开发
        - **CopyUtil**:*复制工具类*,用于实体类与响应类之间复制属性
        - **RedisUtil**:*Redis工具类*,验证一个key是否存在,存在返回false,不存在则放入key,并设置对应有效时间
        - **RegExpUtil**:*正则表达式 工具类*,*获得富文本中的图片文件列表*,方便在服务器中删除不再使用的图片
        - **RequestContext**:用于获取**ThreadLocal**线程本地变量,点赞时:通过AOP获取到远程ip,然后在本线程中保存远程地址,文章id与远程ip存在Redis中,防止重复点赞,一天只能点赞相同文章一次
        - **SnowFlake**:*Twitter的分布式自增ID雪花算法*,用于生成登录Token,日志标识id
        - **LoginUserContext**:保存登录时的用户消息,用于权限校验,改由Shiro验证后被弃用
    - **resources**文件夹
        - **mapper**: 存放自定义sql:复杂统计sql等
        - **application.yml**:测试环境配置文件,配置端口,log配置文件位置,swagger启动选项,图片上传地址,数据库,druid连接池 ,druid监控页配置,时间格式化,MyBatisPlus逻辑删除字段配置,配置需要登录验证的路径
        - **application-prod.yml**:生产环境配置文件,启动配置 java -jar -Dspring.profiles.active=prod
        - **log4j2-spring.yml**:配置日志显示格式,日志存放位置,LOGID配置
- 前端
    - **public**
        - **image** 存放首页logo
        - **js**
            - **md5** 用于登录时密码加密传输
            - **SessionStorage** 解决vuex刷新数据丢失问题
        - **index.html** 放置首次加载提示
    - **src**
        - **components** 放置页面通用组件
        - **router** 前端路由,登录拦截
        - **store** 通过SessionStorage存放vuex的user信息
        - **util**  工具类: 空校验,对象复制,通过递归将数组转为树形结构,生成随机数
        - **views** 放置vue页面
        - **App.vue** vue加载首页
        - **main.ts** 项目的入口文件,实例化Vue,启用项目中的插件,配置axios拦截器,打印请求和返回信息,请求时如果有token在头部加入token
    - **.env.dev || .env.prod** 配置开发和生产环境变量,请求成功码,后端服务器地址

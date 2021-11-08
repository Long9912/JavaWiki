/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 47.107.58.115:3306
 Source Schema         : wiki

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 08/11/2021 23:43:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
     `id` bigint NOT NULL COMMENT 'id',
     `parent` bigint NOT NULL DEFAULT 0 COMMENT '父id',
     `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
     `sort` int NULL DEFAULT NULL COMMENT '顺序',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (100, 0, '前端开发', 100);
INSERT INTO `category` VALUES (102, 100, 'Vue', 101);
INSERT INTO `category` VALUES (200, 0, '后端开发', 200);
INSERT INTO `category` VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` VALUES (202, 200, '框架应用', 203);
INSERT INTO `category` VALUES (300, 0, '数据库', 300);
INSERT INTO `category` VALUES (301, 300, 'Mysql', 301);
INSERT INTO `category` VALUES (302, 300, 'Mybatis', 302);
INSERT INTO `category` VALUES (1446472705908756481, 200, '中间件', 202);

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`  (
    `id` bigint NOT NULL COMMENT '文档id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
    `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
    `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (1, '文档1', '<p>12312345</p>', 0);
INSERT INTO `content` VALUES (2, '文档1.1', '<p>12345678</p>', 0);
INSERT INTO `content` VALUES (1438000187241934850, 'Ant Design of Vue', '<blockquote><h1><font size=\"5\">这里是 Ant Design 的 Vue 实现，开发和服务于企业级后台产品。</font></h1></blockquote><p><br/></p><p><img src=\"https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg\" width=\"167.43\" height=\"167.43\"/>&nbsp; &nbsp;&nbsp;<font size=\"5\">+&nbsp; &nbsp;&nbsp;</font><img src=\"https://qn.antdv.com/vue.png\" width=\"158.43\" height=\"158.43\"/><br/></p><p>特性</p><ul><li>提炼自企业级中后台产品的交互语言和视觉风格。</li><li>开箱即用的高质量 Vue 组件。</li><li>共享<a href=\"http://ant-design.gitee.io/docs/spec/introduce-cn\" target=\"_blank\">Ant Design of React</a>设计工具体系。</li></ul>', 0);
INSERT INTO `content` VALUES (1438000644823724034, '关于ant-design-vue', '<p>众所周知，Ant Design 作为一门设计语言面世，经历过多年的迭代和积累，它对 UI 的设计思想已经成为一套事实标准，受到众多前端开发者及企业的追捧和喜爱，也是 React 开发者手中的神兵利器。希望 ant-design-vue 能够让 Vue 开发者也享受到 Ant Design 的优秀设计。</p><p>ant-design-vue 是 Ant Design 的 Vue 实现，组件的风格与 Ant Design 保持同步，组件的 html 结构和 css 样式也保持一致，真正做到了样式 0 修改，组件 API 也尽量保持了一致。</p><p>Ant Design Vue 致力于提供给程序员愉悦的开发体验!</p>', 0);

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `ebook_id` bigint NOT NULL DEFAULT 0 COMMENT '电子书id',
    `parent` bigint NOT NULL DEFAULT 0 COMMENT '父id',
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
    `sort` int NULL DEFAULT NULL COMMENT '顺序',
    `view_count` int NULL DEFAULT 0 COMMENT '阅读数',
    `vote_count` int NULL DEFAULT 0 COMMENT '点赞数',
    `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文档1', 1, 35, 9, 0, '2021-09-15 21:01:21', '2021-10-07 18:14:57');
INSERT INTO `doc` VALUES (2, 1, 1, '文档1.1', 1, 18, 2, 0, '2021-09-15 21:01:21', '2021-09-23 15:05:43');
INSERT INTO `doc` VALUES (3, 1, 0, '文档2', 2, 10, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (4, 1, 3, '文档2.1', 2, 15, 2, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (5, 1, 1437317930831314945, '文档3.1', 2, 21, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (6, 1, 5, '文档3.1.1', 1, 19, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1437317930831314945, 1, 0, '文档3', 3, 8, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1438000187241934850, 2, 0, 'Ant Design of Vue', 1, 90, 6, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1438000644823724034, 2, 1438000187241934850, '关于ant-design-vue', 3, 50, 4, 0, '2021-09-15 21:01:21', '2021-10-06 00:25:00');

-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
      `id` bigint NOT NULL COMMENT '主键ID',
      `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
      `category1_id` bigint NULL DEFAULT NULL COMMENT '分类1',
      `category2_id` bigint NULL DEFAULT NULL COMMENT '分类2',
      `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
      `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
      `doc_count` int NULL DEFAULT 0 COMMENT '文档数',
      `view_count` int NULL DEFAULT 0 COMMENT '阅读数',
      `vote_count` int NULL DEFAULT 0 COMMENT '点赞数',
      `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
      `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电子书' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook` VALUES (1, 'Spring Boot 笔记', 200, 202, '测试数据', '/image/100580371187503104-1.png', 9, 144, 23, 0, '2021-10-30 15:26:14');
INSERT INTO `ebook` VALUES (2, 'Vue 笔记', 100, 102, '渐进式 JavaScript 框架', '/image/100580394134540289-2.png', 2, 140, 10, 0, '2021-10-08 21:54:17');
INSERT INTO `ebook` VALUES (3, 'MySQL 笔记', 300, 301, '测试数据', '/image/100580414678241280-5.png', 0, 0, 0, 0, '2021-10-30 15:26:19');
INSERT INTO `ebook` VALUES (4, 'Swagger 笔记', 200, 202, '测试数据', '/image/100580432558559233-4.png', 0, 0, 0, 0, '2021-10-30 15:26:23');
INSERT INTO `ebook` VALUES (5, 'MyBatisPlus 笔记', 300, 302, '测试数据', '/image/100580454293442560-5.png', 0, 0, 0, 0, '2021-10-30 15:26:26');
INSERT INTO `ebook` VALUES (1436589909073756161, 'SpringCloud 笔记', 200, 202, '测试数据', '/image/100580471058075648-3.png', 0, 0, 0, 0, '2021-10-30 15:26:29');
INSERT INTO `ebook` VALUES (1436599062148247554, 'MyBatis 笔记', 300, 302, '测试数据', '/image/100580501743603713-5.png', 0, 0, 0, 0, '2021-10-30 15:26:32');
INSERT INTO `ebook` VALUES (1440230385408651266, 'java 笔记', 200, 201, '测试数据', '/image/100580520555057152-4.png', 0, 0, 0, 0, '2021-10-30 15:26:36');
INSERT INTO `ebook` VALUES (1446475575387312129, 'RabbitMQ 笔记', 200, 1446472705908756481, '消息中间件', '/image/101679988444631040-RabbitMQ.png', 22, 82, 2, 0, '2021-10-08 22:12:40');
INSERT INTO `ebook` VALUES (1449683798575644673, 'Nginx笔记', 200, 1446472705908756481, '轻量级的Web服务器、支持反向代理', '/image/109316885362905088-nginx.png', 9, 16, 3, 0, '2021-10-29 23:46:18');
INSERT INTO `ebook` VALUES (1450421385128935425, 'Shiro笔记', 200, 1446472705908756481, '安全认证框架', '/image/105625908567740417-shiro.png', 14, 4, 1, 0, '2021-10-29 23:16:20');
INSERT INTO `ebook` VALUES (1454357816893370370, 'Elasticsearch笔记', 200, 1446472705908756481, '分布式搜索和数据分析引擎', '/image/109562340164374528-Elasticsearch.png', 16, 12, 2, 0, '2021-10-30 16:02:06');

-- ----------------------------
-- Table structure for ebook_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `ebook_snapshot`;
CREATE TABLE `ebook_snapshot`  (
       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
       `ebook_id` bigint NOT NULL DEFAULT 0 COMMENT '电子书id',
       `date` date NOT NULL COMMENT '快照日期',
       `view_count` int NOT NULL DEFAULT 0 COMMENT '阅读数',
       `vote_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
       `view_increase` int NOT NULL DEFAULT 0 COMMENT '阅读增长',
       `vote_increase` int NOT NULL DEFAULT 0 COMMENT '点赞增长',
       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 585 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电子书快照' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ebook_snapshot
-- ----------------------------
INSERT INTO `ebook_snapshot` VALUES (570, 1, '2021-11-08', 144, 23, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (571, 2, '2021-11-08', 140, 10, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (572, 3, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (573, 4, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (574, 5, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (575, 1436589909073756161, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (576, 1436599062148247554, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (577, 1440230385408651266, '2021-11-08', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (578, 1446475575387312129, '2021-11-08', 82, 2, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (579, 1449683798575644673, '2021-11-08', 16, 3, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (580, 1450421385128935425, '2021-11-08', 4, 1, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (581, 1454357816893370370, '2021-11-08', 12, 2, 0, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
         `id` bigint NOT NULL COMMENT '主键ID',
         `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登陆名',
         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
         `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
         `is_admin` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'false' COMMENT '管理员标识',
         PRIMARY KEY (`id`) USING BTREE,
         UNIQUE INDEX `login_name_uindex`(`login_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1433377868295872514, 'Long12', '龙', '068f798972eea38846b627e655dc513c', 'true');
INSERT INTO `user` VALUES (1434857573659443203, 'test', '测试用户', '7354a1d413535a6c0dc5c209e198d799', 'true');
INSERT INTO `user` VALUES (1438141057102610433, 'test2', 'Long12', 'a48895a6380f269780666b2d492d08b2', 'false');
INSERT INTO `user` VALUES (1439132061330874375, 'test3', 'test', '05868668465e6e515f6472740a508ac6', 'false');
INSERT INTO `user` VALUES (1445377119931375618, 'test4', '测试', '296b0f9fc224484c79584a68687943c7', 'false');

SET FOREIGN_KEY_CHECKS = 1;

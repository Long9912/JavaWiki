/*
 Navicat Premium Data Transfer

 Source Server         : wiki@localhost
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : wiki

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 13/09/2021 14:41:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `parent` bigint(255) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (100, 0, '前端开发', 100);
INSERT INTO `category` VALUES (102, 100, 'Vue', 102);
INSERT INTO `category` VALUES (200, 0, 'Java', 200);
INSERT INTO `category` VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` VALUES (202, 200, '框架应用', 202);
INSERT INTO `category` VALUES (300, 0, '数据库', 300);
INSERT INTO `category` VALUES (301, 300, 'Mysql', 301);
INSERT INTO `category` VALUES (302, 300, 'MySQLPlus', 302);
INSERT INTO `category` VALUES (1436952541278535682, 0, 'test', 400);
INSERT INTO `category` VALUES (1436952696622972930, 1436952541278535682, 'test2', 2);

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `ebook_id` bigint(11) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `parent` bigint(50) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int(200) NULL DEFAULT NULL COMMENT '顺序',
  `view_count` int(255) NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(255) NULL DEFAULT 0 COMMENT '点赞数',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文档1', 1, 0, 0, 0);
INSERT INTO `doc` VALUES (2, 1, 1, '文档1.1', 1, 0, 0, 0);
INSERT INTO `doc` VALUES (3, 1, 0, '文档2', 2, 0, 0, 0);
INSERT INTO `doc` VALUES (4, 1, 3, '文档2.1', 2, 0, 0, 0);
INSERT INTO `doc` VALUES (5, 1, 3, '文档2.2', 2, 0, 0, 0);
INSERT INTO `doc` VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0, 0);

-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
  `category1_id` bigint(11) NULL DEFAULT NULL COMMENT '分类1',
  `category2_id` bigint(50) NULL DEFAULT NULL COMMENT '分类2',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `doc_count` int(255) NULL DEFAULT 0 COMMENT '文档数',
  `view_count` int(255) NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(255) NULL DEFAULT 0 COMMENT '点赞数',
  `version` int(255) NULL DEFAULT 1 COMMENT '乐观锁',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电子书' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook` VALUES (1, 'Spring Boot 入门教程', 200, 202, '企业级应用开发最佳首选框架', '/image/1.png', 3, 0, 0, 1, 0, '2021-09-07 21:29:15', '2021-09-12 16:23:52');
INSERT INTO `ebook` VALUES (2, 'Vue 入门教程', 100, 102, '企业级应用开发最佳首选框架', '/image/2.png', 0, 0, 0, 1, 0, '2021-09-07 21:30:25', '2021-09-12 16:16:31');
INSERT INTO `ebook` VALUES (3, 'MySQL 入门教程', 300, 301, '企业级应用开发最佳首选框架', '/image/5.png', 0, 0, 0, 1, 0, '2021-09-07 21:30:55', '2021-09-12 16:17:25');
INSERT INTO `ebook` VALUES (4, 'Swagger 入门教程', 1436952541278535682, 1436952696622972930, '企业级应用开发最佳首选框架', '/image/4.png', 0, 0, 0, 1, 0, '2021-09-07 21:34:40', '2021-09-13 13:03:15');
INSERT INTO `ebook` VALUES (5, 'MyBatisPlus 入门教程', 1, 2, '企业级应用开发最佳首选框架', '/image/5.png', 0, 0, 0, 1, 0, '2021-09-07 21:32:06', '2021-09-11 14:44:37');
INSERT INTO `ebook` VALUES (1436589909073756161, 'Spring 入门教程', 1, 2, '入门', '/image/1.png', 0, 0, 0, 1, 0, '2021-09-11 15:18:20', '2021-09-12 16:22:26');
INSERT INTO `ebook` VALUES (1436599062148247554, 'MyBatis 入门教程', 1, 2, NULL, '/image/5.png', 0, 0, 0, 1, 0, '2021-09-11 15:54:42', '2021-09-11 15:54:58');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `version` int(255) NULL DEFAULT 1 COMMENT '乐观锁',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Jone', 18, 'test1@baomidou.com', 1, 0, '2021-09-06 20:14:28', '2021-09-06 20:15:06');
INSERT INTO `user` VALUES (2, 'Jack', 20, 'test2@baomidou.com', 1, 1, '2021-09-06 20:14:28', '2021-09-06 20:15:06');
INSERT INTO `user` VALUES (3, 'Tom', 28, 'test3@baomidou.com', 1, 0, '2021-09-06 20:14:28', '2021-09-06 20:15:06');
INSERT INTO `user` VALUES (4, 'Sandy', 21, 'test4@baomidou.com', 1, 0, '2021-09-06 20:14:28', '2021-09-06 20:15:06');
INSERT INTO `user` VALUES (5, 'Long12', 24, 'qq.com', 2, 0, '2021-09-06 20:14:28', '2021-09-06 21:00:05');
INSERT INTO `user` VALUES (1434857573659443201, 'Long', 18, 'qq.com', 1, 0, NULL, NULL);
INSERT INTO `user` VALUES (1434858980412280833, 'Long', 18, 'qq.com', 1, 0, '2021-09-06 20:40:14', NULL);

SET FOREIGN_KEY_CHECKS = 1;

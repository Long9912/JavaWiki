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

 Date: 10/09/2021 21:58:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `ebook` VALUES (1, 'Spring Boot 入门教程', 1, 2, '企业级应用开发最佳首选框架', '/image/1.png', 3, 0, 0, 1, 0, '2021-09-07 21:29:15', NULL);
INSERT INTO `ebook` VALUES (2, 'Vue 入门教程', NULL, NULL, '企业级应用开发最佳首选框架', '/image/2.png', 0, 0, 0, 1, 0, '2021-09-07 21:30:25', NULL);
INSERT INTO `ebook` VALUES (3, 'MySQL 入门教程', NULL, NULL, '企业级应用开发最佳首选框架', '/image/5.png', 0, 0, 0, 1, 0, '2021-09-07 21:30:55', NULL);
INSERT INTO `ebook` VALUES (4, 'Swagger 入门教程', NULL, NULL, '企业级应用开发最佳首选框架', '/image/4.png', 0, 0, 0, 1, 0, '2021-09-07 21:34:40', NULL);
INSERT INTO `ebook` VALUES (5, 'MyBatisPlus 入门教程', NULL, NULL, '企业级应用开发最佳首选框架', '/image/5.png', 0, 0, 0, 1, 0, '2021-09-07 21:32:06', NULL);

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
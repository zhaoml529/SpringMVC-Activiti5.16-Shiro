/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-08-29 17:21:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_process_model
-- ----------------------------
DROP TABLE IF EXISTS `t_process_model`;
CREATE TABLE `t_process_model` (
  `id` bigint(20) NOT NULL,
  `processKey` varchar(255) DEFAULT NULL,
  `processName` varchar(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '流程状态 1.激活状态  2.挂起状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_process_model
-- ----------------------------
INSERT INTO `t_process_model` VALUES ('1', 'vacation', '请假流程', '2016-08-27 19:22:08', '1');

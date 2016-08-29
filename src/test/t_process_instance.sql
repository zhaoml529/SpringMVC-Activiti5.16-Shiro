/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-08-29 17:20:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_process_instance
-- ----------------------------
DROP TABLE IF EXISTS `t_process_instance`;
CREATE TABLE `t_process_instance` (
  `id` bigint(20) NOT NULL,
  `modelId` bigint(20) DEFAULT NULL,
  `procDefId` varchar(64) DEFAULT NULL COMMENT 't_process_define的id',
  `operationType` char(1) DEFAULT NULL COMMENT '操作类型 1.同意 2.不同意 3.重新申请 4.取消申请 0.结束节点',
  `targetRef` bigint(20) DEFAULT NULL COMMENT '指向节点的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_process_instance
-- ----------------------------
INSERT INTO `t_process_instance` VALUES ('1', '1', '1', '1', '2');
INSERT INTO `t_process_instance` VALUES ('2', '1', '1', '2', '3');
INSERT INTO `t_process_instance` VALUES ('3', '1', '2', '1', '0');
INSERT INTO `t_process_instance` VALUES ('4', '1', '2', '2', '3');
INSERT INTO `t_process_instance` VALUES ('5', '1', '3', '3', '1');
INSERT INTO `t_process_instance` VALUES ('6', '1', '3', '4', '0');

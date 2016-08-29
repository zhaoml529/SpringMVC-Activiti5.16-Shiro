/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-08-29 17:20:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_process_define
-- ----------------------------
DROP TABLE IF EXISTS `t_process_define`;
CREATE TABLE `t_process_define` (
  `id` bigint(20) NOT NULL,
  `modelId` bigint(20) DEFAULT NULL COMMENT '外键',
  `taskId` varchar(100) DEFAULT NULL COMMENT '后台自动生成的 节点id: userTask1',
  `taskName` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `candidateType` char(1) DEFAULT NULL COMMENT '1办理人 2候选人 3候选组',
  `candidateIds` varchar(100) DEFAULT NULL COMMENT '候选人 组',
  `isStartEvent` char(1) DEFAULT NULL COMMENT '是否是起始节点 1.是 0.不是',
  `isInitiator` char(1) DEFAULT NULL,
  `targetGateway` varchar(100) DEFAULT NULL COMMENT '指向的关口名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_process_define
-- ----------------------------
INSERT INTO `t_process_define` VALUES ('1', '1', null, '经理审批', '1', '15', '1', '0', null, null);
INSERT INTO `t_process_define` VALUES ('2', '1', null, '人事审批', '1', '17', '0', '0', null, null);
INSERT INTO `t_process_define` VALUES ('3', '1', null, '修改申请', '1', 'createUserId', '0', '1', null, null);

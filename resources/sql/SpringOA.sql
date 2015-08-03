/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : 127.0.0.1:3306
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2015-08-03 11:43:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_expense_account
-- ----------------------------
DROP TABLE IF EXISTS `t_expense_account`;
CREATE TABLE `t_expense_account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` datetime DEFAULT NULL,
  `MONEY` decimal(19,2) DEFAULT NULL,
  `PROC_INST_ID` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `APPLY_DATE` date DEFAULT NULL,
  `OCCUR_DATE` date DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_expense_account
-- ----------------------------

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group` (
  `GROUP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_group
-- ----------------------------
INSERT INTO `t_group` VALUES ('1', '老板组', 'boss');
INSERT INTO `t_group` VALUES ('2', '总监组', 'director');
INSERT INTO `t_group` VALUES ('3', '员工组', 'employee');
INSERT INTO `t_group` VALUES ('4', '财务组', 'finance');
INSERT INTO `t_group` VALUES ('5', '人事组', 'hr');
INSERT INTO `t_group` VALUES ('6', '经理组', 'manager');
INSERT INTO `t_group` VALUES ('7', '超级管理员', 'admin');
INSERT INTO `t_group` VALUES ('10', 'teste', 'tt');

-- ----------------------------
-- Table structure for t_group_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_group_resource`;
CREATE TABLE `t_group_resource` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_group_resource
-- ----------------------------
INSERT INTO `t_group_resource` VALUES ('3', '1', '13');
INSERT INTO `t_group_resource` VALUES ('4', '1', '14');
INSERT INTO `t_group_resource` VALUES ('5', '1', '15');
INSERT INTO `t_group_resource` VALUES ('6', '1', '16');
INSERT INTO `t_group_resource` VALUES ('7', '1', '17');
INSERT INTO `t_group_resource` VALUES ('9', '1', '22');
INSERT INTO `t_group_resource` VALUES ('10', '1', '23');
INSERT INTO `t_group_resource` VALUES ('11', '1', '24');
INSERT INTO `t_group_resource` VALUES ('12', '1', '25');
INSERT INTO `t_group_resource` VALUES ('13', '1', '26');
INSERT INTO `t_group_resource` VALUES ('14', '1', '27');
INSERT INTO `t_group_resource` VALUES ('15', '1', '28');
INSERT INTO `t_group_resource` VALUES ('16', '1', '29');
INSERT INTO `t_group_resource` VALUES ('49', '3', '11');
INSERT INTO `t_group_resource` VALUES ('50', '3', '12');
INSERT INTO `t_group_resource` VALUES ('51', '3', '13');
INSERT INTO `t_group_resource` VALUES ('52', '3', '14');
INSERT INTO `t_group_resource` VALUES ('53', '3', '15');
INSERT INTO `t_group_resource` VALUES ('54', '3', '16');
INSERT INTO `t_group_resource` VALUES ('55', '3', '17');
INSERT INTO `t_group_resource` VALUES ('56', '3', '21');
INSERT INTO `t_group_resource` VALUES ('57', '3', '22');
INSERT INTO `t_group_resource` VALUES ('58', '3', '23');
INSERT INTO `t_group_resource` VALUES ('59', '3', '24');
INSERT INTO `t_group_resource` VALUES ('60', '3', '25');
INSERT INTO `t_group_resource` VALUES ('61', '3', '26');
INSERT INTO `t_group_resource` VALUES ('62', '3', '27');
INSERT INTO `t_group_resource` VALUES ('63', '3', '28');
INSERT INTO `t_group_resource` VALUES ('64', '3', '29');
INSERT INTO `t_group_resource` VALUES ('65', '3', '31');
INSERT INTO `t_group_resource` VALUES ('66', '3', '32');
INSERT INTO `t_group_resource` VALUES ('67', '3', '41');
INSERT INTO `t_group_resource` VALUES ('68', '3', '42');
INSERT INTO `t_group_resource` VALUES ('69', '3', '51');
INSERT INTO `t_group_resource` VALUES ('70', '3', '52');
INSERT INTO `t_group_resource` VALUES ('71', '4', '11');
INSERT INTO `t_group_resource` VALUES ('72', '4', '12');
INSERT INTO `t_group_resource` VALUES ('73', '4', '13');
INSERT INTO `t_group_resource` VALUES ('74', '4', '14');
INSERT INTO `t_group_resource` VALUES ('75', '4', '15');
INSERT INTO `t_group_resource` VALUES ('76', '4', '16');
INSERT INTO `t_group_resource` VALUES ('77', '4', '17');
INSERT INTO `t_group_resource` VALUES ('78', '4', '21');
INSERT INTO `t_group_resource` VALUES ('79', '4', '22');
INSERT INTO `t_group_resource` VALUES ('80', '4', '23');
INSERT INTO `t_group_resource` VALUES ('81', '4', '24');
INSERT INTO `t_group_resource` VALUES ('82', '4', '25');
INSERT INTO `t_group_resource` VALUES ('83', '4', '26');
INSERT INTO `t_group_resource` VALUES ('84', '4', '27');
INSERT INTO `t_group_resource` VALUES ('85', '4', '28');
INSERT INTO `t_group_resource` VALUES ('86', '4', '29');
INSERT INTO `t_group_resource` VALUES ('87', '4', '31');
INSERT INTO `t_group_resource` VALUES ('88', '4', '32');
INSERT INTO `t_group_resource` VALUES ('89', '4', '41');
INSERT INTO `t_group_resource` VALUES ('90', '4', '42');
INSERT INTO `t_group_resource` VALUES ('91', '4', '51');
INSERT INTO `t_group_resource` VALUES ('92', '4', '52');
INSERT INTO `t_group_resource` VALUES ('93', '5', '11');
INSERT INTO `t_group_resource` VALUES ('94', '5', '12');
INSERT INTO `t_group_resource` VALUES ('95', '5', '13');
INSERT INTO `t_group_resource` VALUES ('96', '5', '14');
INSERT INTO `t_group_resource` VALUES ('97', '5', '15');
INSERT INTO `t_group_resource` VALUES ('98', '5', '16');
INSERT INTO `t_group_resource` VALUES ('99', '5', '17');
INSERT INTO `t_group_resource` VALUES ('100', '5', '21');
INSERT INTO `t_group_resource` VALUES ('101', '5', '22');
INSERT INTO `t_group_resource` VALUES ('102', '5', '23');
INSERT INTO `t_group_resource` VALUES ('103', '5', '24');
INSERT INTO `t_group_resource` VALUES ('104', '5', '25');
INSERT INTO `t_group_resource` VALUES ('105', '5', '26');
INSERT INTO `t_group_resource` VALUES ('106', '5', '27');
INSERT INTO `t_group_resource` VALUES ('107', '5', '28');
INSERT INTO `t_group_resource` VALUES ('108', '5', '29');
INSERT INTO `t_group_resource` VALUES ('109', '5', '31');
INSERT INTO `t_group_resource` VALUES ('110', '5', '32');
INSERT INTO `t_group_resource` VALUES ('111', '5', '41');
INSERT INTO `t_group_resource` VALUES ('112', '5', '42');
INSERT INTO `t_group_resource` VALUES ('113', '5', '51');
INSERT INTO `t_group_resource` VALUES ('114', '5', '52');
INSERT INTO `t_group_resource` VALUES ('115', '6', '11');
INSERT INTO `t_group_resource` VALUES ('116', '6', '12');
INSERT INTO `t_group_resource` VALUES ('117', '6', '13');
INSERT INTO `t_group_resource` VALUES ('118', '6', '14');
INSERT INTO `t_group_resource` VALUES ('119', '6', '15');
INSERT INTO `t_group_resource` VALUES ('120', '6', '16');
INSERT INTO `t_group_resource` VALUES ('121', '6', '17');
INSERT INTO `t_group_resource` VALUES ('122', '6', '21');
INSERT INTO `t_group_resource` VALUES ('123', '6', '22');
INSERT INTO `t_group_resource` VALUES ('124', '6', '23');
INSERT INTO `t_group_resource` VALUES ('125', '6', '24');
INSERT INTO `t_group_resource` VALUES ('126', '6', '25');
INSERT INTO `t_group_resource` VALUES ('127', '6', '26');
INSERT INTO `t_group_resource` VALUES ('128', '6', '27');
INSERT INTO `t_group_resource` VALUES ('129', '6', '28');
INSERT INTO `t_group_resource` VALUES ('130', '6', '29');
INSERT INTO `t_group_resource` VALUES ('131', '6', '31');
INSERT INTO `t_group_resource` VALUES ('132', '6', '32');
INSERT INTO `t_group_resource` VALUES ('133', '6', '41');
INSERT INTO `t_group_resource` VALUES ('134', '6', '42');
INSERT INTO `t_group_resource` VALUES ('135', '6', '51');
INSERT INTO `t_group_resource` VALUES ('136', '6', '52');
INSERT INTO `t_group_resource` VALUES ('137', '7', '61');
INSERT INTO `t_group_resource` VALUES ('138', '7', '62');
INSERT INTO `t_group_resource` VALUES ('139', '7', '63');
INSERT INTO `t_group_resource` VALUES ('140', '7', '64');
INSERT INTO `t_group_resource` VALUES ('141', '7', '65');
INSERT INTO `t_group_resource` VALUES ('142', '7', '71');
INSERT INTO `t_group_resource` VALUES ('143', '7', '72');
INSERT INTO `t_group_resource` VALUES ('144', '7', '73');
INSERT INTO `t_group_resource` VALUES ('145', '7', '74');
INSERT INTO `t_group_resource` VALUES ('146', '7', '75');
INSERT INTO `t_group_resource` VALUES ('147', '7', '81');
INSERT INTO `t_group_resource` VALUES ('148', '7', '91');
INSERT INTO `t_group_resource` VALUES ('149', '7', '92');
INSERT INTO `t_group_resource` VALUES ('150', '7', '93');
INSERT INTO `t_group_resource` VALUES ('151', '7', '100');
INSERT INTO `t_group_resource` VALUES ('152', '7', '101');
INSERT INTO `t_group_resource` VALUES ('153', '1', '103');
INSERT INTO `t_group_resource` VALUES ('154', '1', '104');
INSERT INTO `t_group_resource` VALUES ('155', '1', '105');
INSERT INTO `t_group_resource` VALUES ('156', '1', '106');
INSERT INTO `t_group_resource` VALUES ('157', '1', '107');
INSERT INTO `t_group_resource` VALUES ('163', '3', '103');
INSERT INTO `t_group_resource` VALUES ('164', '3', '104');
INSERT INTO `t_group_resource` VALUES ('165', '3', '105');
INSERT INTO `t_group_resource` VALUES ('166', '3', '106');
INSERT INTO `t_group_resource` VALUES ('167', '3', '107');
INSERT INTO `t_group_resource` VALUES ('168', '4', '103');
INSERT INTO `t_group_resource` VALUES ('169', '4', '104');
INSERT INTO `t_group_resource` VALUES ('170', '4', '105');
INSERT INTO `t_group_resource` VALUES ('171', '4', '106');
INSERT INTO `t_group_resource` VALUES ('172', '4', '107');
INSERT INTO `t_group_resource` VALUES ('173', '5', '103');
INSERT INTO `t_group_resource` VALUES ('174', '5', '104');
INSERT INTO `t_group_resource` VALUES ('175', '5', '105');
INSERT INTO `t_group_resource` VALUES ('176', '5', '106');
INSERT INTO `t_group_resource` VALUES ('177', '5', '107');
INSERT INTO `t_group_resource` VALUES ('178', '6', '103');
INSERT INTO `t_group_resource` VALUES ('179', '6', '104');
INSERT INTO `t_group_resource` VALUES ('180', '6', '105');
INSERT INTO `t_group_resource` VALUES ('181', '6', '106');
INSERT INTO `t_group_resource` VALUES ('182', '6', '107');
INSERT INTO `t_group_resource` VALUES ('183', '7', '103');
INSERT INTO `t_group_resource` VALUES ('184', '7', '102');
INSERT INTO `t_group_resource` VALUES ('185', '7', '108');
INSERT INTO `t_group_resource` VALUES ('186', '7', '109');
INSERT INTO `t_group_resource` VALUES ('187', '1', '12');
INSERT INTO `t_group_resource` VALUES ('188', '1', '21');
INSERT INTO `t_group_resource` VALUES ('190', '7', '110');
INSERT INTO `t_group_resource` VALUES ('191', '7', '111');
INSERT INTO `t_group_resource` VALUES ('222', '2', '11');
INSERT INTO `t_group_resource` VALUES ('223', '2', '12');
INSERT INTO `t_group_resource` VALUES ('224', '2', '13');
INSERT INTO `t_group_resource` VALUES ('225', '2', '14');
INSERT INTO `t_group_resource` VALUES ('226', '2', '15');
INSERT INTO `t_group_resource` VALUES ('227', '2', '16');
INSERT INTO `t_group_resource` VALUES ('228', '2', '17');
INSERT INTO `t_group_resource` VALUES ('229', '2', '21');
INSERT INTO `t_group_resource` VALUES ('230', '2', '22');
INSERT INTO `t_group_resource` VALUES ('231', '2', '23');
INSERT INTO `t_group_resource` VALUES ('232', '2', '24');
INSERT INTO `t_group_resource` VALUES ('233', '2', '25');
INSERT INTO `t_group_resource` VALUES ('234', '2', '26');
INSERT INTO `t_group_resource` VALUES ('235', '2', '27');
INSERT INTO `t_group_resource` VALUES ('236', '2', '28');
INSERT INTO `t_group_resource` VALUES ('237', '2', '29');
INSERT INTO `t_group_resource` VALUES ('238', '2', '31');
INSERT INTO `t_group_resource` VALUES ('239', '2', '32');
INSERT INTO `t_group_resource` VALUES ('240', '2', '41');
INSERT INTO `t_group_resource` VALUES ('241', '2', '42');
INSERT INTO `t_group_resource` VALUES ('242', '2', '51');
INSERT INTO `t_group_resource` VALUES ('243', '2', '52');
INSERT INTO `t_group_resource` VALUES ('244', '2', '103');
INSERT INTO `t_group_resource` VALUES ('245', '2', '104');
INSERT INTO `t_group_resource` VALUES ('246', '2', '105');
INSERT INTO `t_group_resource` VALUES ('247', '2', '106');
INSERT INTO `t_group_resource` VALUES ('248', '2', '107');

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `parent_ids` varchar(100) DEFAULT NULL,
  `permission` varchar(100) DEFAULT NULL,
  `available` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_sys_resource_parent_id` (`parent_id`),
  KEY `idx_sys_resource_parent_ids` (`parent_ids`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
INSERT INTO `t_resource` VALUES ('1', '资源', 'menu', '#', '0', '0/', '#', '1');
INSERT INTO `t_resource` VALUES ('11', '申请查看', 'menu', 'processAction/process/toListApply', '1', '0/1/', 'user:process:runningVacation', '1');
INSERT INTO `t_resource` VALUES ('12', '查看请假申请', 'button', null, '11', '0/1/11/', 'user:vacation:list', '1');
INSERT INTO `t_resource` VALUES ('13', '请假详情', 'button', null, '11', '0/1/11/', '*:vacation:details', '1');
INSERT INTO `t_resource` VALUES ('14', '查看薪资调整申请', 'button', null, '11', '0/1/11/', 'user:salary:list', '1');
INSERT INTO `t_resource` VALUES ('15', '薪资调整详情', 'button', null, '11', '0/1/11/', 'user:salary:details', '1');
INSERT INTO `t_resource` VALUES ('16', '查看报销申请', 'button', null, '11', '0/1/11/', 'user:expense:list', '1');
INSERT INTO `t_resource` VALUES ('17', '报销详情', 'button', null, '11', '0/1/11/', 'user:expense:details', '1');
INSERT INTO `t_resource` VALUES ('21', '我的任务', 'menu', 'processAction/userTaskList', '1', '0/1/', 'user:*', '1');
INSERT INTO `t_resource` VALUES ('22', '待办的任务', 'button', null, '21', '0/1/21/', 'user:task:todoTask', '1');
INSERT INTO `t_resource` VALUES ('23', '受理的任务', 'button', null, '21', '0/1/21/', 'user:task:doTask', '1');
INSERT INTO `t_resource` VALUES ('24', '签收', 'button', '', '21', '0/1/21/', 'user:task:claim', '1');
INSERT INTO `t_resource` VALUES ('25', '办理请假申请', 'button', '', '21', '0/1/21/', 'user:vacation:toApproval', '1');
INSERT INTO `t_resource` VALUES ('26', '办理薪资调整', 'button', '', '21', '0/1/21/', 'user:salary:toApproval', '1');
INSERT INTO `t_resource` VALUES ('27', '办理报销申请', 'button', '', '21', '0/1/21/', 'user:expense:toApproval', '1');
INSERT INTO `t_resource` VALUES ('28', '完成申请', 'button', null, '21', '0/1/21/', 'user:*:complate', '1');
INSERT INTO `t_resource` VALUES ('29', '调整申请', 'button', null, '21', '0/1/21/', 'user:*:modify', '1');
INSERT INTO `t_resource` VALUES ('31', '请假', 'menu', 'vacationAction/toAdd', '1', '0/1/', 'user:vacation:*', '1');
INSERT INTO `t_resource` VALUES ('32', '添加请假申请', 'button', null, '31', '0/1/31/', '*:vacation:doAdd', '1');
INSERT INTO `t_resource` VALUES ('41', '报销', 'menu', 'expenseAction/toAdd', '1', '0/1/', 'user:expense:*', '1');
INSERT INTO `t_resource` VALUES ('42', '添加报销申请', 'button', null, '41', '0/1/41/', 'user:expense:doAdd', '1');
INSERT INTO `t_resource` VALUES ('51', '薪资调整', 'menu', 'salaryAction/toAdd', '1', '0/1/', 'user:salary:*', '1');
INSERT INTO `t_resource` VALUES ('52', '添加薪资调整', 'button', null, '51', '0/1/51/', 'user:salary:doAdd', '1');
INSERT INTO `t_resource` VALUES ('61', '用户组管理', 'menu', 'groupAction/permissionAssignment', '1', '0/1/', 'admin:*', '1');
INSERT INTO `t_resource` VALUES ('62', 'to添加用户组', 'button', null, '61', '0/1/61', 'admin:group:toAdd', '1');
INSERT INTO `t_resource` VALUES ('63', '添加用户组', 'button', null, '61', '0/1/61/', 'admin:group:doAdd', '1');
INSERT INTO `t_resource` VALUES ('64', 'to修改用户组', 'button', null, '61', '0/1/61/', 'admin:group:toUpdate', '1');
INSERT INTO `t_resource` VALUES ('65', '修改用户组', 'button', null, '61', '0/1/61/', 'admin:group:doUpdate', '1');
INSERT INTO `t_resource` VALUES ('71', '用户管理', 'menu', 'userAction/userDatagrid', '1', '0/1/', 'admin:*', '1');
INSERT INTO `t_resource` VALUES ('72', 'to添加用户', 'button', null, '71', '0/1/71/', 'admin:user:toAdd', '1');
INSERT INTO `t_resource` VALUES ('73', '添加用户', 'button', null, '71', '0/1/71/', 'admin:user:doAdd', '1');
INSERT INTO `t_resource` VALUES ('74', 'to修改用户', 'button', null, '71', '0/1/71/', 'admin:user:toUpdate', '1');
INSERT INTO `t_resource` VALUES ('75', '修改用户', 'button', null, '71', '0/1/71/', 'admin:user:doUpdate', '1');
INSERT INTO `t_resource` VALUES ('81', '流程定义管理', 'menu', 'processAction/process/toListProcessInstance', '1', '0/1/', 'admin:process:*', '1');
INSERT INTO `t_resource` VALUES ('91', '流程实例管理', 'menu', 'processAction/process/toListProcessManager', '1', '0/1/', 'admin:process:*', '1');
INSERT INTO `t_resource` VALUES ('92', '挂起流程', 'button', null, '91', '0/1/91/', 'admin:process:suspend', '1');
INSERT INTO `t_resource` VALUES ('93', '激活流程', 'buton', null, '91', '0/1/91/', 'admin:process:active', '1');
INSERT INTO `t_resource` VALUES ('100', '在线用户', 'menu', 'userAction/toListOnlineUser', '1', '0/1/', 'admin:*', '1');
INSERT INTO `t_resource` VALUES ('101', '踢出用户', 'button', null, '100', '0/1/100/', 'admin:session:forceLogout', '1');
INSERT INTO `t_resource` VALUES ('102', '流程设计模型', 'menu', 'modelAction/toListModel', '1', '0/1/', 'admin:process:*', '1');
INSERT INTO `t_resource` VALUES ('103', '流程跟踪', 'button', null, '81', '0/1/81/', 'user:process:trace', '1');
INSERT INTO `t_resource` VALUES ('104', '已结束的流程', 'button', null, '81', '0/1/81/', 'user:process:finished', '1');
INSERT INTO `t_resource` VALUES ('105', '请假申请查看', 'button', null, '81', '0/1/81/', 'user:process:runningVacation', '1');
INSERT INTO `t_resource` VALUES ('106', '报销申请查看', 'button', null, '81', '0/1/81/', 'user:process:runningExpense', '1');
INSERT INTO `t_resource` VALUES ('107', '薪资申请查看', 'button', null, '81', '0/1/81/', 'user:process:runningSalary', '1');
INSERT INTO `t_resource` VALUES ('108', '同步用户', 'button', null, '71', '0/1/71/', 'admin:user:syncUser', '1');
INSERT INTO `t_resource` VALUES ('109', '审批设定', 'menu', 'permissionAction/toListBpmn', '1', '0/1/', 'admin:*', '1');
INSERT INTO `t_resource` VALUES ('110', '初始化', 'button', null, '109', '0/1/109/', 'admin:permission:init', '1');
INSERT INTO `t_resource` VALUES ('111', '资源管理', 'menu', 'resourceAction/toList', '1', '0/1/', 'admin:*', '1');
INSERT INTO `t_resource` VALUES ('112', 'test', 'menu', '123123', '11', '123123', '123123', '1');

-- ----------------------------
-- Table structure for t_salary
-- ----------------------------
DROP TABLE IF EXISTS `t_salary`;
CREATE TABLE `t_salary` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BASE_MONEY` decimal(19,2) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_salary
-- ----------------------------
INSERT INTO `t_salary` VALUES ('1', '500.00', '12');
INSERT INTO `t_salary` VALUES ('2', '20000.00', '13');

-- ----------------------------
-- Table structure for t_salary_adjust
-- ----------------------------
DROP TABLE IF EXISTS `t_salary_adjust`;
CREATE TABLE `t_salary_adjust` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ADJUST_MONEY` decimal(19,2) DEFAULT NULL,
  `APPLY_DATE` date DEFAULT NULL,
  `DSCP` varchar(255) DEFAULT NULL,
  `PROC_INST_ID` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_salary_adjust
-- ----------------------------
INSERT INTO `t_salary_adjust` VALUES ('24', '10000.00', '2015-07-14', '薪资调整', '262526', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('25', '18000.00', '2015-07-15', '撤销看看评论', '270026', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('26', '20000.00', '2015-07-15', '删除评论', '272501', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('27', '20500.00', '2015-07-15', 'comment', '275005', '13', 'PENDING');
INSERT INTO `t_salary_adjust` VALUES ('28', '26000.00', '2015-07-16', '1111', '280005', '13', 'APPROVAL_FAILED');
INSERT INTO `t_salary_adjust` VALUES ('29', '25000.00', '2015-07-15', '111', '285001', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('30', '30000.00', '2015-07-16', '123', '295029', '13', 'APPROVAL_FAILED');
INSERT INTO `t_salary_adjust` VALUES ('31', '31000.00', '2015-07-16', '232', '297501', '13', 'APPROVAL_FAILED');
INSERT INTO `t_salary_adjust` VALUES ('32', '123456.00', '2015-07-16', '123', '297539', '13', 'PENDING');
INSERT INTO `t_salary_adjust` VALUES ('33', '20000.00', '2015-07-16', '123', '300017', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('34', '20000.00', '2015-07-17', '123123', null, '16', 'PENDING');
INSERT INTO `t_salary_adjust` VALUES ('35', '22000.00', '2015-07-17', '112233', '305001', '13', 'APPROVAL_SUCCESS');
INSERT INTO `t_salary_adjust` VALUES ('36', '22000.00', '2015-07-20', '888', '310001', '13', 'PENDING');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(255) DEFAULT NULL,
  `USER_PWD` varchar(255) DEFAULT NULL,
  `REG_DATE` date DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT NULL,
  `LOCKED` int(11) DEFAULT NULL,
  `USER_SALT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `FK_n76p61r4jd9duyf1c2njxx041` (`GROUP_ID`),
  CONSTRAINT `FK_n76p61r4jd9duyf1c2njxx041` FOREIGN KEY (`GROUP_ID`) REFERENCES `t_group` (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('4', '老板', '16a995df59c545d7b5a1bb6c467335af', '2015-02-07', '1', '0', 'df619f4b54b71c4eb3ab46a3e77bb0df');
INSERT INTO `t_user` VALUES ('5', '财务', '5663fe5a302c2cf4c09da32e71c576d2', '2015-02-07', '4', '0', '2db9ce54137dea81dcee95d43256c487');
INSERT INTO `t_user` VALUES ('6', '员工2', '5c1ccd5c0311bf59b3d01a8143eac7a6', null, '3', '0', '06b3e93953b1c416db97aa95626b3919');
INSERT INTO `t_user` VALUES ('13', 'zhao', 'cde49e4e9e04c67929164327cd406af7', '2015-01-12', '3', '0', 'fc0885339b523c7fe9fadaf05cbe5269');
INSERT INTO `t_user` VALUES ('14', 'admin', 'c4d6f647513261f13111505facb02fa5', '2015-01-19', '7', '0', '0a8135fc704ad1271973423804320195');
INSERT INTO `t_user` VALUES ('15', '经理', '72b0d9b72bd552983083bebc4b9f9ef4', '2015-02-07', '6', '0', 'b94b50d150fee37de6e9c389edb30397');
INSERT INTO `t_user` VALUES ('16', '总监', '0067847e904e1295a23015cad660382c', '2015-02-07', '2', '0', '52e5a800953f810af2a3c1367c207e74');
INSERT INTO `t_user` VALUES ('17', '人事', '377ba153f45e6d9ca243a62a8a055a97', '2015-02-07', '5', '0', 'ebabc4618cedbe831f848fef43038082');
INSERT INTO `t_user` VALUES ('18', '经理2', '0273e7ce534e5e24e572c8b3b2e613a5', '2015-04-27', '6', '0', '899865f3edff41b3c1b420fd3e354426');
INSERT INTO `t_user` VALUES ('19', '总监2', '06c8ce0fbf0693d20a873366c2062322', '2015-04-27', '2', '0', 'b0abe376c5429e0c07c65d6ea16a4142');

-- ----------------------------
-- Table structure for t_user_task
-- ----------------------------
DROP TABLE IF EXISTS `t_user_task`;
CREATE TABLE `t_user_task` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROC_DEF_KEY` varchar(255) DEFAULT NULL,
  `PROC_DEF_NAME` varchar(255) DEFAULT NULL,
  `TASK_DEF_KEY` varchar(255) DEFAULT NULL,
  `TASK_NAME` varchar(255) DEFAULT NULL,
  `TASK_TYPE` varchar(255) DEFAULT NULL,
  `CANDIDATE_NAME` varchar(255) DEFAULT NULL,
  `CANDIDATE_IDS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_task
-- ----------------------------
INSERT INTO `t_user_task` VALUES ('31', 'com.zml.oa.vacation', '请假流程', 'managerAudit', '经理审批', 'assignee', '经理', '15');
INSERT INTO `t_user_task` VALUES ('32', 'com.zml.oa.vacation', '请假流程', 'directorAudit', '总监审批', 'candidateUser', '总监,总监2', '16,19');
INSERT INTO `t_user_task` VALUES ('33', 'com.zml.oa.vacation', '请假流程', 'modifyApply', '调整申请', 'modify', '任务发起人', '0');
INSERT INTO `t_user_task` VALUES ('34', 'com.zml.oa.vacation', '请假流程', 'hrAudit', '人力资源审批', 'candidateGroup', '人事组', '5');
INSERT INTO `t_user_task` VALUES ('35', 'com.zml.oa.salary', '薪资调整', 'directorAudit', '总监审批', 'candidateGroup', '总监组', '2');
INSERT INTO `t_user_task` VALUES ('36', 'com.zml.oa.salary', '薪资调整', 'hrAudit', '人事审批', 'assignee', '人事', '17');
INSERT INTO `t_user_task` VALUES ('37', 'com.zml.oa.salary', '薪资调整', 'bossAudit', '老板审批', 'assignee', '老板', '4');
INSERT INTO `t_user_task` VALUES ('38', 'com.zml.oa.salary', '薪资调整', 'modifyApply', '调整申请', 'modify', '任务发起人', '0');
INSERT INTO `t_user_task` VALUES ('39', 'com.zml.oa.expense', '报销流程', 'financeAudit', '财务审批', 'assignee', '财务', '5');
INSERT INTO `t_user_task` VALUES ('40', 'com.zml.oa.expense', '报销流程', 'payForMoney', '现金支付', null, '', '');

-- ----------------------------
-- Table structure for t_vacation
-- ----------------------------
DROP TABLE IF EXISTS `t_vacation`;
CREATE TABLE `t_vacation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APPLY_DATE` date DEFAULT NULL,
  `BEGIN_DATE` date DEFAULT NULL,
  `WORK_DAYS` int(11) DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `PROC_INST_ID` varchar(255) DEFAULT NULL,
  `REASON` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `VAC_TYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_vacation
-- ----------------------------
INSERT INTO `t_vacation` VALUES ('1', '2015-07-16', '2015-07-16', '6', '2015-07-23', '290001', '123123', 'APPROVAL_SUCCESS', '13', '1');

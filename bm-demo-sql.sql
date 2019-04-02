/*
Navicat MySQL Data Transfer

Source Server         : 192.168.240.20_DEV
Source Server Version : 50627
Source Host           : 192.168.240.20:3306
Source Database       : vans

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2018-12-27 14:57:06
*/

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(8) DEFAULT NULL COMMENT '父ID',
  `parent_ids` varchar(2000) DEFAULT NULL COMMENT '树ID',
  `text` varchar(100) NOT NULL COMMENT '菜单名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `url` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target_type` varchar(32) DEFAULT NULL COMMENT '页面打开方式',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` tinyint(1) DEFAULT '1' COMMENT '是否显示\n1：显示\n0：隐藏',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记\n1：删除\n0：未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', null, '', '系统设置', '9', '', '', 'fa fa-cog', '1', '', '测试', '2018-01-24 12:03:44', '2018-09-06 10:49:29', '0');
INSERT INTO `sys_menu` VALUES ('2', '1', '1,', '字典管理', '1', '/#/dict', 'iframe-tab', 'fa fa-navicon', '1', 'sys:dict:view', null, '2018-01-24 14:14:09', '2018-01-24 14:14:11', '0');
INSERT INTO `sys_menu` VALUES ('3', '2', '1,2,', '修改', '1', null, null, null, '0', 'sys:dict:edit', null, '2018-01-24 14:16:02', '2018-01-24 14:16:04', '0');
INSERT INTO `sys_menu` VALUES ('4', '1', '1,', '菜单管理', '2', '/#/menu', 'iframe-tab', 'fa fa-navicon', '1', 'sys:menu:view', null, '2018-01-24 15:12:11', '2018-01-24 15:12:14', '0');
INSERT INTO `sys_menu` VALUES ('5', '4', '1,4,', '修改', '1', null, null, null, '0', 'sys:menu:edit', null, '2018-01-24 15:13:05', '2018-01-24 15:13:08', '0');
INSERT INTO `sys_menu` VALUES ('6', '1', '1,', '角色管理', '3', '/#/role', 'iframe-tab', 'fa fa-navicon', '1', 'sys:role:view', '角色管理', '2018-01-24 15:50:52', '2018-01-24 15:50:52', '0');
INSERT INTO `sys_menu` VALUES ('7', '6', '1,6,', '编辑', '1', null, 'iframe-tab', null, '0', 'sys:role:edit', '编辑角色', '2018-01-24 15:51:22', '2018-01-24 15:51:22', '0');
INSERT INTO `sys_menu` VALUES ('8', '1', '1,', '用户管理', '4', '/#/user', 'iframe-tab', 'fa fa-navicon', '1', 'sys:user:list', '用户管理', '2018-01-24 16:27:10', '2018-01-24 16:27:50', '0');
INSERT INTO `sys_menu` VALUES ('9', '8', '1,8,', '查看', '1', null, 'iframe-tab', null, '0', 'sys:user:view', null, '2018-01-24 16:28:14', '2018-01-24 16:28:14', '0');
INSERT INTO `sys_menu` VALUES ('10', '8', '1,8,', '编辑', '2', null, 'iframe-tab', null, '0', 'sys:user:edit', null, '2018-01-24 16:28:38', '2018-01-24 16:28:38', '0');
INSERT INTO `sys_menu` VALUES ('11', '8', '1,8,', '授权', '3', null, 'iframe-tab', null, '0', 'sys:user:auth', null, '2018-01-24 16:29:07', '2018-01-24 16:30:07', '0');
INSERT INTO `sys_menu` VALUES ('12', null, null, '定时任务管理', '2', '', 'iframe-tab', 'fa fa-cog', '1', '', '定时任务管理', '2018-01-25 13:38:27', '2018-01-25 13:38:27', '1');
INSERT INTO `sys_menu` VALUES ('13', '12', '12,', '定时任务', '1', '/#/timer', 'iframe-tab', 'fa fa-navicon', '1', '', null, '2018-01-25 13:39:40', '2018-01-25 13:39:40', '1');
INSERT INTO `sys_menu` VALUES ('14', '1', '1,', '代码生成器', '1', '/#/generator', 'iframe-tab', 'fa fa-navicon', '1', 'generator:generator:view', '后台代码生成器', '2018-07-04 11:58:22', '2018-07-04 11:58:22', '0');
INSERT INTO `sys_menu` VALUES ('15', '14', '1,14,', '代码生成', '1', null, 'iframe-tab', null, '0', 'generator:generator:code', '代码生成操作', '2018-07-04 13:12:13', '2018-07-04 13:12:13', '0');
INSERT INTO `sys_menu` VALUES ('16', '1', '1,', 'ueditor示例', '1', '/#/demo', 'iframe-tab', 'fa fa-navicon', '1', '', 'ueditor示例', '2018-07-04 13:18:55', '2018-07-06 15:11:23', '0');

-- ----------------------------
-- Table structure for sys_dict_entry
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_entry`;
CREATE TABLE `sys_dict_entry` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `dicttype_id` varchar(50) NOT NULL COMMENT '业务字典子选项',
  `dict_id` varchar(100) NOT NULL COMMENT '字典编码',
  `dict_name` varchar(400) DEFAULT NULL COMMENT '字典名称',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（1使用中/0已废弃）',
  `sort` smallint(4) DEFAULT NULL COMMENT '排序编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记\n1：删除\n0：未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_dict_entry
-- ----------------------------
INSERT INTO `sys_dict_entry` VALUES ('1', 'SYS_DICT_SEX', 'male', '男', '1', '1', '2018-01-24 14:40:45', '2018-01-24 14:40:45', '0');
INSERT INTO `sys_dict_entry` VALUES ('2', 'SYS_DICT_SEX', 'female', '女', '1', '2', '2018-01-24 14:40:57', '2018-01-24 14:40:57', '0');
INSERT INTO `sys_dict_entry` VALUES ('3', 'SYS_DICT_SEX', 'unknown', '未知', '1', '3', '2018-01-24 14:41:28', '2018-01-24 14:41:28', '0');


-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `dicttype_id` varchar(100) NOT NULL COMMENT '业务字典编码',
  `dicttype_name` varchar(100) DEFAULT NULL COMMENT '业务字典名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记\n1：删除\n0：未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1', 'SYS_DICT_SEX', '性别', '2018-01-24 14:25:56', '2018-01-24 14:25:56', '0');


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(36) NOT NULL COMMENT '角色代码',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否可用\n1：可用\n0：停用',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记\n1：删除\n0：未删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'ROLE_ADMIN', '超级管理员', '1', '超级管理员', '2018-01-24 11:31:01', '2018-07-30 12:23:54', '0');
INSERT INTO `sys_role` VALUES ('2', 'role_test', '测试体验者', '1', '无系统设置权限', '2018-08-01 13:09:44', '2018-08-01 13:09:44', '0');


-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(8) unsigned NOT NULL COMMENT '角色ID',
  `menu_id` int(8) unsigned NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '6');
INSERT INTO `sys_role_menu` VALUES ('1', '7');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '9');
INSERT INTO `sys_role_menu` VALUES ('1', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '11');
INSERT INTO `sys_role_menu` VALUES ('1', '14');
INSERT INTO `sys_role_menu` VALUES ('1', '15');
INSERT INTO `sys_role_menu` VALUES ('1', '16');


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(36) NOT NULL COMMENT '姓名',
  `email` varchar(50) DEFAULT NULL COMMENT '邮件',
  `phone` varchar(36) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(36) DEFAULT NULL COMMENT '手机',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否可用\n1：可用\n0：停用',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记\n1：删除\n0：未删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$08$UIbl948v1vaFLzwr3Hea7uJECTdYsEA8gkxWxNgBLBVXbIG1ODyLO', '超级管理员', '514471352@qq.com', null, null, '1', '超级管理员', '2018-01-24 10:19:49', '2018-01-24 10:19:51', '0');
INSERT INTO `sys_user` VALUES ('2', 'huangpeishan', '524c8ab9741987f5d233532fe5b2b4ffd609f740e0da3dfba37116f9ae4a51c3e4ae4d777dcc5bdf', '黄佩珊', '', null, '18665712882', '1', '', '2018-07-17 11:24:54', '2018-08-01 15:09:20', '1');
INSERT INTO `sys_user` VALUES ('7', '80438036', '$2a$08$zuYcS1BV35spaIJhdYmvKe/DnDdBM3xphQ7GaUUzWUVTuLAVM/i3e', '李国庆', '', null, '18665052861', '1', '', '2018-08-01 15:14:47', '2018-08-10 11:25:52', '0');
INSERT INTO `sys_user` VALUES ('24', '80512165', '$2a$08$NPt1LKPZKyHZ7zBOtD4MPO.03AXd9OaGFEGMvO1lBrn0H.BrUuU7G', '李拉拉', '', null, '13500000000', '1', '', '2018-08-01 16:38:31', '2018-08-01 16:38:31', '0');
INSERT INTO `sys_user` VALUES ('25', '80488098', '$2a$08$UPjeLW5ThruIUQq9ak.dnupXFnK8FmWg.y3TFa18YFbaJQNmXkhm2', '星星', '', null, '15800000000', '1', '', '2018-08-01 16:42:57', '2018-08-01 16:42:57', '0');
INSERT INTO `sys_user` VALUES ('26', '110', '$2a$08$.azd6KySeRnfHku8/i.daucKat0DikoaN617oXUm9E1/a/5uuw8cq', '1', '', null, '1', '1', '', '2018-08-15 15:18:55', '2018-08-15 15:18:55', '0');
INSERT INTO `sys_user` VALUES ('27', '80468295', '$2a$08$UIbl948v1vaFLzwr3Hea7uhE4HRvdA/1HiPpoXnJ96VEyYU4cpDAW', '123', '1212@123', null, null, '1', null, null, null, '0');


-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(8) unsigned NOT NULL COMMENT '用户ID',
  `role_id` int(8) unsigned NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('7', '1');
INSERT INTO `sys_user_role` VALUES ('22', '2');
INSERT INTO `sys_user_role` VALUES ('23', '1');
INSERT INTO `sys_user_role` VALUES ('23', '2');
INSERT INTO `sys_user_role` VALUES ('24', '1');
INSERT INTO `sys_user_role` VALUES ('24', '2');
INSERT INTO `sys_user_role` VALUES ('25', '2');
INSERT INTO `sys_user_role` VALUES ('26', '1');
INSERT INTO `sys_user_role` VALUES ('26', '2');
INSERT INTO `sys_user_role` VALUES ('27', '1');


-- ----------------------------
-- Table structure for m_banner
-- ----------------------------
DROP TABLE IF EXISTS `m_banner`;
CREATE TABLE `m_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) DEFAULT NULL COMMENT '编码',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `title` varchar(256) DEFAULT NULL COMMENT '标题',
  `url_type` varchar(32) DEFAULT NULL COMMENT '跳转类型',
  `pic_url` varchar(512) DEFAULT NULL COMMENT '展示图URL',
  `web_url` varchar(256) DEFAULT NULL COMMENT '链接页面URL',
  `web_title` varchar(256) DEFAULT NULL COMMENT '链接页面标题',
  `web_desc` varchar(512) DEFAULT NULL COMMENT '链接页面描述',
  `url_code` varchar(32) DEFAULT NULL COMMENT 'url指向页面Code（内部页面）',
  `url_param` varchar(256) DEFAULT NULL COMMENT '内部跳转参数',
  `status` varchar(1) DEFAULT NULL COMMENT '是否有效（1有效，2关闭）',
  `start_time` datetime DEFAULT NULL COMMENT '生效开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '生效结束时间',
  `position` int(11) DEFAULT NULL COMMENT '排序优先级',
  `create_code` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `op_code` varchar(32) DEFAULT NULL COMMENT '操作人',
  `op_name` varchar(64) DEFAULT NULL COMMENT '操作人编号',
  `op_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='banner表';

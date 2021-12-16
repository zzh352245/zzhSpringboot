
DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `permission_code` varchar(32) DEFAULT NULL COMMENT '权限code',
  `permission_name` varchar(32) DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

/*Data for the table `sys_permission` */

insert  into `sys_permission`(`id`,`permission_code`,`permission_name`) values (1,'create_user','创建用户');
insert  into `sys_permission`(`id`,`permission_code`,`permission_name`) values (2,'query_user','查看用户');
insert  into `sys_permission`(`id`,`permission_code`,`permission_name`) values (3,'delete_user','删除用户');
insert  into `sys_permission`(`id`,`permission_code`,`permission_name`) values (4,'modify_user','修改用户');
insert  into `sys_permission`(`id`,`permission_code`,`permission_name`) values (5,'test','测试');

/*Table structure for table `sys_request_path` */

DROP TABLE IF EXISTS `sys_request_path`;

CREATE TABLE `sys_request_path` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `url` varchar(64) NOT NULL COMMENT '请求路径',
  `description` varchar(128) DEFAULT NULL COMMENT '路径描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='请求路径';

/*Data for the table `sys_request_path` */

insert  into `sys_request_path`(`id`,`url`,`description`) values (1,'/sys-user/getUser','查询用户');
insert  into `sys_request_path`(`id`,`url`,`description`) values (2,'/sys-user/test','测试');

/*Table structure for table `sys_request_path_permission_relation` */

DROP TABLE IF EXISTS `sys_request_path_permission_relation`;

CREATE TABLE `sys_request_path_permission_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `url_id` int DEFAULT NULL COMMENT '请求路径id',
  `permission_id` int DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='路径权限关联表';

/*Data for the table `sys_request_path_permission_relation` */

insert  into `sys_request_path_permission_relation`(`id`,`url_id`,`permission_id`) values (1,1,2);
insert  into `sys_request_path_permission_relation`(`id`,`url_id`,`permission_id`) values (2,2,5);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_code` varchar(16) NOT NULL COMMENT '角色编码',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名',
  `role_description` varchar(64) DEFAULT NULL COMMENT '角色说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`role_code`,`role_name`,`role_description`) values (1,'admin','管理员','管理员，拥有所有权限');
insert  into `sys_role`(`id`,`role_code`,`role_name`,`role_description`) values (2,'user','普通用户','普通用户，拥有部分权限');

/*Table structure for table `sys_role_permission_relation` */

DROP TABLE IF EXISTS `sys_role_permission_relation`;

CREATE TABLE `sys_role_permission_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-权限关联关系表';

/*Data for the table `sys_role_permission_relation` */

insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (1,1,1);
insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (2,1,2);
insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (3,1,3);
insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (4,1,4);
insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (5,2,1);
insert  into `sys_role_permission_relation`(`id`,`role_id`,`permission_id`) values (6,2,2);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_code` varchar(32) NOT NULL COMMENT '用户编码',
  `user_name` varchar(16) NOT NULL COMMENT '16位账号',
  `pass_word` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后密码',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '盐值',
  `nick_name` varchar(16) NOT NULL COMMENT '昵称',
  `phone_number` varchar(16) NOT NULL COMMENT '手机号',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别：0-未知 1-男 2-女',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `reg_ip` bigint NOT NULL COMMENT '注册IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_ip` bigint NOT NULL COMMENT '最后一次登录IP',
  `last_login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `identity` tinyint(1) NOT NULL DEFAULT '1' COMMENT '身份：1-普通用户 9-管理员 99-超管',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_code` (`user_code`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账号管理';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`user_code`,`user_name`,`pass_word`,`salt`,`nick_name`,`phone_number`,`gender`,`email`,`reg_ip`,`create_time`,`last_login_ip`,`last_login_time`,`identity`) values (1,'123456','admin','$2a$10$bmBcFNJRINJfgcpZlrw/ruysDAt2B5f2qZfCPeKD7RoS9qO3tyRs.',NULL,'超管1','15811111111',1,NULL,2130706433,'2021-12-02 15:02:43',2130706433,'2021-12-04 11:03:42',1);
insert  into `sys_user`(`id`,`user_code`,`user_name`,`pass_word`,`salt`,`nick_name`,`phone_number`,`gender`,`email`,`reg_ip`,`create_time`,`last_login_ip`,`last_login_time`,`identity`) values (2,'234567','aaa','$2a$10$6KqJ0TiaBDoo4gqpwhA1rOJUMPn9vbOI6kBU9W2pq2rpAubJw9BnO',NULL,'普通用户','15822222222',0,NULL,2130706433,'2021-12-02 15:04:10',2130706433,'2021-12-02 15:04:10',1);

/*Table structure for table `sys_user_role_relation` */

DROP TABLE IF EXISTS `sys_user_role_relation`;

CREATE TABLE `sys_user_role_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联关系表';

/*Data for the table `sys_user_role_relation` */

insert  into `sys_user_role_relation`(`id`,`user_id`,`role_id`) values (1,1,1);
insert  into `sys_user_role_relation`(`id`,`user_id`,`role_id`) values (2,2,2);


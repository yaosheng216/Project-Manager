
-- ----------------------------
-- Table structure for flow_definition
-- ----------------------------
DROP TABLE IF EXISTS `flow_definition`;
CREATE TABLE `flow_definition` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `act_def_id` varchar(255)  ,
  `company_id` varchar(255)  ,
  `is_free` boolean,
  `node_defs` json
)
;

-- ----------------------------
-- Table structure for flow_group
-- ----------------------------
DROP TABLE IF EXISTS `flow_group`;
CREATE TABLE `flow_group` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `company_id` varchar(255)  ,
  `is_build_in` boolean,
  `name` varchar(255)
)
;

-- ----------------------------
-- Table structure for flow_group_init
-- ----------------------------
DROP TABLE IF EXISTS `flow_group_init`;
CREATE TABLE `flow_group_init` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `is_build_in` boolean,
  `name` varchar(255)
)
;

-- ----------------------------
-- Table structure for flow_sheet
-- ----------------------------
DROP TABLE IF EXISTS `flow_sheet`;
CREATE TABLE `flow_sheet` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `audit_logs` json,
  `audit_persons` json,
  `company_id` varchar(255)  ,
  `copy_persons` json,
  `department_id` varchar(255)  ,
  `department_name` varchar(255)  ,
  `flow_app_id` varchar(255)  ,
  `instance_id` varchar(255)  ,
  `is_finished` boolean,
  `sheet_data` json,
  `sheet_number` varchar(255)  ,
  `staff_id` varchar(255)  ,
  `staff_name` varchar(255)  ,
  `status` int4,
  `submit_time` timestamp(6)
)
;

-- ----------------------------
-- Table structure for work_flow_app
-- ----------------------------
DROP TABLE IF EXISTS `work_flow_app`;
CREATE TABLE `work_flow_app` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `app_type` int4,
  `company_id` varchar(255)  ,
  `description` varchar(255)  ,
  `icon` varchar(255)  ,
  `inputs` json,
  `is_all_visible` boolean,
  `is_custom` boolean,
  `is_enabled` boolean,
  `name` varchar(255)  ,
  `visible_departments` json,
  `visible_staffs` json,
  `flow_def_id` varchar(50)  ,
  `group_id` varchar(50)
)
;

-- ----------------------------
-- Table structure for work_flow_app_init
-- ----------------------------
DROP TABLE IF EXISTS `work_flow_app_init`;
CREATE TABLE `work_flow_app_init` (
  `id` bigint(50)   NOT NULL,
  `create_time` timestamp(6),
  `updated_time` timestamp(6),
  `app_type` int4,
  `description` varchar(255)  ,
  `icon` varchar(255)  ,
  `inputs` json,
  `is_all_visible` boolean,
  `is_custom` boolean,
  `is_enabled` boolean,
  `name` varchar(255)  ,
  `group_id` varchar(50)
)
;

-- ----------------------------
-- Primary Key structure for table flow_definition
-- ----------------------------
ALTER TABLE `flow_definition` ADD CONSTRAINT `flow_definition_pkey` PRIMARY KEY (`id`);

-- ----------------------------
-- Primary Key structure for table flow_group
-- ----------------------------
ALTER TABLE `flow_group` ADD CONSTRAINT `flow_group_pkey` PRIMARY KEY (`id`);

-- ----------------------------
-- Primary Key structure for table flow_group_init
-- ----------------------------
ALTER TABLE `flow_group_init` ADD CONSTRAINT `flow_group_init_pkey` PRIMARY KEY (`id`);

-- ----------------------------
-- Primary Key structure for table flow_sheet
-- ----------------------------
ALTER TABLE `flow_sheet` ADD CONSTRAINT `flow_sheet_pkey` PRIMARY KEY (`id`);

-- ----------------------------
-- Primary Key structure for table work_flow_app
-- ----------------------------
ALTER TABLE `work_flow_app` ADD CONSTRAINT `work_flow_app_pkey` PRIMARY KEY (`id`);

-- ----------------------------
-- Primary Key structure for table work_flow_app_init
-- ----------------------------
ALTER TABLE `work_flow_app_init` ADD CONSTRAINT `work_flow_app_init_pkey` PRIMARY KEY (`id`);

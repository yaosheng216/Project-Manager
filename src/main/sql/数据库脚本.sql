/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.9-log : Database - oa-test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`oa-test` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `oa-test`;

/*Table structure for table `act_evt_log` */

DROP TABLE IF EXISTS `act_evt_log`;

CREATE TABLE `act_evt_log` (
  `LOG_NR_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DATA_` longblob,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_PROCESSED_` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`LOG_NR_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_evt_log` */

/*Table structure for table `act_ge_bytearray` */

DROP TABLE IF EXISTS `act_ge_bytearray`;

CREATE TABLE `act_ge_bytearray` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTES_` longblob,
  `GENERATED_` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ge_bytearray` */

/*Table structure for table `act_ge_property` */

DROP TABLE IF EXISTS `act_ge_property`;

CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL,
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  PRIMARY KEY (`NAME_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ge_property` */

insert  into `act_ge_property`(`NAME_`,`VALUE_`,`REV_`) values ('cfg.execution-related-entities-count','false',1),('next.dbid','1',1),('schema.history','create(7.0.0.0)',1),('schema.version','7.0.0.0',1);

/*Table structure for table `act_hi_actinst` */

DROP TABLE IF EXISTS `act_hi_actinst`;

CREATE TABLE `act_hi_actinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ACT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_ACT_INST_START` (`START_TIME_`) USING BTREE,
  KEY `ACT_IDX_HI_ACT_INST_END` (`END_TIME_`) USING BTREE,
  KEY `ACT_IDX_HI_ACT_INST_PROCINST` (`PROC_INST_ID_`,`ACT_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_ACT_INST_EXEC` (`EXECUTION_ID_`,`ACT_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_actinst` */

/*Table structure for table `act_hi_attachment` */

DROP TABLE IF EXISTS `act_hi_attachment`;

CREATE TABLE `act_hi_attachment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `URL_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CONTENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_attachment` */

/*Table structure for table `act_hi_comment` */

DROP TABLE IF EXISTS `act_hi_comment`;

CREATE TABLE `act_hi_comment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MESSAGE_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `FULL_MSG_` longblob,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_comment` */

/*Table structure for table `act_hi_detail` */

DROP TABLE IF EXISTS `act_hi_detail`;

CREATE TABLE `act_hi_detail` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_DETAIL_PROC_INST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_DETAIL_ACT_INST` (`ACT_INST_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_DETAIL_TIME` (`TIME_`) USING BTREE,
  KEY `ACT_IDX_HI_DETAIL_NAME` (`NAME_`) USING BTREE,
  KEY `ACT_IDX_HI_DETAIL_TASK_ID` (`TASK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_detail` */

/*Table structure for table `act_hi_identitylink` */

DROP TABLE IF EXISTS `act_hi_identitylink`;

CREATE TABLE `act_hi_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_IDENT_LNK_USER` (`USER_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_IDENT_LNK_TASK` (`TASK_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_IDENT_LNK_PROCINST` (`PROC_INST_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_identitylink` */

/*Table structure for table `act_hi_procinst` */

DROP TABLE IF EXISTS `act_hi_procinst`;

CREATE TABLE `act_hi_procinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `START_USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `END_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE KEY `PROC_INST_ID_` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PRO_INST_END` (`END_TIME_`) USING BTREE,
  KEY `ACT_IDX_HI_PRO_I_BUSKEY` (`BUSINESS_KEY_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_procinst` */

/*Table structure for table `act_hi_taskinst` */

DROP TABLE IF EXISTS `act_hi_taskinst`;

CREATE TABLE `act_hi_taskinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `CLAIM_TIME_` datetime(3) DEFAULT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_TASK_INST_PROCINST` (`PROC_INST_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_taskinst` */

/*Table structure for table `act_hi_varinst` */

DROP TABLE IF EXISTS `act_hi_varinst`;

CREATE TABLE `act_hi_varinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` datetime(3) DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`,`VAR_TYPE_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_hi_varinst` */

/*Table structure for table `act_procdef_info` */

DROP TABLE IF EXISTS `act_procdef_info`;

CREATE TABLE `act_procdef_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `INFO_JSON_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE KEY `ACT_UNIQ_INFO_PROCDEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_IDX_INFO_PROCDEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_INFO_JSON_BA` (`INFO_JSON_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_procdef_info` */

/*Table structure for table `act_re_deployment` */

DROP TABLE IF EXISTS `act_re_deployment`;

CREATE TABLE `act_re_deployment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
  `ENGINE_VERSION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_re_deployment` */

/*Table structure for table `act_re_model` */

DROP TABLE IF EXISTS `act_re_model`;

CREATE TABLE `act_re_model` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `META_INFO_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_re_model` */

/*Table structure for table `act_re_procdef` */

DROP TABLE IF EXISTS `act_re_procdef`;

CREATE TABLE `act_re_procdef` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `ENGINE_VERSION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`,`VERSION_`,`TENANT_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_re_procdef` */

/*Table structure for table `act_ru_deadletter_job` */

DROP TABLE IF EXISTS `act_ru_deadletter_job`;

CREATE TABLE `act_ru_deadletter_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_DEADLETTER_JOB_EXECUTION` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`) USING BTREE,
  KEY `ACT_FK_DEADLETTER_JOB_PROC_DEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_DEADLETTER_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_deadletter_job` */

/*Table structure for table `act_ru_event_subscr` */

DROP TABLE IF EXISTS `act_ru_event_subscr`;

CREATE TABLE `act_ru_event_subscr` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CONFIGURATION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`) USING BTREE,
  KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_event_subscr` */

/*Table structure for table `act_ru_execution` */

DROP TABLE IF EXISTS `act_ru_execution`;

CREATE TABLE `act_ru_execution` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ROOT_PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) DEFAULT NULL,
  `IS_MI_ROOT_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `CACHED_ENT_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) DEFAULT NULL,
  `START_USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_COUNT_ENABLED_` tinyint(4) DEFAULT NULL,
  `EVT_SUBSCR_COUNT_` int(11) DEFAULT NULL,
  `TASK_COUNT_` int(11) DEFAULT NULL,
  `JOB_COUNT_` int(11) DEFAULT NULL,
  `TIMER_JOB_COUNT_` int(11) DEFAULT NULL,
  `SUSP_JOB_COUNT_` int(11) DEFAULT NULL,
  `DEADLETTER_JOB_COUNT_` int(11) DEFAULT NULL,
  `VAR_COUNT_` int(11) DEFAULT NULL,
  `ID_LINK_COUNT_` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`) USING BTREE,
  KEY `ACT_IDC_EXEC_ROOT` (`ROOT_PROC_INST_ID_`) USING BTREE,
  KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`) USING BTREE,
  KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`) USING BTREE,
  KEY `ACT_FK_EXE_PROCDEF` (`PROC_DEF_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_execution` */

/*Table structure for table `act_ru_identitylink` */

DROP TABLE IF EXISTS `act_ru_identitylink`;

CREATE TABLE `act_ru_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`) USING BTREE,
  KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`) USING BTREE,
  KEY `ACT_IDX_ATHRZ_PROCEDEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`) USING BTREE,
  KEY `ACT_FK_IDL_PROCINST` (`PROC_INST_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_identitylink` */

/*Table structure for table `act_ru_integration` */

DROP TABLE IF EXISTS `act_ru_integration`;

CREATE TABLE `act_ru_integration` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `FLOW_NODE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_DATE_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_INT_EXECUTION` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_INT_PROC_INST` (`PROCESS_INSTANCE_ID_`) USING BTREE,
  KEY `ACT_FK_INT_PROC_DEF` (`PROC_DEF_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_integration` */

/*Table structure for table `act_ru_job` */

DROP TABLE IF EXISTS `act_ru_job`;

CREATE TABLE `act_ru_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RETRIES_` int(11) DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_JOB_EXECUTION` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`) USING BTREE,
  KEY `ACT_FK_JOB_PROC_DEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_job` */

/*Table structure for table `act_ru_suspended_job` */

DROP TABLE IF EXISTS `act_ru_suspended_job`;

CREATE TABLE `act_ru_suspended_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RETRIES_` int(11) DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_SUSPENDED_JOB_EXECUTION` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`) USING BTREE,
  KEY `ACT_FK_SUSPENDED_JOB_PROC_DEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_SUSPENDED_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_suspended_job` */

/*Table structure for table `act_ru_task` */

DROP TABLE IF EXISTS `act_ru_task`;

CREATE TABLE `act_ru_task` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DELEGATION_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CLAIM_TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`) USING BTREE,
  KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_task` */

/*Table structure for table `act_ru_timer_job` */

DROP TABLE IF EXISTS `act_ru_timer_job`;

CREATE TABLE `act_ru_timer_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RETRIES_` int(11) DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_TIMER_JOB_EXECUTION` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_TIMER_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`) USING BTREE,
  KEY `ACT_FK_TIMER_JOB_PROC_DEF` (`PROC_DEF_ID_`) USING BTREE,
  KEY `ACT_FK_TIMER_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_timer_job` */

/*Table structure for table `act_ru_variable` */

DROP TABLE IF EXISTS `act_ru_variable`;

CREATE TABLE `act_ru_variable` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`) USING BTREE,
  KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `act_ru_variable` */

/*Table structure for table `flow_definition` */

DROP TABLE IF EXISTS `flow_definition`;

CREATE TABLE `flow_definition` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `act_def_id` varchar(255) DEFAULT NULL,
  `company_id` varchar(255) DEFAULT NULL,
  `is_free` tinyint(1) DEFAULT NULL,
  `node_defs` json DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `flow_definition` */

insert  into `flow_definition`(`id`,`create_time`,`updated_time`,`act_def_id`,`company_id`,`is_free`,`node_defs`) values (1,'2019-10-10 08:36:41.591000','2019-10-10 08:36:41.591000',NULL,'1',1,NULL),(2,'2019-10-10 08:36:41.598000','2019-10-10 08:36:41.598000',NULL,'1',1,NULL),(3,'2019-10-10 08:36:41.604000','2019-10-10 08:36:41.604000',NULL,'1',1,NULL),(4,'2019-10-10 08:36:41.608000','2019-10-10 08:36:41.608000',NULL,'1',1,NULL),(5,'2019-10-10 08:36:41.613000','2019-10-10 08:36:41.613000',NULL,'1',1,NULL),(6,'2019-10-10 08:36:41.618000','2019-10-10 08:36:41.618000',NULL,'1',1,NULL),(7,'2019-10-10 08:36:41.623000','2019-10-10 08:36:41.623000',NULL,'1',1,NULL),(8,'2019-10-10 08:36:41.628000','2019-10-10 08:36:41.628000',NULL,'1',1,NULL),(9,'2019-10-10 08:36:41.632000','2019-10-10 08:36:41.632000',NULL,'1',1,NULL),(10,'2019-10-10 08:36:41.638000','2019-10-10 08:36:41.638000',NULL,'1',1,NULL),(11,'2019-10-10 08:36:41.642000','2019-10-10 08:36:41.642000',NULL,'1',1,NULL),(12,'2019-10-10 08:36:41.647000','2019-10-10 08:36:41.647000',NULL,'1',1,NULL);

/*Table structure for table `flow_group` */

DROP TABLE IF EXISTS `flow_group`;

CREATE TABLE `flow_group` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `company_id` varchar(255) DEFAULT NULL,
  `is_build_in` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `flow_group` */

insert  into `flow_group`(`id`,`create_time`,`updated_time`,`company_id`,`is_build_in`,`name`) values (1,'2019-05-20 15:07:11.891000','2019-10-10 08:36:41.571000','1',1,'其他'),(2,'2019-05-20 15:07:11.966000','2019-10-10 08:36:41.578000','1',0,'人事审批');

/*Table structure for table `flow_group_init` */

DROP TABLE IF EXISTS `flow_group_init`;

CREATE TABLE `flow_group_init` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `is_build_in` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `flow_group_init` */

insert  into `flow_group_init`(`id`,`create_time`,`updated_time`,`is_build_in`,`name`) values (1,'2019-05-20 15:07:11.891000','2019-05-20 15:07:11.891000',1,'其他'),(2,'2019-05-20 15:07:11.966000','2019-05-20 15:07:11.966000',0,'人事审批');

/*Table structure for table `flow_sheet` */

DROP TABLE IF EXISTS `flow_sheet`;

CREATE TABLE `flow_sheet` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `audit_logs` json DEFAULT NULL,
  `audit_persons` json DEFAULT NULL,
  `company_id` varchar(255) DEFAULT NULL,
  `copy_persons` json DEFAULT NULL,
  `department_id` varchar(255) DEFAULT NULL,
  `department_name` varchar(255) DEFAULT NULL,
  `flow_app_id` varchar(255) DEFAULT NULL,
  `instance_id` varchar(255) DEFAULT NULL,
  `is_finished` tinyint(1) DEFAULT NULL,
  `sheet_data` json DEFAULT NULL,
  `sheet_number` varchar(255) DEFAULT NULL,
  `staff_id` varchar(255) DEFAULT NULL,
  `staff_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `submit_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `flow_sheet` */

/*Table structure for table `work_flow_app` */

DROP TABLE IF EXISTS `work_flow_app`;

CREATE TABLE `work_flow_app` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `app_type` int(11) DEFAULT NULL,
  `company_id` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `inputs` json DEFAULT NULL,
  `is_all_visible` tinyint(1) DEFAULT NULL,
  `is_custom` tinyint(1) DEFAULT NULL,
  `is_enabled` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `visible_departments` json DEFAULT NULL,
  `visible_staffs` json DEFAULT NULL,
  `flow_def_id` varchar(50) DEFAULT NULL,
  `group_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk50x4xgnurtr3lbojwn7xu3kxg` (`group_id`) USING BTREE,
  KEY `fktbr2kvtnbtmjwavur85bdmg3e` (`flow_def_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `work_flow_app` */

insert  into `work_flow_app`(`id`,`create_time`,`updated_time`,`app_type`,`company_id`,`description`,`icon`,`inputs`,`is_all_visible`,`is_custom`,`is_enabled`,`name`,`visible_departments`,`visible_staffs`,`flow_def_id`,`group_id`) values (1,'2019-10-10 08:36:41.672000','2019-10-10 08:36:41.672000',0,'1','请假申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择\", \"code\": \"LeaveType\", \"unit\": null, \"title\": \"请假类型\", \"options\": [\"事假\", \"病假\", \"调休\", \"年假\", \"婚假\", \"产假\", \"陪产假\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择时间\", \"code\": \"LeaveStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择时间\", \"code\": \"LeaveEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请假时长将自动计入考勤统计\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入请假事由\", \"code\": \"LeaveReason\", \"unit\": null, \"title\": \"请假事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"LeavePicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,0,1,'请假',NULL,NULL,'1','2'),(2,'2019-10-10 08:36:41.698000','2019-10-10 08:36:41.698000',3,'1','补卡申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择时间\", \"code\": \"CardTime\", \"unit\": null, \"title\": \"补卡时间点\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择是否上班\", \"code\": \"StartFlag\", \"unit\": null, \"title\": \"是否上班打卡\", \"options\": null, \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入补卡原因\", \"code\": \"CardReason\", \"unit\": null, \"title\": \"补卡原因\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"CardPicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,0,1,'补卡',NULL,NULL,'2','2'),(3,'2019-10-10 08:36:41.709000','2019-10-10 08:36:41.709000',1,'1','外出申请','icon-waichu-F-','[{\"tip\": \"请选择时间\", \"code\": \"OutWorkStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择时间\", \"code\": \"OutWorkEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"根据排班时间自动计算时长\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入外出事由\", \"code\": \"OutWorkReason\", \"unit\": null, \"title\": \"外出事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"OutWorkPicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,0,1,'外出',NULL,NULL,'3','2'),(4,'2019-10-10 08:36:41.716000','2019-10-10 08:36:41.716000',2,'1','加班申请','icon-jiaban-1','[{\"tip\": \"请选择时间\", \"code\": \"OverTimeStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择时间\", \"code\": \"OverTimeEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"根据排班时间自动计算时长\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入加班事由\", \"code\": \"OverTimeReason\", \"unit\": null, \"title\": \"加班事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"OverTimePicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,0,1,'加班',NULL,NULL,'4','2'),(5,'2019-10-10 08:36:41.726000','2019-10-10 08:36:41.726000',NULL,'1','出差申请','icon-waichu-F-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"开始时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"结束时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入数字\", \"code\": null, \"unit\": \"\", \"title\": \"时长(天)\", \"options\": [], \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"出差事由\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'出差',NULL,NULL,'5','2'),(6,'2019-10-10 08:36:41.732000','2019-10-10 08:36:41.732000',NULL,'1','日常费用报销申请','icon-baoxiao-hongse-','[{\"tip\": \"请输入数字\", \"code\": null, \"unit\": \"\", \"title\": \"报销金额(元)\", \"options\": [], \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"如：采购费用、活动经费\", \"code\": null, \"unit\": \"\", \"title\": \"报销类别\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"费用明细\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'报销',NULL,NULL,'6','1'),(7,'2019-10-10 08:36:41.739000','2019-10-10 08:36:41.739000',NULL,'1','转正申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"试用期岗位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"对本岗位的理解\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"试用期工作总结\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"对公司的意见和建议\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'转正',NULL,NULL,'7','1'),(8,'2019-10-10 08:36:41.745000','2019-10-10 08:36:41.745000',NULL,'1','离职申请','icon-shenpi-chengse-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"离职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"所属岗位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"交接人员\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"劳动合同开始时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"劳动合同结束时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"离职原因\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"所需交接事项\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'离职',NULL,NULL,'8','1'),(9,'2019-10-10 08:36:41.753000','2019-10-10 08:36:41.753000',NULL,'1','资质使用申请','icon-shenpi-chengse-','[{\"tip\": \"如：营业执照\", \"code\": null, \"unit\": \"\", \"title\": \"资质类别\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"经办人\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"经办人部门\", \"options\": [], \"required\": false, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"是否外带\", \"options\": [\"是\", \"否\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"借用时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"归还时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"使用事由\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"备注\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'资质使用',NULL,NULL,'9','1'),(10,'2019-10-10 08:36:41.759000','2019-10-10 08:36:41.759000',NULL,'1','录用审批','icon-shuju-zise-','[{\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"员工姓名\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"录用部门\", \"options\": [], \"required\": true, \"inputType\": \"DepartSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": true, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"职位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"手机号码\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"员工类型\", \"options\": [\"无类型\", \"全职\", \"兼职\", \"实习\", \"劳务派遣\", \"退休返聘\", \"劳务外包\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职日期\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',0,0,0,'录用审批',NULL,NULL,'10','1'),(11,'2019-10-10 08:36:41.764000','2019-10-10 08:36:41.764000',NULL,'1','物品领用申请','icon-jiaban-1','[{\"tip\": \"如：日常办公\", \"code\": null, \"unit\": \"\", \"title\": \"物品用途\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"物品名称\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"数量\", \"options\": [], \"required\": false, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"领用详情\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'物品领用',NULL,NULL,'11','1'),(12,'2019-10-10 08:36:41.772000','2019-10-10 08:36:41.772000',NULL,'1','通用审批','icon-shenpi-chengse-','[{\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"申请内容\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"申请详情\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',0,0,0,'通用审批',NULL,NULL,'12','1');

/*Table structure for table `work_flow_app_init` */

DROP TABLE IF EXISTS `work_flow_app_init`;

CREATE TABLE `work_flow_app_init` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `app_type` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `inputs` json DEFAULT NULL,
  `is_all_visible` tinyint(1) DEFAULT NULL,
  `is_custom` tinyint(1) DEFAULT NULL,
  `is_enabled` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `group_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk9ga43e8n4d8le3m5bbk1jpf7r` (`group_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `work_flow_app_init` */

insert  into `work_flow_app_init`(`id`,`create_time`,`updated_time`,`app_type`,`description`,`icon`,`inputs`,`is_all_visible`,`is_custom`,`is_enabled`,`name`,`group_id`) values (1,'2019-05-20 15:07:12.052000','2019-05-20 15:07:12.095000',0,'请假申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择\", \"code\": \"LeaveType\", \"unit\": null, \"title\": \"请假类型\", \"options\": [\"事假\", \"病假\", \"调休\", \"年假\", \"婚假\", \"产假\", \"陪产假\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null}, {\"tip\": \"请选择时间\", \"code\": \"LeaveStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请选择时间\", \"code\": \"LeaveEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请假时长将自动计入考勤统计\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入请假事由\", \"code\": \"LeaveReason\", \"unit\": null, \"title\": \"请假事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"LeavePicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null}]',1,0,1,'请假','2'),(2,'2019-05-20 15:07:12.090000','2019-05-20 15:07:12.096000',3,'补卡申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择时间\", \"code\": \"CardTime\", \"unit\": null, \"title\": \"补卡时间点\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请选择是否上班\", \"code\": \"StartFlag\", \"unit\": null, \"title\": \"是否上班打卡\", \"options\": null, \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请输入补卡原因\", \"code\": \"CardReason\", \"unit\": null, \"title\": \"补卡原因\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"CardPicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null}]',1,0,1,'补卡','2'),(3,'2019-05-20 15:07:12.091000','2019-05-20 15:07:12.097000',1,'外出申请','icon-waichu-F-','[{\"tip\": \"请选择时间\", \"code\": \"OutWorkStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请选择时间\", \"code\": \"OutWorkEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"根据排班时间自动计算时长\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入外出事由\", \"code\": \"OutWorkReason\", \"unit\": null, \"title\": \"外出事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"OutWorkPicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null}]',1,0,1,'外出','2'),(4,'2019-05-20 15:07:12.093000','2019-05-20 15:07:12.097000',2,'加班申请','icon-jiaban-1','[{\"tip\": \"请选择时间\", \"code\": \"OverTimeStartTime\", \"unit\": null, \"title\": \"开始时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"请选择时间\", \"code\": \"OverTimeEndTime\", \"unit\": null, \"title\": \"结束时间\", \"options\": null, \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": true, \"isSingleSelect\": null}, {\"tip\": \"根据排班时间自动计算时长\", \"code\": \"Duration\", \"unit\": \"天\", \"title\": \"时长（天）\", \"options\": null, \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null, \"calculateFormula\": \"结束时间-开始时间\", \"isNeedToCalculate\": true}, {\"tip\": \"请输入加班事由\", \"code\": \"OverTimeReason\", \"unit\": null, \"title\": \"加班事由\", \"options\": null, \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": true, \"maxLength\": 0, \"isDateTime\": null, \"isSingleSelect\": null}, {\"tip\": \"图片最多可添加9张\", \"code\": \"OverTimePicture\", \"unit\": null, \"title\": \"图片\", \"options\": null, \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": true, \"maxLength\": 9, \"isDateTime\": null, \"isSingleSelect\": null}]',1,0,1,'加班','2'),(5,'2019-06-06 17:16:19.047000','2019-06-06 17:17:48.810000',NULL,'出差申请','icon-waichu-F-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"开始时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"结束时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入数字\", \"code\": null, \"unit\": \"\", \"title\": \"时长(天)\", \"options\": [], \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"出差事由\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'出差','2'),(6,'2019-06-06 16:43:10.453000','2019-06-06 16:57:38.899000',NULL,'日常费用报销申请','icon-baoxiao-hongse-','[{\"tip\": \"请输入数字\", \"code\": null, \"unit\": \"\", \"title\": \"报销金额(元)\", \"options\": [], \"required\": true, \"inputType\": \"Digital\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"如：采购费用、活动经费\", \"code\": null, \"unit\": \"\", \"title\": \"报销类别\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"费用明细\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'报销','1'),(7,'2019-06-06 16:52:23.835000','2019-06-06 16:59:49.283000',NULL,'转正申请','icon-qingjia-BACFF-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"试用期岗位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"对本岗位的理解\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"试用期工作总结\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"对公司的意见和建议\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'转正','1'),(8,'2019-06-06 16:54:07.616000','2019-06-06 17:05:18.245000',NULL,'离职申请','icon-shenpi-chengse-','[{\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"离职时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"所属岗位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"交接人员\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"劳动合同开始时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"劳动合同结束时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"离职原因\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"所需交接事项\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'离职','1'),(9,'2019-06-06 16:54:34.692000','2019-06-06 17:02:22.305000',NULL,'资质使用申请','icon-shenpi-chengse-','[{\"tip\": \"如：营业执照\", \"code\": null, \"unit\": \"\", \"title\": \"资质类别\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"经办人\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"经办人部门\", \"options\": [], \"required\": false, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"是否外带\", \"options\": [\"是\", \"否\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"借用时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"归还时间\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"使用事由\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"备注\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'资质使用','1'),(10,'2019-06-06 17:05:53.226000','2019-06-06 17:09:01.548000',NULL,'录用审批','icon-shuju-zise-','[{\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"员工姓名\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"录用部门\", \"options\": [], \"required\": true, \"inputType\": \"DepartSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": true, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"职位\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"手机号码\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"员工类型\", \"options\": [\"无类型\", \"全职\", \"兼职\", \"实习\", \"劳务派遣\", \"退休返聘\", \"劳务外包\"], \"required\": true, \"inputType\": \"SingleSelect\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请选择\", \"code\": null, \"unit\": \"\", \"title\": \"入职日期\", \"options\": [], \"required\": true, \"inputType\": \"Date\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',0,0,0,'录用审批','1'),(11,'2019-06-06 17:09:35.289000','2019-06-06 17:10:52.277000',NULL,'物品领用申请','icon-jiaban-1','[{\"tip\": \"如：日常办公\", \"code\": null, \"unit\": \"\", \"title\": \"物品用途\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"物品名称\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"数量\", \"options\": [], \"required\": false, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"领用详情\", \"options\": [], \"required\": false, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',1,1,1,'物品领用','1'),(12,'2019-06-06 17:11:18.321000','2019-06-06 17:12:23.038000',NULL,'通用审批','icon-shenpi-chengse-','[{\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"申请内容\", \"options\": [], \"required\": true, \"inputType\": \"SingleText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"请输入\", \"code\": null, \"unit\": \"\", \"title\": \"申请详情\", \"options\": [], \"required\": true, \"inputType\": \"MultiText\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}, {\"tip\": \"undefined\", \"code\": null, \"unit\": \"\", \"title\": \"图片\", \"options\": [], \"required\": false, \"inputType\": \"Picture\", \"isBuildIn\": false, \"maxLength\": -1, \"isDateTime\": false, \"isSingleSelect\": false, \"calculateFormula\": null, \"isNeedToCalculate\": null}]',0,0,0,'通用审批','1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- --------------------------------------------------------
-- ホスト:                          127.0.0.1
-- サーバーのバージョン:                   10.6.4-MariaDB - mariadb.org binary distribution
-- サーバー OS:                      Win64
-- HeidiSQL バージョン:               11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- vec のデータベース構造をダンプしています
CREATE DATABASE IF NOT EXISTS `vec` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci */;
USE `vec`;

--  テーブル vec.m_user の構造をダンプしています
CREATE TABLE IF NOT EXISTS `m_user` (
  `SERI_NO` int(11) DEFAULT NULL,
  `USER_ID` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `USER_PASS` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル vec.m_user: ~1 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `m_user` DISABLE KEYS */;
INSERT INTO `m_user` (`SERI_NO`, `USER_ID`, `USER_PASS`) VALUES
	(1, 'JJJ', '123'),
	(2, 'KKK', '123'),
	(3, 'AAB', '456');
/*!40000 ALTER TABLE `m_user` ENABLE KEYS */;

--  テーブル vec.t_basic_info の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_basic_info` (
  `KUR` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `PROJ_F_CODE` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `MODEL_CD` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `COLOR` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `MANUF_DATE` datetime DEFAULT NULL,
  `INS_DT` datetime DEFAULT NULL,
  `INST_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  UNIQUE KEY `KUR` (`KUR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル vec.t_basic_info: ~5 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_basic_info` DISABLE KEYS */;
INSERT INTO `t_basic_info` (`KUR`, `PROJ_F_CODE`, `MODEL_CD`, `COLOR`, `MANUF_DATE`, `INS_DT`, `INST_BY`, `UPD_DT`, `UPD_BY`) VALUES
	('FFF', 'XXX', 'YYY', 'Red', '2024-09-10 22:41:50', '2024-09-10 22:42:09', 'VECB0202', NULL, NULL),
	('DJJ', 'DGGh', 'YYGF', 'Blue', '2024-09-11 22:49:37', '2024-09-11 22:49:38', 'VECB0202', NULL, NULL),
	('DDD', NULL, NULL, NULL, '2024-09-15 10:07:19', '2024-09-15 10:07:20', 'VECB0202', NULL, NULL),
	('AAA', NULL, NULL, NULL, '2024-09-15 10:07:30', '2024-09-15 10:07:32', 'VECB0202', NULL, NULL),
	('EEE', NULL, NULL, NULL, '2024-09-15 10:07:39', '2024-09-15 10:07:40', 'VECB0202', NULL, NULL);
/*!40000 ALTER TABLE `t_basic_info` ENABLE KEYS */;

--  テーブル vec.t_ecu_info の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_ecu_info` (
  `KUR` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `CMNMN` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `CMNMN_PL_VAL` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `INS_DT` datetime DEFAULT NULL,
  `INS_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル vec.t_ecu_info: ~7 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_ecu_info` DISABLE KEYS */;
INSERT INTO `t_ecu_info` (`KUR`, `CMNMN`, `CMNMN_PL_VAL`, `INS_DT`, `INS_BY`, `UPD_DT`, `UPD_BY`) VALUES
	('FFF', 'REU_METER', '21CX54J0A', '2024-09-10 22:43:02', 'VECB0202', NULL, NULL),
	('FFF', 'UYU_RETER', '452DF02XJ', '2024-09-10 22:43:36', 'VECB0202', NULL, NULL),
	('DJJ', 'REU_METER', '20CX54J0A', '2024-09-11 22:50:10', 'VECB0202', NULL, NULL),
	('DJJ', 'TYC_RTY', '772DF02XJ', '2024-09-11 22:50:30', 'VECB0202', NULL, NULL),
	('AAA', 'EE', '', '2024-09-15 10:08:36', 'VECB0202', NULL, NULL),
	('DDD', 'ser', '', '2024-09-15 10:08:52', 'VECB0202', NULL, NULL),
	('EEE', 'sf', NULL, '2024-09-15 10:09:06', 'VECB0202', NULL, NULL);
/*!40000 ALTER TABLE `t_ecu_info` ENABLE KEYS */;

--  テーブル vec.t_user_search_criteria_ の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_user_search_criteria_` (
  `S_C_ID` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `USER_ID` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `KUR` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `PROJECT_JYA_CODE` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `MODEL_CODE` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `COLOR` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `MANUFACTER_DATE` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `INS_DT` datetime NOT NULL,
  `INS_BY` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL DEFAULT '',
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`S_C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル vec.t_user_search_criteria_: ~4 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_user_search_criteria_` DISABLE KEYS */;
INSERT INTO `t_user_search_criteria_` (`S_C_ID`, `USER_ID`, `KUR`, `PROJECT_JYA_CODE`, `MODEL_CODE`, `COLOR`, `MANUFACTER_DATE`, `INS_DT`, `INS_BY`, `UPD_DT`, `UPD_BY`) VALUES
	('2024-09-14 12:30:15', 'KKK', 'CC', 'CC', '', '', '', '2024-09-14 12:30:17', 'VECWS01', NULL, NULL),
	('2024-09-15 11:07:55', 'JJJ', '', '', '', '', '', '2024-09-15 11:07:56', 'VECWS01', NULL, NULL),
	('2024-09-15 11:08:02', 'JJJ', '', '', '', '', '', '2024-09-15 11:08:03', 'VECWS01', NULL, NULL),
	('2024-09-20 22:58:21', 'JJJ', 'CC', 'CC', '', '', '', '2024-09-20 22:58:24', 'VECWS01', NULL, NULL);
/*!40000 ALTER TABLE `t_user_search_criteria_` ENABLE KEYS */;

--  テーブル vec.t_user_search_result の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_user_search_result` (
  `S_R_ID` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `USER_ID` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `DWN_LNK` varchar(16383) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `ERR_MSG` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `INS_DT` datetime DEFAULT NULL,
  `INS_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル vec.t_user_search_result: ~14 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_user_search_result` DISABLE KEYS */;
INSERT INTO `t_user_search_result` (`S_R_ID`, `USER_ID`, `STATUS`, `DWN_LNK`, `ERR_MSG`, `INS_DT`, `INS_BY`, `UPD_DT`, `UPD_BY`) VALUES
	('eee', 'eee', 0, 'ee', 'eee', '2024-09-14 23:48:57', NULL, NULL, NULL),
	('20240914-235347', 'KKK', 1, '', 'No record found.', '2024-09-14 23:53:47', 'VECWS02', NULL, NULL),
	('20240915-135942', 'KKK', 0, '/api/download?fileName=20240915-135942.zip&userId=KKK', '', '2024-09-15 13:59:42', 'VECWS02', NULL, NULL),
	('20240915-140020', 'KKK', 1, '', 'Count of search result exceeds limit :: 5.', '2024-09-15 14:00:20', 'VECWS02', NULL, NULL),
	('20240915-140134', 'KKK', 1, '', 'Count of search result exceeds limit :: 5.', '2024-09-15 14:01:34', 'VECWS02', NULL, NULL),
	('20240915-140136', 'KKK', 1, '', 'Count of search result exceeds limit :: 5.', '2024-09-15 14:01:36', 'VECWS02', NULL, NULL),
	('20240915-140138', 'KKK', 1, '', 'Count of search result exceeds limit :: 5.', '2024-09-15 14:01:38', 'VECWS02', NULL, NULL),
	('20240921-224704', 'JJJ', 0, '/api/download?fileName=20240921-224704.zip&userId=JJJ', '', '2024-09-21 22:47:04', 'VECWS02', NULL, NULL),
	('20241021-222935', 'AAB', 0, '/api/download?fileName=20241021-222935.zip&userId=AAB', '', '2024-10-21 22:29:35', 'VECWS02', NULL, NULL),
	('20241022-202007', 'JJJ', 1, '', 'No record found.', '2024-10-22 20:20:07', 'VECWS02', NULL, NULL),
	('20241022-202018', 'JJJ', 1, '', 'No record found.', '2024-10-22 20:20:18', 'VECWS02', NULL, NULL),
	('20241022-202023', 'JJJ', 0, '/api/download?fileName=20241022-202023.zip&userId=JJJ', '', '2024-10-22 20:20:23', 'VECWS02', NULL, NULL),
	('20241022-202301', 'JJJ', 0, '/api/download?fileName=20241022-202301.zip&userId=JJJ', '', '2024-10-22 20:23:01', 'VECWS02', NULL, NULL),
	('rrr', 'JJJ', 0, '/api/download?fileName=20241022-202350.csv&userId=JJJ', '', '2024-10-22 20:23:50', 'VECWS02', NULL, NULL);
/*!40000 ALTER TABLE `t_user_search_result` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

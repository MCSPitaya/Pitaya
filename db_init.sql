-- MySQL dump 10.16  Distrib 10.1.26-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: pitaya_dev1
-- ------------------------------------------------------
-- Server version	10.1.26-MariaDB-0+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `pitaya_dev1`
--

--CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pitaya_dev1` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

--USE `pitaya_dev1`;

--
-- Table structure for table `case_auth_codes`
--

DROP TABLE IF EXISTS `case_auth_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `case_auth_codes` (
  `case_id` bigint(20) NOT NULL,
  `auth_codes` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`case_id`,`user_id`),
  KEY `FK3fmwth8h6c24jebykyacm6w45` (`user_id`),
  CONSTRAINT `FK3fmwth8h6c24jebykyacm6w45` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKalwckyoxe331xi97ut4g6if3x` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `case_auth_codes`
--

LOCK TABLES `case_auth_codes` WRITE;
/*!40000 ALTER TABLE `case_auth_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `case_auth_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `case_clients`
--

DROP TABLE IF EXISTS `case_clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `case_clients` (
  `case_id` bigint(20) NOT NULL,
  `clients_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_3b3h3jho76sgfeb4if73vscli` (`clients_id`),
  KEY `FKa1llt428trutu4eif4p0crycp` (`case_id`),
  CONSTRAINT `FKa1llt428trutu4eif4p0crycp` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`),
  CONSTRAINT `FKoo55m84yb18qmecwfvkhw24pu` FOREIGN KEY (`clients_id`) REFERENCES `clients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `case_clients`
--

LOCK TABLES `case_clients` WRITE;
/*!40000 ALTER TABLE `case_clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `case_clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cases`
--

DROP TABLE IF EXISTS `cases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cases` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `case_number` varchar(255) DEFAULT NULL,
  `cre_dat` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `mod_dat` datetime NOT NULL,
  `title` varchar(255) NOT NULL,
  `court_id` bigint(20) NOT NULL,
  `cre_uid` bigint(20) NOT NULL,
  `firm_id` bigint(20) NOT NULL,
  `mod_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKskx3eiuk7m3en7vul6dmscuwm` (`court_id`),
  KEY `FKqbxojnipw0fr1ou4263wn82qj` (`cre_uid`),
  KEY `FK2k49ay6wsdpk4ws5g1hy7b61n` (`firm_id`),
  KEY `FKexovr168pdek0d8fb84o710me` (`mod_uid`),
  CONSTRAINT `FK2k49ay6wsdpk4ws5g1hy7b61n` FOREIGN KEY (`firm_id`) REFERENCES `firms` (`id`),
  CONSTRAINT `FKexovr168pdek0d8fb84o710me` FOREIGN KEY (`mod_uid`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqbxojnipw0fr1ou4263wn82qj` FOREIGN KEY (`cre_uid`) REFERENCES `users` (`id`),
  CONSTRAINT `FKskx3eiuk7m3en7vul6dmscuwm` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cases`
--

LOCK TABLES `cases` WRITE;
/*!40000 ALTER TABLE `cases` DISABLE KEYS */;
/*!40000 ALTER TABLE `cases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `tel_number` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `firm_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq68d4830xrrknh7jwx3nsdkyo` (`firm_id`),
  CONSTRAINT `FKq68d4830xrrknh7jwx3nsdkyo` FOREIGN KEY (`firm_id`) REFERENCES `firms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courts`
--

DROP TABLE IF EXISTS `courts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `tel_number` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courts`
--

LOCK TABLES `courts` WRITE;
/*!40000 ALTER TABLE `courts` DISABLE KEYS */;
INSERT INTO `courts` VALUES (1,'Basel',NULL,'Kanzlei Schlichtungsbehörde','5','Bäumleingasse','+41 61 267 64 39','4051'),(2,'Bern',NULL,'Schlichtungsbehörde Bern-Mittelland','34','Effingerstrasse','+41 31 635 47 50','3008');
/*!40000 ALTER TABLE `courts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_auth_codes`
--

DROP TABLE IF EXISTS `file_auth_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_auth_codes` (
  `file_id` bigint(20) NOT NULL,
  `auth_codes` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`file_id`,`user_id`),
  KEY `FKt3ykdkgydeouei2xv3kvfnnb` (`user_id`),
  CONSTRAINT `FK1qta5s9el2jqh8b0f0ltmi1v` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`),
  CONSTRAINT `FKt3ykdkgydeouei2xv3kvfnnb` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_auth_codes`
--

LOCK TABLES `file_auth_codes` WRITE;
/*!40000 ALTER TABLE `file_auth_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_auth_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_data`
--

DROP TABLE IF EXISTS `file_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cre_dat` datetime NOT NULL,
  `data` longblob NOT NULL,
  `cre_uid` bigint(20) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdthjnng1c1cmpbqb6v4l6wi3c` (`cre_uid`),
  KEY `FKtave7n6r0sotgifolu2sssmij` (`file_id`),
  CONSTRAINT `FKdthjnng1c1cmpbqb6v4l6wi3c` FOREIGN KEY (`cre_uid`) REFERENCES `users` (`id`),
  CONSTRAINT `FKtave7n6r0sotgifolu2sssmij` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_data`
--

LOCK TABLES `file_data` WRITE;
/*!40000 ALTER TABLE `file_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cre_dat` datetime NOT NULL,
  `mod_dat` datetime NOT NULL,
  `name` varchar(80) NOT NULL,
  `cre_uid` bigint(20) NOT NULL,
  `mod_uid` bigint(20) NOT NULL,
  `case_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK74rctlu9erf0hgw3xdpwh6e8j` (`case_id`,`name`),
  KEY `FKnnnyrli42cqj041ndwfgc9it9` (`cre_uid`),
  KEY `FKdb1o1l1km1m62qq3qrho13cfy` (`mod_uid`),
  CONSTRAINT `FKdb1o1l1km1m62qq3qrho13cfy` FOREIGN KEY (`mod_uid`) REFERENCES `users` (`id`),
  CONSTRAINT `FKiwu12234cs2ls830fctxnvp2k` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`),
  CONSTRAINT `FKnnnyrli42cqj041ndwfgc9it9` FOREIGN KEY (`cre_uid`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `firms`
--

DROP TABLE IF EXISTS `firms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `firms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `firms`
--

LOCK TABLES `firms` WRITE;
/*!40000 ALTER TABLE `firms` DISABLE KEYS */;
INSERT INTO `firms` VALUES (1,NULL,'Pitaya Test Firm',NULL,NULL,NULL),(2,NULL,'PITAYA',NULL,NULL,NULL);
/*!40000 ALTER TABLE `firms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cre_dat` datetime NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `file_id` bigint(20) DEFAULT NULL,
  `firm_id` bigint(20) NOT NULL,
  `case_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsq5ykqyrih8ab03a313tdv79u` (`file_id`),
  KEY `FKg1m74gu8mndnb40re79tliv0t` (`firm_id`),
  KEY `FKr8kgys5l8q5iq4katmhi6xflj` (`case_id`),
  KEY `FK9y21adhxn0ayjhfocscqox7bh` (`user_id`),
  CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKg1m74gu8mndnb40re79tliv0t` FOREIGN KEY (`firm_id`) REFERENCES `firms` (`id`),
  CONSTRAINT `FKr8kgys5l8q5iq4katmhi6xflj` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`),
  CONSTRAINT `FKsq5ykqyrih8ab03a313tdv79u` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokens` (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cre_dat` datetime NOT NULL,
  `exp_dat` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`token_id`),
  KEY `FK2dylsfo39lgjyqml2tbe0b0ss` (`user_id`),
  CONSTRAINT `FK2dylsfo39lgjyqml2tbe0b0ss` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `auth_codes` varchar(255) DEFAULT NULL,
  `email` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `password` varchar(100) NOT NULL,
  `tech_user` bit(1) NOT NULL,
  `username` varchar(15) NOT NULL,
  `firm_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK_sx468g52bpetvlad2j9y0lptc` (`email`),
  KEY `FKupc81b0g575u2jegr5nimg76` (`firm_id`),
  CONSTRAINT `FKupc81b0g575u2jegr5nimg76` FOREIGN KEY (`firm_id`) REFERENCES `firms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'','ADMIN','test@test.com','Test User','$2a$10$MJqeEWJBz28pDGyO0hdXreS3bXGrLezlJCVXuv7.tLwWInVxWrwvS','\0','test',1),(2,'\0','ADMIN','support@example.com','PITAYA','dummy','','__tech',2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `v_case_auth`
--

DROP TABLE IF EXISTS `v_case_auth`;
/*!50001 DROP VIEW IF EXISTS `v_case_auth`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_case_auth` (
  `id` tinyint NOT NULL,
  `user_id` tinyint NOT NULL,
  `case_auth` tinyint NOT NULL,
  `user_auth` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_case_summary`
--

DROP TABLE IF EXISTS `v_case_summary`;
/*!50001 DROP VIEW IF EXISTS `v_case_summary`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_case_summary` (
  `id` tinyint NOT NULL,
  `case_number` tinyint NOT NULL,
  `title` tinyint NOT NULL,
  `cre_dat` tinyint NOT NULL,
  `mod_dat` tinyint NOT NULL,
  `user_id` tinyint NOT NULL,
  `case_auth` tinyint NOT NULL,
  `user_auth` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_file_auth`
--

DROP TABLE IF EXISTS `v_file_auth`;
/*!50001 DROP VIEW IF EXISTS `v_file_auth`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_file_auth` (
  `id` tinyint NOT NULL,
  `user_id` tinyint NOT NULL,
  `file_auth` tinyint NOT NULL,
  `case_auth` tinyint NOT NULL,
  `user_auth` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Current Database: `pitaya_dev1`
--

--USE `pitaya_dev1`;

--
-- Final view structure for view `v_case_auth`
--

/*!50001 DROP TABLE IF EXISTS `v_case_auth`*/;
/*!50001 DROP VIEW IF EXISTS `v_case_auth`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pitaya`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_case_auth` AS select `c`.`id` AS `id`,`u`.`id` AS `user_id`,`ca`.`auth_codes` AS `case_auth`,`u`.`auth_codes` AS `user_auth` from ((`cases` `c` join `users` `u` on((`c`.`firm_id` = `u`.`firm_id`))) left join `case_auth_codes` `ca` on(((`ca`.`case_id` = `c`.`id`) and (`ca`.`user_id` = `u`.`id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_case_summary`
--

/*!50001 DROP TABLE IF EXISTS `v_case_summary`*/;
/*!50001 DROP VIEW IF EXISTS `v_case_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pitaya`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_case_summary` AS select `c`.`id` AS `id`,`c`.`case_number` AS `case_number`,`c`.`title` AS `title`,`c`.`cre_dat` AS `cre_dat`,`c`.`mod_dat` AS `mod_dat`,`u`.`id` AS `user_id`,`a`.`auth_codes` AS `case_auth`,`u`.`auth_codes` AS `user_auth` from ((`cases` `c` join `users` `u` on((`c`.`firm_id` = `u`.`firm_id`))) left join `case_auth_codes` `a` on(((`a`.`user_id` = `u`.`id`) and (`a`.`case_id` = `c`.`id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_file_auth`
--

/*!50001 DROP TABLE IF EXISTS `v_file_auth`*/;
/*!50001 DROP VIEW IF EXISTS `v_file_auth`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pitaya`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_file_auth` AS select `f`.`id` AS `id`,`u`.`id` AS `user_id`,`fa`.`auth_codes` AS `file_auth`,`ca`.`auth_codes` AS `case_auth`,`u`.`auth_codes` AS `user_auth` from ((((`files` `f` join `cases` `c` on((`f`.`case_id` = `c`.`id`))) join `users` `u` on((`c`.`firm_id` = `u`.`firm_id`))) left join `file_auth_codes` `fa` on(((`fa`.`file_id` = `f`.`id`) and (`fa`.`user_id` = `u`.`id`)))) left join `case_auth_codes` `ca` on(((`ca`.`case_id` = `c`.`id`) and (`ca`.`user_id` = `u`.`id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-08 10:51:43

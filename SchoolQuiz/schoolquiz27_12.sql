-- MySQL dump 10.10
--
-- Host: localhost    Database: schoolquiz
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
CREATE TABLE `answers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ANSWER_TEXT` text,
  `ENABLED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `answers`
--


/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
LOCK TABLES `answers` WRITE;
INSERT INTO `answers` VALUES (16,'1940',1),(17,'1941',1),(18,'1939',1),(19,'1938',1),(20,'Стив Возняк',1),(21,'Билл Гейтс',1),(22,'Стив джобс',1),(23,'Да',1),(24,'Нет',1),(25,'Java',1),(26,'C++',1),(27,'Actionscript 3',1),(28,'Assembler',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;

--
-- Table structure for table `question_answer`
--

DROP TABLE IF EXISTS `question_answer`;
CREATE TABLE `question_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION_ID` int(11) DEFAULT NULL,
  `ANSWER_ID` int(11) DEFAULT NULL,
  `RIGHT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `QUESTION_ID` (`QUESTION_ID`),
  KEY `ANSWER_ID` (`ANSWER_ID`),
  CONSTRAINT `question_answer_ibfk_1` FOREIGN KEY (`QUESTION_ID`) REFERENCES `questions` (`ID`),
  CONSTRAINT `question_answer_ibfk_2` FOREIGN KEY (`ANSWER_ID`) REFERENCES `answers` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_answer`
--


/*!40000 ALTER TABLE `question_answer` DISABLE KEYS */;
LOCK TABLES `question_answer` WRITE;
INSERT INTO `question_answer` VALUES (1,1,16,0),(2,1,17,0),(3,1,18,1),(4,1,19,0),(5,2,20,0),(6,2,21,1),(7,2,22,0),(8,5,23,0),(9,5,24,0),(10,6,23,1),(11,6,24,0),(12,7,23,1),(13,4,25,1),(14,4,26,1),(15,4,27,1),(16,4,28,0),(17,7,24,0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `question_answer` ENABLE KEYS */;

--
-- Table structure for table `questiongroups`
--

DROP TABLE IF EXISTS `questiongroups`;
CREATE TABLE `questiongroups` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` text,
  `ENABLED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `questiongroups`
--


/*!40000 ALTER TABLE `questiongroups` DISABLE KEYS */;
LOCK TABLES `questiongroups` WRITE;
INSERT INTO `questiongroups` VALUES (1,'IT тематика',1),(2,'Автомобили',1),(3,'Исторические',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `questiongroups` ENABLE KEYS */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION_TEXT` text,
  `RESPONSE_TYPE` int(11) DEFAULT NULL,
  `QUESTION_GROUP` int(11) DEFAULT NULL,
  `QUESTION_PARENT_ID` int(11) DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `QUESTION_GROUP` (`QUESTION_GROUP`),
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`QUESTION_GROUP`) REFERENCES `questiongroups` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `questions`
--


/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
LOCK TABLES `questions` WRITE;
INSERT INTO `questions` VALUES (1,'Когда началась Вторая Мировая Война',1,3,NULL,1),(2,'Кто глава кампании Microsoft',1,1,NULL,1),(3,'На эмблеме какой марки автомобиля изображена лошадь',1,2,NULL,1),(4,'Какой язык программирования поддерживает работу с XML',2,1,NULL,1),(5,'Верны ли утверждения',3,3,NULL,1),(6,'Солнце светит ярко',4,3,5,1),(7,'Вода мокрая',4,3,5,1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;

--
-- Table structure for table `useranswers`
--

DROP TABLE IF EXISTS `useranswers`;
CREATE TABLE `useranswers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_RESULT_ID` int(11) DEFAULT NULL,
  `QUESTION_ID` int(11) DEFAULT NULL,
  `ANSWER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_RESULT_ID` (`USER_RESULT_ID`),
  KEY `QUESTION_ID` (`QUESTION_ID`),
  KEY `ANSWER_ID` (`ANSWER_ID`),
  CONSTRAINT `useranswers_ibfk_1` FOREIGN KEY (`USER_RESULT_ID`) REFERENCES `userresult` (`ID`),
  CONSTRAINT `useranswers_ibfk_2` FOREIGN KEY (`QUESTION_ID`) REFERENCES `questions` (`ID`),
  CONSTRAINT `useranswers_ibfk_3` FOREIGN KEY (`ANSWER_ID`) REFERENCES `answers` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `useranswers`
--


/*!40000 ALTER TABLE `useranswers` DISABLE KEYS */;
LOCK TABLES `useranswers` WRITE;
INSERT INTO `useranswers` VALUES (1,14,1,18),(2,14,5,25),(3,14,5,26),(4,14,5,27),(5,14,5,28),(6,14,4,25),(7,14,4,26),(8,14,4,27),(9,14,4,25),(10,14,4,26),(11,14,4,27),(12,15,6,21),(13,15,6,23),(14,15,4,25),(15,15,4,26),(16,15,4,25),(17,15,4,26),(18,15,4,27),(19,15,4,25),(20,15,4,26),(21,15,4,27),(22,15,4,25),(23,15,4,26),(24,15,4,27),(25,15,4,25),(26,15,4,26),(27,15,4,27);
UNLOCK TABLES;
/*!40000 ALTER TABLE `useranswers` ENABLE KEYS */;

--
-- Table structure for table `userresult`
--

DROP TABLE IF EXISTS `userresult`;
CREATE TABLE `userresult` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` text CHARACTER SET latin1,
  `COMP_IP` text CHARACTER SET latin1,
  `START_TIME` bigint(20) DEFAULT NULL,
  `END_TIME` bigint(20) DEFAULT NULL,
  `RESULT` int(11) DEFAULT NULL,
  `SESSION_ID` text CHARACTER SET latin1,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userresult`
--


/*!40000 ALTER TABLE `userresult` DISABLE KEYS */;
LOCK TABLES `userresult` WRITE;
INSERT INTO `userresult` VALUES (1,'Vasiliy','192.168.0.1',1356000015168,0,2,'F6VRX2L55CYK9RY391P4'),(2,'Serg','127.0.0.1',1356086783907,0,3,'471ZV0G6N508MNH0V194'),(3,'Serg','127.0.0.1',1356087183077,0,2,'804IO8G3IKC62ZRXAQG2'),(4,'Vasiliy pupkin','127.0.0.1',1356087248397,0,2,'9U7M97T7948HYE419T9R'),(5,'Jonny','127.0.0.1',1356102429868,0,2,'E6A3M2I6552RG417JE6L'),(6,'Jonny','127.0.0.1',1356102565790,0,2,'54W71A7JZ67V8B9A30DH'),(7,'Jonny','127.0.0.1',1356102635388,0,2,'B93M7ZO8T69K3YYI0OMN'),(8,'Jonny','127.0.0.1',1356102790907,0,2,'W84FJP528T08Y9N6LEKW'),(9,'Sanechek','127.0.0.1',1356103018561,0,2,'87Z388B2D13514Z99F23'),(10,'Sanec','127.0.0.1',1356104349544,0,2,'ZHM4948466I0BVXHWL0J'),(11,'Sanec','127.0.0.1',1356343063570,1356446375514,2,'X36UQX5P0G0Z33Q00IM8'),(12,'Serg','127.0.0.1',1356347911592,0,2,'HM827144UCT02PZPSJ6R'),(13,'Denchik','192.168.0.2',1356432749257,1356432788701,2,'OESJ3F32RYYAEYJC8C2I'),(14,'Serg','127.0.0.1',1356444471856,1356535672206,3,'7649U568SC2G87HJ0CRS'),(15,'Daniels','127.0.0.1',1356449540525,1356534164235,2,'IIAODK5G0C07WMVO2TF1'),(16,'Daniels','127.0.0.1',1356449584450,1356535672251,3,'8832F70NJD39313Q122D'),(17,'Jonson','127.0.0.1',1356509919350,1356535672276,3,'61973E6Y4D05P9SV4FV5'),(18,'Jackson','127.0.0.1',1356509941296,1356535672291,3,'2JYA75M6UFHD0DVEV853'),(19,'Serg','127.0.0.1',1356515946434,1356535672306,3,'66443LB455YS5JFQXYAO'),(20,'dfjkghfdjg','127.0.0.1',1356519165292,1356535672326,3,'GFB0V9XS1H4126GIYT04'),(21,'','127.0.0.1',1356523987098,1356535672341,3,'8KRRABHDLQN0CFW243C8'),(22,'Tester','127.0.0.1',1356527570790,1356535672366,3,'HR25JZRV16420039794U'),(23,'Tester','127.0.0.1',1356527662726,1356535672376,3,'B171C9R80GLVW409Q9R8'),(24,'Daniels','127.0.0.1',1356527719180,1356535672391,3,'2149OXD658B84PAJO4DM'),(25,'Daniels','127.0.0.1',1356527800613,1356535672401,3,'33C2A6MW8ACO3YC8YSZ9'),(26,'Ligalize','127.0.0.1',1356527930597,1356535672416,3,'XWRMRR59Q2Q714232K6W'),(27,'Jade','127.0.0.1',1356535720991,1356616783639,3,'G580D1MD4KF459G023WU'),(28,'Stieve','127.0.0.1',1356616773478,1356620384653,3,'3KBB739B974XN1369V01'),(29,'Alex','127.0.0.1',1356621264159,0,1,'45WYW50V4A50X43K47U1');
UNLOCK TABLES;
/*!40000 ALTER TABLE `userresult` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


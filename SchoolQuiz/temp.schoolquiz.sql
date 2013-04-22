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
  `QUESTION_ID` int(11) DEFAULT NULL,
  `ANSWER_TEXT` text CHARACTER SET latin1,
  `RIGHT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `QUESTION_ID` (`QUESTION_ID`),
  CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`QUESTION_ID`) REFERENCES `questions` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `answers`
--


/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
LOCK TABLES `answers` WRITE;
INSERT INTO `answers` VALUES (1,1,'1940',0),(2,1,'1939',1),(3,1,'1941',0),(4,1,'1938',0),(5,2,'Bill Geits',1),(6,2,'Tom Brooks',0),(7,2,'Steave Djobs',0),(8,3,'Lamborgini',0),(9,3,'Lancia',0),(10,3,'Ford',0),(11,3,'Ferrari',1),(12,4,'Java',1),(13,4,'ActionScript3',1),(14,4,'C++',1),(15,4,'Assembler',0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION_TEXT` text CHARACTER SET latin1,
  `RESPONSE_TYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=cp1251 COLLATE=cp1251_ukrainian_ci;

--
-- Dumping data for table `questions`
--


/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
LOCK TABLES `questions` WRITE;
INSERT INTO `questions` VALUES (1,'When the Second Great War Started',1),(2,'Who is the Head of company Microsoft',1),(3,'What car has the emblem of Horse on the CompanySign',1),(4,'What language of programming supports the XML fileParsing',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `useranswers`
--


/*!40000 ALTER TABLE `useranswers` DISABLE KEYS */;
LOCK TABLES `useranswers` WRITE;
INSERT INTO `useranswers` VALUES (1,1,1,2),(2,1,2,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userresult`
--


/*!40000 ALTER TABLE `userresult` DISABLE KEYS */;
LOCK TABLES `userresult` WRITE;
INSERT INTO `userresult` VALUES (1,'Vasiliy','192.168.0.1',1356000015168,0,1,'F6VRX2L55CYK9RY391P4'),(2,'Serg','127.0.0.1',1356086783907,0,3,'471ZV0G6N508MNH0V194'),(3,'Serg','127.0.0.1',1356087183077,0,1,'804IO8G3IKC62ZRXAQG2'),(4,'Vasiliy pupkin','127.0.0.1',1356087248397,0,1,'9U7M97T7948HYE419T9R'),(5,'Jonny','127.0.0.1',1356102429868,0,1,'E6A3M2I6552RG417JE6L'),(6,'Jonny','127.0.0.1',1356102565790,0,1,'54W71A7JZ67V8B9A30DH'),(7,'Jonny','127.0.0.1',1356102635388,0,1,'B93M7ZO8T69K3YYI0OMN'),(8,'Jonny','127.0.0.1',1356102790907,0,1,'W84FJP528T08Y9N6LEKW'),(9,'Sanechek','127.0.0.1',1356103018561,0,1,'87Z388B2D13514Z99F23'),(10,'Sanec','127.0.0.1',1356104349544,0,1,'ZHM4948466I0BVXHWL0J');
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


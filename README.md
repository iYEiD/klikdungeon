# klikdungeon
Object Oriented Programming II Project, Using JAVAFX, MYSQL

"I don't own any of the sound assets or monster images,
Monsters: OLD SCHOOL RUNESCAPE
Sounds: Minecraft Mobs"
//////////////////////////////////////////////////////////
In order to play/test the game use fix the schema as passed in the databasemanager
and use these queries to generate the tables

CREATE TABLE `player` (
   `idplayer` int NOT NULL AUTO_INCREMENT,
   `name` varchar(45) NOT NULL,
   `password` varchar(45) NOT NULL,
   `level` varchar(45) NOT NULL,
   `gold` varchar(45) NOT NULL,
   PRIMARY KEY (`idplayer`)
 ) 

CREATE TABLE `monster` (
   `idmonster` int NOT NULL AUTO_INCREMENT,
   `name` varchar(100) NOT NULL,
   `dialogue` varchar(250) NOT NULL,
   `health` int NOT NULL,
   `imagePath` varchar(1000) NOT NULL,
   `deathPath` varchar(1000) NOT NULL,
   PRIMARY KEY (`idmonster`)
 ) 
 CREATE TABLE `weapon` (
   `idweapon` int NOT NULL AUTO_INCREMENT,
   `name` varchar(50) NOT NULL,
   `damage` int NOT NULL,
   `cost` int NOT NULL,
   PRIMARY KEY (`idweapon`)
 ) 

 CREATE TABLE `inventory` (
   `idinventory` int NOT NULL AUTO_INCREMENT,
   `idplayer` int DEFAULT NULL,
   `idweapon` int DEFAULT NULL,
   PRIMARY KEY (`idinventory`),
   KEY `idplayer` (`idplayer`),
   KEY `idweapon` (`idweapon`),
   CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`idplayer`) REFERENCES `player` (`idplayer`),
   CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`idweapon`) REFERENCES `weapon` (`idweapon`)
 ) 

 //////////////////////////////////////////////////////

 Have Fun! :D
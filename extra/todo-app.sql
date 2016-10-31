-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 31, 2016 at 08:32 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.6.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `todo-app`
--
CREATE DATABASE IF NOT EXISTS `todo-app` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `todo-app`;

-- --------------------------------------------------------

--
-- Table structure for table `notes`
--
-- Creation: Oct 31, 2016 at 04:24 PM
--

DROP TABLE IF EXISTS `notes`;
CREATE TABLE IF NOT EXISTS `notes` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `notes`:
--

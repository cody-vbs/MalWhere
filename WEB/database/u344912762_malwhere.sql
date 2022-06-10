-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jun 10, 2022 at 06:32 AM
-- Server version: 10.5.12-MariaDB-cll-lve
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u344912762_malwhere`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(10) UNSIGNED NOT NULL,
  `uname` varchar(50) NOT NULL,
  `pass` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `uname`, `pass`) VALUES
(1, 'admin', '20be459727e35f01ad0e228a2aa9579d');

-- --------------------------------------------------------

--
-- Table structure for table `scan_logs`
--

CREATE TABLE `scan_logs` (
  `id` int(10) UNSIGNED NOT NULL,
  `user` varchar(20) NOT NULL,
  `scan_result` varchar(10) NOT NULL,
  `timestamp` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `scan_logs`
--

INSERT INTO `scan_logs` (`id`, `user`, `scan_result`, `timestamp`) VALUES
(1, 'syd12@gmail.com', 'Malicious', '2022-03-04'),
(2, 'syd1234@gmail.com', 'Malicious', '2022-03-05'),
(3, 'gues1224', 'Benign', '2022-03-12'),
(4, 'gues1224', 'Benign', '2022-03-12'),
(5, '', 'Benign', '2022-04-08'),
(6, 'guest39852', 'Benign', '2022-04-08'),
(7, 'guest39852', 'Benign', '2022-04-08'),
(8, 'Syd Ph', 'Benign', '2022-04-08'),
(9, '', 'Benign', '2022-04-12'),
(10, '', 'Benign', '2022-04-12'),
(11, '', 'Benign', '2022-04-12'),
(12, '', 'Benign', '2022-04-12'),
(13, '', 'Benign', '2022-04-12'),
(14, '', 'Benign', '2022-04-12'),
(15, '', 'Benign', '2022-04-12'),
(16, '', 'Benign', '2022-04-12'),
(17, '', 'Benign', '2022-04-12'),
(18, '', 'Benign', '2022-04-12'),
(19, '', 'Benign', '2022-04-12'),
(20, '', 'Benign', '2022-04-12'),
(21, 'guest07344', 'Benign', '2022-04-12'),
(22, '', 'Malicious', '2022-05-17'),
(23, '', 'Benign', '2022-05-17'),
(24, '', 'Benign', '2022-05-17'),
(25, '', 'Malicious', '2022-05-17'),
(26, '', 'Malicious', '2022-05-17'),
(27, 'Ms Liezel', 'Malicious', '2022-05-17'),
(28, 'Shayne Vir Dahan', 'Malicious', '2022-05-17'),
(29, 'Ms Liezel', 'Malicious', '2022-05-17'),
(30, '', 'Benign', '2022-05-17'),
(31, 'Shayne Dahan', 'Malicious', '2022-05-17'),
(32, '', 'Malicious', '2022-05-22'),
(33, '', 'Benign', '2022-05-19'),
(34, '', 'Benign', '2022-05-19'),
(35, 'Jose Emmanuel Ang', 'Benign', '2022-05-19'),
(36, 'Jose Emmanuel Ang', 'Benign', '2022-05-19'),
(37, 'Jose Emmanuel Ang', 'Benign', '2022-05-19'),
(38, 'Ms Liezel', 'Malicious', '2022-05-19'),
(39, 'Ms Liezel', 'Benign', '2022-05-19'),
(40, 'Ms Liezel', 'Malicious', '2022-05-19'),
(41, 'Ms Liezel', 'Malicious', '2022-05-19'),
(42, 'Ms Liezel', 'Benign', '2022-05-19'),
(43, 'Ms Liezel', 'Malicious', '2022-05-19'),
(44, 'Ms Liezel', 'Benign', '2022-05-19'),
(45, 'Ms Liezel', 'Benign', '2022-05-19'),
(46, 'Ms Liezel', 'Benign', '2022-05-19'),
(47, 'Ms Liezel', 'Benign', '2022-05-19'),
(48, 'Ms Liezel', 'Benign', '2022-05-19'),
(49, 'Ms Liezel', 'Malicious', '2022-05-19'),
(50, 'Ms Liezel', 'Malicious', '2022-05-19'),
(51, 'Ms Liezel', 'Malicious', '2022-05-19'),
(52, '', 'Malicious', '2022-05-19'),
(53, '', 'Malicious', '2022-05-20'),
(54, '', 'Benign', '2022-05-21'),
(55, '', 'Benign', '2022-05-21'),
(56, '', 'Benign', '2022-05-21'),
(57, '', 'Malicious', '2022-05-21'),
(58, '', 'Benign', '2022-05-21'),
(59, '', 'Benign', '2022-05-21'),
(60, '', 'Benign', '2022-05-21'),
(61, '', 'Benign', '2022-05-21'),
(62, '', 'Benign', '2022-05-21'),
(63, '', 'Benign', '2022-05-21'),
(64, '', 'Benign', '2022-05-21'),
(65, '', 'Malicious', '2022-05-21'),
(66, '', 'Malicious', '2022-05-21'),
(67, '', 'Malicious', '2022-05-21'),
(68, '', 'Malicious', '2022-05-21'),
(69, '', 'Malicious', '2022-05-21'),
(70, '', 'Benign', '2022-05-21'),
(71, '', 'Benign', '2022-05-22'),
(72, '', 'Benign', '2022-05-22'),
(73, '', 'Benign', '2022-05-22'),
(74, '', 'Benign', '2022-05-22'),
(75, '', 'Benign', '2022-05-22'),
(76, '', 'Benign', '2022-05-22'),
(77, '', 'Benign', '2022-05-22'),
(78, '', 'Benign', '2022-05-22'),
(79, '', 'Benign', '2022-05-22'),
(80, '', 'Benign', '2022-05-23'),
(81, '', 'Benign', '2022-05-23'),
(82, '', 'Malicious', '2022-05-23'),
(83, '', 'Malicious', '2022-05-23'),
(84, '', 'Malicious', '2022-05-23'),
(85, '', 'Malicious', '2022-05-23'),
(86, '', 'Benign', '2022-05-24'),
(87, '', 'Malicious', '2022-05-24'),
(88, 'Syd Ph', 'Malicious', '2022-05-24'),
(89, 'Syd Ph', 'Malicious', '2022-05-24'),
(90, 'Syd Ph', 'Malicious', '2022-05-24'),
(91, 'Syd Ph', 'Benign', '2022-05-24'),
(92, 'Shayne Vir Dahan', 'Malicious', '2022-05-25'),
(93, 'Shayne Vir Dahan', 'Malicious', '2022-05-25'),
(94, '', 'Malicious', '2022-05-25'),
(95, '', 'Malicious', '2022-05-25'),
(96, '', 'Benign', '2022-05-25'),
(97, '', 'Benign', '2022-05-25'),
(98, '', 'Benign', '2022-05-25'),
(99, '', 'Benign', '2022-05-25'),
(100, '', 'Benign', '2022-05-25'),
(101, '', 'Benign', '2022-05-25'),
(102, '', 'Malicious', '2022-05-25'),
(103, '', 'Benign', '2022-05-25'),
(104, '', 'Malicious', '2022-05-25'),
(105, '', 'Malicious', '2022-05-25'),
(106, '', 'Malicious', '2022-05-25'),
(107, '', 'Benign', '2022-05-25'),
(108, '', 'Malicious', '2022-05-25'),
(109, '', 'Malicious', '2022-05-25'),
(110, '', 'Malicious', '2022-05-25'),
(111, '', 'Malicious', '2022-05-25'),
(112, '', 'Malicious', '2022-05-25'),
(113, '', 'Malicious', '2022-05-25'),
(114, '', 'Malicious', '2022-05-25'),
(115, '', 'Malicious', '2022-05-25'),
(116, '', 'Malicious', '2022-05-25'),
(117, '', 'Malicious', '2022-05-25'),
(118, '', 'Malicious', '2022-05-25'),
(119, '', 'Malicious', '2022-05-25'),
(120, '', 'Malicious', '2022-05-25'),
(121, '', 'Malicious', '2022-05-25'),
(122, '', 'Malicious', '2022-05-25'),
(123, '', 'Malicious', '2022-05-25'),
(124, '', 'Malicious', '2022-05-25'),
(125, '', 'Malicious', '2022-05-25'),
(126, '', 'Malicious', '2022-05-25'),
(127, '', 'Malicious', '2022-05-25'),
(128, '', 'Malicious', '2022-05-25'),
(129, '', 'Malicious', '2022-05-25'),
(130, '', 'Benign', '2022-05-25'),
(131, '', 'Benign', '2022-05-25'),
(132, '', 'Benign', '2022-05-25'),
(133, '', 'Malicious', '2022-05-25'),
(134, '', 'Malicious', '2022-05-25'),
(135, '', 'Malicious', '2022-05-25'),
(136, '', 'Malicious', '2022-05-25'),
(137, '', 'Malicious', '2022-05-25'),
(138, '', 'Malicious', '2022-05-25'),
(139, '', 'Malicious', '2022-05-25'),
(140, '', 'Malicious', '2022-05-25'),
(141, '', 'Malicious', '2022-05-25'),
(142, '', 'Malicious', '2022-05-25'),
(143, '', 'Benign', '2022-05-25'),
(144, '', 'Benign', '2022-05-25'),
(145, '', 'Benign', '2022-05-25'),
(146, '', 'Benign', '2022-05-25'),
(147, '', 'Benign', '2022-05-25'),
(148, '', 'Benign', '2022-05-25'),
(149, '', 'Benign', '2022-05-25'),
(150, '', 'Benign', '2022-05-25'),
(151, '', 'Benign', '2022-05-25'),
(152, '', 'Malicious', '2022-05-25'),
(153, '', 'Malicious', '2022-05-25'),
(154, '', 'Benign', '2022-05-25'),
(155, '', 'Malicious', '2022-05-25'),
(156, '', 'Benign', '2022-05-26'),
(157, '', 'Malicious', '2022-05-26'),
(158, '', 'Malicious', '2022-05-26'),
(159, '', 'Malicious', '2022-05-26'),
(160, '', 'Malicious', '2022-05-26'),
(161, '', 'Malicious', '2022-05-26'),
(162, '', 'Benign', '2022-05-26'),
(163, '', 'Benign', '2022-05-26'),
(164, '', 'Benign', '2022-05-26'),
(165, '', 'Benign', '2022-05-26'),
(166, '', 'Benign', '2022-05-26'),
(167, '', 'Malicious', '2022-05-26'),
(168, '', 'Benign', '2022-05-26'),
(169, '', 'Malicious', '2022-05-27'),
(170, '', 'Benign', '2022-05-27'),
(171, '', 'Malicious', '2022-05-27'),
(172, '', 'Malicious', '2022-05-27'),
(173, '', 'Malicious', '2022-05-27'),
(174, '', 'Malicious', '2022-05-27'),
(175, '', 'Malicious', '2022-05-27'),
(176, '', 'Benign', '2022-05-27'),
(177, '', 'Benign', '2022-05-27'),
(178, '', 'Benign', '2022-05-27'),
(179, '', 'Benign', '2022-05-27'),
(180, '', 'Benign', '2022-05-27'),
(181, '', 'Malicious', '2022-05-28'),
(182, '', 'Benign', '2022-05-28'),
(183, '', 'Malicious', '2022-05-30'),
(184, '', 'Benign', '2022-05-31'),
(185, '', 'Benign', '2022-05-31'),
(186, '', 'Malicious', '2022-05-31'),
(187, '', 'Benign', '2022-06-01'),
(188, 'Syd Ph', 'Malicious', '2022-06-01'),
(189, 'Syd Ph', 'Malicious', '2022-06-01'),
(190, 'Syd Ph', 'Benign', '2022-06-01'),
(191, 'Syd Ph', 'Benign', '2022-06-01'),
(192, 'Syd Ph', 'Malicious', '2022-06-01'),
(193, 'Syd Ph', 'Malicious', '2022-06-01'),
(194, 'Syd Ph', 'Malicious', '2022-06-01'),
(195, 'Syd Ph', 'Benign', '2022-06-01'),
(196, 'Syd Ph', 'Benign', '2022-06-01'),
(197, '', 'Benign', '2022-06-02'),
(198, '', 'Malicious', '2022-06-02'),
(199, '', 'Malicious', '2022-06-02'),
(200, '', 'Malicious', '2022-06-02'),
(201, '', 'Malicious', '2022-06-02'),
(202, '', 'Malicious', '2022-06-02'),
(203, '', 'Malicious', '2022-06-02'),
(204, '', 'Benign', '2022-06-02'),
(205, '', 'Malicious', '2022-06-02'),
(206, '', 'Benign', '2022-06-02'),
(207, '', 'Benign', '2022-06-02'),
(208, '', 'Benign', '2022-06-02'),
(209, '', 'Benign', '2022-06-02'),
(210, '', 'Benign', '2022-06-02'),
(211, '', 'Benign', '2022-06-02'),
(212, '', 'Malicious', '2022-06-02'),
(213, '', 'Benign', '2022-06-02'),
(214, '', 'Benign', '2022-06-02'),
(215, '', 'Benign', '2022-06-02'),
(216, '', 'Benign', '2022-06-03'),
(217, '', 'Benign', '2022-06-03'),
(218, '', 'Malicious', '2022-06-03'),
(219, '', 'Malicious', '2022-06-03'),
(220, '', 'Benign', '2022-06-03'),
(221, '', 'Benign', '2022-06-03'),
(222, '', 'Malicious', '2022-06-03'),
(223, '', 'Benign', '2022-06-03'),
(224, '', 'Malicious', '2022-06-03'),
(225, '', 'Malicious', '2022-06-03'),
(226, '', 'Benign', '2022-06-03'),
(227, '', 'Benign', '2022-06-03'),
(228, '', 'Benign', '2022-06-03'),
(229, '', 'Benign', '2022-06-03'),
(230, '', 'Benign', '2022-06-03'),
(231, '', 'Benign', '2022-06-03'),
(232, '', 'Benign', '2022-06-03'),
(233, '', 'Malicious', '2022-06-03'),
(234, '', 'Malicious', '2022-06-03'),
(235, '', 'Malicious', '2022-06-03'),
(236, '', 'Malicious', '2022-06-03'),
(237, '', 'Benign', '2022-06-03'),
(238, '', 'Benign', '2022-06-03'),
(239, '', 'Benign', '2022-06-03'),
(240, '', 'Benign', '2022-06-03'),
(241, '', 'Benign', '2022-06-04'),
(242, '', 'Benign', '2022-06-04'),
(243, '', 'Malicious', '2022-06-04'),
(244, '', 'Benign', '2022-06-04'),
(245, 'Syd Ph', 'Benign', '2022-06-04'),
(246, '', 'Malicious', '2022-06-04'),
(247, '', 'Malicious', '2022-06-04');

-- --------------------------------------------------------

--
-- Table structure for table `url_reports`
--

CREATE TABLE `url_reports` (
  `id` int(10) UNSIGNED NOT NULL,
  `caseNum` varchar(50) NOT NULL,
  `reporter_name` varchar(50) NOT NULL,
  `category_name` varchar(10) NOT NULL,
  `url_source` varchar(15) DEFAULT NULL,
  `status` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `imageurl` varchar(50) NOT NULL,
  `text_url` text NOT NULL,
  `lat` float(10,6) NOT NULL,
  `lng` float(10,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `url_reports`
--

INSERT INTO `url_reports` (`id`, `caseNum`, `reporter_name`, `category_name`, `url_source`, `status`, `email`, `imageurl`, `text_url`, `lat`, `lng`) VALUES
(1, '824922655973', 'guest76699', 'Phishing', 'Facebook', 'MALICIOUS', 'sydph77@gmail.com', 'https://malwhereapp.com/uploads/0.jpeg', 'https://urly.it/3mqzs', 7.243243, 125.615044),
(2, '918285784760', 'Syd Ph', 'Phishing', 'Facebook', 'MALICIOUS', 'sydph127655@gmail.com', 'https://malwhereapp.com/uploads/1.jpeg', 'https://zaxx.eu/lzYwb', 7.225225, 125.628189),
(3, '610087578298', 'guest48062', 'Phishing', 'Twitter', 'PENDING', 'sydph63@gmail.com', 'https://malwhereapp.com/uploads/2.jpeg', 'http://medcaritasphil04.atspace.co.uk/', 7.225225, 125.628189),
(4, '118756485971', 'Syd Ph', 'Benign', 'Facebook', 'MALICIOUS', 'sydph77@gmail.com', 'https://malwhereapp.com/uploads/3.jpeg', 'https://bit.Ily/3NaeFoS', 7.225225, 125.628189),
(5, '512108382598', '', 'Phishing', 'Facebook', 'MALICIOUS', '', 'https://malwhereapp.com/uploads/4.jpeg', 'https://drive.google.com/uc?export=download&confirm=no', 7.243243, 125.633209);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `scan_logs`
--
ALTER TABLE `scan_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `url_reports`
--
ALTER TABLE `url_reports`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `scan_logs`
--
ALTER TABLE `scan_logs`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=248;

--
-- AUTO_INCREMENT for table `url_reports`
--
ALTER TABLE `url_reports`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

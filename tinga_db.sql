-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 26, 2019 at 03:33 PM
-- Server version: 5.7.26-0ubuntu0.16.04.1
-- PHP Version: 7.2.14-1+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tinga_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_address`
--

CREATE TABLE `tbl_address` (
  `id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `address` longtext NOT NULL,
  `location_lat` varchar(30) NOT NULL,
  `location_long` varchar(30) NOT NULL,
  `uid` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `tbl_delivery`
--

CREATE TABLE `tbl_delivery` (
  `delivery_id` int(3) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  `fname` varchar(20) DEFAULT NULL,
  `lname` varchar(20) DEFAULT NULL,
  `aadhar_no` bigint(12) NOT NULL,
  `address` varchar(200) NOT NULL,
  `phone_number` varchar(13) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `rating` varchar(3) NOT NULL,
  `status` varchar(20) NOT NULL,
  `current_lat` varchar(10) NOT NULL,
  `current_long` varchar(10) NOT NULL,
  `image_code` longtext,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_food_items`
--

CREATE TABLE `tbl_food_items` (
  `id` bigint(8) NOT NULL,
  `food_id` varchar(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` bigint(4) NOT NULL,
  `food_desc` varchar(1000) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `typetag` varchar(15) NOT NULL,
  `subtype_tag` varchar(15) NOT NULL,
  `recommended` varchar(3) NOT NULL,
  `image_path` varchar(150) NOT NULL,
  `sid` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_food_items1`
--

CREATE TABLE `tbl_food_items1` (
  `id` bigint(8) NOT NULL,
  `food_id` varchar(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` bigint(4) NOT NULL,
  `food_desc` varchar(1000) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `typetag` varchar(15) NOT NULL,
  `subtype_tag` varchar(15) NOT NULL,
  `recommended` varchar(3) NOT NULL,
  `image_path` varchar(150) NOT NULL,
  `sid` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_images`
--

CREATE TABLE `tbl_images` (
  `id` int(6) NOT NULL,
  `image_path` varchar(300) NOT NULL,
  `table_tag` varchar(100) NOT NULL,
  `sid` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `tbl_order`
--

CREATE TABLE `tbl_order` (
  `order_id` int(11) NOT NULL,
  `total_price` bigint(5) NOT NULL,
  `order_date` date NOT NULL,
  `order_time` varchar(15) DEFAULT NULL,
  `customer_id` varchar(30) NOT NULL,
  `restaurant_id` varchar(12) NOT NULL,
  `delivery_id` int(10) DEFAULT NULL,
  `order_payment_mode` varchar(30) NOT NULL,
  `order_address` varchar(300) NOT NULL,
  `order_lat` varchar(10) DEFAULT NULL,
  `order_long` varchar(10) DEFAULT NULL,
  `order_delivery_time` varchar(50) DEFAULT NULL,
  `order_status` varchar(30) NOT NULL,
  `order_rating` varchar(5) DEFAULT NULL,
  `delivery_rating` varchar(5) DEFAULT NULL,
  `order_feedback` varchar(500) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `tbl_order_Items`
--

CREATE TABLE `tbl_order_Items` (
  `id` int(11) NOT NULL,
  `food_id` varchar(10) NOT NULL,
  `quantity` int(2) NOT NULL,
  `price` bigint(7) NOT NULL,
  `order_id` bigint(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `tbl_otp`
--

CREATE TABLE `tbl_otp` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone` varchar(12) NOT NULL,
  `otp` varchar(10) NOT NULL,
  `verified` tinyint(4) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `tbl_ratings`
--

CREATE TABLE `tbl_ratings` (
  `id` int(10) NOT NULL,
  `keyword` varchar(10) NOT NULL,
  `rating` varchar(3) NOT NULL,
  `s_id` varchar(10) NOT NULL,
  `rd_id` varchar(10) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurants`
--

CREATE TABLE `tbl_restaurants` (
  `restaurant_id` varchar(12) NOT NULL,
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  `image_path` longtext,
  `address` varchar(250) NOT NULL,
  `city` varchar(20) NOT NULL,
  `state` varchar(20) NOT NULL,
  `country` varchar(30) NOT NULL,
  `cuisine` varchar(350) DEFAULT NULL,
  `phone_number` bigint(13) NOT NULL,
  `timings` varchar(250) DEFAULT NULL,
  `rating` varchar(3) DEFAULT NULL,
  `status_tag` varchar(10) NOT NULL,
  `location_lat` varchar(15) NOT NULL,
  `location_long` varchar(15) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurant_feedback`
--

CREATE TABLE `tbl_restaurant_feedback` (
  `id` int(5) NOT NULL,
  `restaurant_id` varchar(12) NOT NULL,
  `rating` int(1) NOT NULL,
  `feedback` varchar(500) NOT NULL,
  `date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

CREATE TABLE `tbl_users` (
  `uid` varchar(30) NOT NULL,
  `id` int(3) NOT NULL,
  `fname` varchar(20) DEFAULT NULL,
  `lname` varchar(20) DEFAULT NULL,
  `phone_number` varchar(13) DEFAULT NULL,
  `mobile_verified` tinyint(1) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `access_role` varchar(10) NOT NULL,
  `image_code` longtext,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_modified_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users_log`
--

CREATE TABLE `tbl_users_log` (
  `id` int(3) NOT NULL,
  `uid` varchar(30) NOT NULL,
  `login_at` timestamp NULL DEFAULT NULL,
  `logout_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_address`
--
ALTER TABLE `tbl_address`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_delivery`
--
ALTER TABLE `tbl_delivery`
  ADD PRIMARY KEY (`delivery_id`),
  ADD UNIQUE KEY `id` (`delivery_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `phone_number` (`phone_number`);

--
-- Indexes for table `tbl_food_items`
--
ALTER TABLE `tbl_food_items`
  ADD PRIMARY KEY (`food_id`),
  ADD UNIQUE KEY `unique` (`id`),
  ADD KEY `sid` (`sid`);

--
-- Indexes for table `tbl_food_items1`
--
ALTER TABLE `tbl_food_items1`
  ADD PRIMARY KEY (`food_id`),
  ADD UNIQUE KEY `unique` (`id`),
  ADD KEY `sid` (`sid`);

--
-- Indexes for table `tbl_images`
--
ALTER TABLE `tbl_images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_order`
--
ALTER TABLE `tbl_order`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `tbl_order_Items`
--
ALTER TABLE `tbl_order_Items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_otp`
--
ALTER TABLE `tbl_otp`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_ratings`
--
ALTER TABLE `tbl_ratings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_restaurants`
--
ALTER TABLE `tbl_restaurants`
  ADD PRIMARY KEY (`restaurant_id`),
  ADD UNIQUE KEY `unique` (`id`);

--
-- Indexes for table `tbl_restaurant_feedback`
--
ALTER TABLE `tbl_restaurant_feedback`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_users`
--
ALTER TABLE `tbl_users`
  ADD PRIMARY KEY (`uid`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `phone_number` (`phone_number`);

--
-- Indexes for table `tbl_users_log`
--
ALTER TABLE `tbl_users_log`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_address`
--
ALTER TABLE `tbl_address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `tbl_delivery`
--
ALTER TABLE `tbl_delivery`
  MODIFY `delivery_id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `tbl_food_items`
--
ALTER TABLE `tbl_food_items`
  MODIFY `id` bigint(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `tbl_food_items1`
--
ALTER TABLE `tbl_food_items1`
  MODIFY `id` bigint(8) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_images`
--
ALTER TABLE `tbl_images`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `tbl_order`
--
ALTER TABLE `tbl_order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `tbl_order_Items`
--
ALTER TABLE `tbl_order_Items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125;
--
-- AUTO_INCREMENT for table `tbl_otp`
--
ALTER TABLE `tbl_otp`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;
--
-- AUTO_INCREMENT for table `tbl_ratings`
--
ALTER TABLE `tbl_ratings`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `tbl_restaurants`
--
ALTER TABLE `tbl_restaurants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbl_restaurant_feedback`
--
ALTER TABLE `tbl_restaurant_feedback`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_users`
--
ALTER TABLE `tbl_users`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
--
-- AUTO_INCREMENT for table `tbl_users_log`
--
ALTER TABLE `tbl_users_log`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=220;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

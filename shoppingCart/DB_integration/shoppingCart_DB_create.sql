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


-- shopping_cart のデータベース構造をダンプしています
CREATE DATABASE IF NOT EXISTS `shopping_cart` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci */;
USE `shopping_cart`;

--  テーブル shopping_cart.cart の構造をダンプしています
CREATE TABLE IF NOT EXISTS `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_price` int(11) DEFAULT NULL,
  `total_quantity` int(11) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `ins_dt` datetime DEFAULT NULL,
  `ins_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `upd_dt` datetime DEFAULT NULL,
  `upd_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9emlp6m95v5er2bcqkjsw48he` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル shopping_cart.cart: ~0 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` (`id`, `total_price`, `total_quantity`, `user_id`, `ins_dt`, `ins_by`, `upd_dt`, `upd_by`) VALUES
	(35, 0, 0, 40, '2024-11-22 22:28:05', NULL, NULL, NULL);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;

--  テーブル shopping_cart.cart_item の構造をダンプしています
CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `cart_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `ins_dt` datetime DEFAULT NULL,
  `ins_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `upd_dt` datetime DEFAULT NULL,
  `upd_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1uobyhgl1wvgt1jpccia8xxs3` (`cart_id`),
  KEY `FKqkqmvkmbtiaqn2nfqf25ymfs2` (`product_id`),
  CONSTRAINT `FK1uobyhgl1wvgt1jpccia8xxs3` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`),
  CONSTRAINT `FKqkqmvkmbtiaqn2nfqf25ymfs2` FOREIGN KEY (`product_id`) REFERENCES `t_products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル shopping_cart.cart_item: ~0 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;

--  テーブル shopping_cart.m_user の構造をダンプしています
CREATE TABLE IF NOT EXISTS `m_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `INS_DT` datetime DEFAULT NULL,
  `INS_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_BY` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル shopping_cart.m_user: ~1 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `m_user` DISABLE KEYS */;
INSERT INTO `m_user` (`ID`, `EMAIL`, `password`, `INS_DT`, `INS_BY`, `UPD_DT`, `UPD_BY`) VALUES
	(1, 'a@a.com', '$2a$10$HH6LOIxrXv987KhtEpHrleJbYruX49KH0hGYp0uS80RWN/1bCelUu', NULL, NULL, NULL, NULL),
	(40, 'aaaaa@gmail.com', '$2a$10$PNtQ9sUdpQvE4ouPHRnGw.Xy5aA1BPOhypkcvptMI1mPHbgPyP0f6', '2024-11-22 22:28:05', 'tttt', NULL, NULL);
/*!40000 ALTER TABLE `m_user` ENABLE KEYS */;

--  テーブル shopping_cart.t_orders の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` int(11) DEFAULT NULL,
  `session_id` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `url` varchar(1024) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `ins_dt` datetime DEFAULT NULL,
  `ins_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `upd_dt` datetime DEFAULT NULL,
  `upd_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル shopping_cart.t_orders: ~4 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_orders` DISABLE KEYS */;
INSERT INTO `t_orders` (`id`, `amount`, `session_id`, `status`, `url`, `user_id`, `ins_dt`, `ins_by`, `upd_dt`, `upd_by`) VALUES
	(1, 0, 'cs_test_a1MM5CbIWpKfeShSmcxH8DbZ8iq2A35GnEu5C8pKTSVufOIOgD2VTyhroU', 'paid', 'https://checkout.stripe.com/c/pay/cs_test_a1MM5CbIWpKfeShSmcxH8DbZ8iq2A35GnEu5C8pKTSVufOIOgD2VTyhroU#fidkdWxOYHwnPyd1blpxYHZxWjA0VEJgUl1CVVFDaFYzVk5UQF01STdnPWdEMlJccz1mZ3dTd0ByYlNsdmlvZG49NWJfQlZfYGpncDNzfGBjRmZBdE1VbH89NG90aDY3cVRoUEd0YUFBVkRcNTVhdUNCQl1LVycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 6, '2024-11-02 19:46:08', NULL, NULL, NULL),
	(2, 0, 'cs_test_a1WVhSFAtDwcYXa7Ppavays5mFHpuyUCbVoWJkyF7oUKflEb3x1Kkizgu0', 'unpaid', 'https://checkout.stripe.com/c/pay/cs_test_a1WVhSFAtDwcYXa7Ppavays5mFHpuyUCbVoWJkyF7oUKflEb3x1Kkizgu0#fidkdWxOYHwnPyd1blpxYHZxWjA0VEJgUl1CVVFDaFYzVk5UQF01STdnPWdEMlJccz1mZ3dTd0ByYlNsdmlvZG49NWJfQlZfYGpncDNzfGBjRmZBdE1VbH89NG90aDY3cVRoUEd0YUFBVkRcNTVhdUNCQl1LVycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 6, '2024-11-02 19:57:53', NULL, NULL, NULL),
	(3, 590, 'cs_test_a1EzL59YCcvqFwxWb33kODChAkiZg3VjtMD9RjcDi0ar94gvpSekXWd1ro', 'paid', 'https://checkout.stripe.com/c/pay/cs_test_a1EzL59YCcvqFwxWb33kODChAkiZg3VjtMD9RjcDi0ar94gvpSekXWd1ro#fidkdWxOYHwnPyd1blpxYHZxWjA0VEJgUl1CVVFDaFYzVk5UQF01STdnPWdEMlJccz1mZ3dTd0ByYlNsdmlvZG49NWJfQlZfYGpncDNzfGBjRmZBdE1VbH89NG90aDY3cVRoUEd0YUFBVkRcNTVhdUNCQl1LVycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 9, '2024-11-03 12:44:00', NULL, NULL, NULL),
	(4, 297, 'cs_test_a1Z6hrwZ0xcyI89j2og74LAoet9U2datpvUbWVojGUDNIPuigZmyHMwA4f', 'unpaid', 'https://checkout.stripe.com/c/pay/cs_test_a1Z6hrwZ0xcyI89j2og74LAoet9U2datpvUbWVojGUDNIPuigZmyHMwA4f#fidkdWxOYHwnPyd1blpxYHZxWjA0VEJgUl1CVVFDaFYzVk5UQF01STdnPWdEMlJccz1mZ3dTd0ByYlNsdmlvZG49NWJfQlZfYGpncDNzfGBjRmZBdE1VbH89NG90aDY3cVRoUEd0YUFBVkRcNTVhdUNCQl1LVycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 9, '2024-11-03 13:01:19', NULL, NULL, NULL);
/*!40000 ALTER TABLE `t_orders` ENABLE KEYS */;

--  テーブル shopping_cart.t_products の構造をダンプしています
CREATE TABLE IF NOT EXISTS `t_products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `ins_dt` datetime DEFAULT NULL,
  `ins_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `upd_dt` datetime DEFAULT NULL,
  `upd_by` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- テーブル shopping_cart.t_products: ~16 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `t_products` DISABLE KEYS */;
INSERT INTO `t_products` (`id`, `category`, `description`, `image`, `name`, `price`, `ins_dt`, `ins_by`, `upd_dt`, `upd_by`) VALUES
	(2, 'Bags', 'The words on bag Paimon is my Emergency food. If you are a fan of paimon, you will love this bag. It would be a great addition to your fan collection. Make a funny gift.', 'https://cdn.pixabay.com/photo/2020/10/17/03/55/woman-5661078_1280.png', 'Anime Inspired Novelty Bag Paimon is my Emergency Food Traveller Gift Anime Lover Gift', 118, '2024-11-02 18:46:52', NULL, NULL, NULL),
	(3, 'Bags', 'The words on bag Paimon is my Emergency food. If you are a fan of paimon, you will love this bag. It would be a great addition to your fan collection. Make a funny gift.', 'https://cdn.pixabay.com/photo/2020/10/17/03/55/woman-5661078_1280.png', 'Anime Inspired Novelty Bag Paimon is my Emergency Food Traveller Gift Anime Lover Gift', 118, '2024-11-02 18:54:21', NULL, NULL, NULL),
	(4, 'Headset', 'Surrounding Stereo Subwoofer Clear sound operating strong brass', 'https://cdn.pixabay.com/photo/2015/09/09/20/30/headphones-933157_1280.jpg', 'Stereo Gaming Headset, Noise Cancelling Over Ear Headphones with Mic, LED Light, Bass Surround, Soft Memory Earmuffs', 219, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(5, 'Mice', 'Pentakill, 5 DPI Levels - Geared with 5 redefinable DPI levels (default as: 500/1000/2000/3000/4000)', 'https://cdn.pixabay.com/photo/2022/08/14/16/39/mouse-7386247_1280.jpg', 'RGB Gaming Mouse, 8000 DPI Wired Optical Gamer Mouse with 11 Programmable Buttons & 5 Backlit Modes, Software Supports DIY Keybinds Rapid Fire Button', 199, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(6, 'Chair', 'Most Comfortable And Relaxing: Equipped with headrest and lumbar pillow.', 'https://images.pexels.com/photos/7862491/pexels-photo-7862491.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 'Gaming Chair, Computer Chair with Footrest and Lumbar Support, Height Adjustable Game Chair with 360°-Swivel Seat and Headrest and for Office or Gaming', 1899, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(7, 'Dinnerware', '18-PIECE SET: Includes (6) 10-1/4-inch dinner plates, (6) 6-3/4-inch appetizer plates and (6) 18-oz soup/cereal bowls. This set has everything you need for a full table service of 6, and it comes with the classic Corelle style.', 'https://cdn.pixabay.com/photo/2014/11/11/10/22/plate-526603_1280.jpg', '18-Piece Service for 6 Dinnerware Set, Triple Layer Glass and Chip Resistant, Lightweight Round Plates and Bowls Set, Country Cottage', 699, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(8, 'Light', 'Night Lights Plug into Wall : 0.5 watts, 50 lumen, 3000k warm white Night Light. 4pcs long-life LED, if lights up to 7hrs each day, only 0.7 Kwh per year.', 'https://cdn.pixabay.com/photo/2022/11/08/08/52/lamp-7578025_1280.jpg', 'Night Light, Night Lights Plug into Wall 4-Pack, Nightlight Plug in Night Light, Dusk to Dawn Night Lamp Led Night Light for Kids Bedroom, Bathroom, Hallway Warm White', 99, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(9, 'Shoe', 'Memory Foam Insole: The memory foam insole is comfortable to touch,absorbs the impact force in motion, reduces the burden on the body. It feels like you are walking on the clouds.', 'https://cdn.pixabay.com/photo/2014/06/18/18/42/running-shoe-371625_1280.jpg', 'Men\'s Non Slip Running Shoes Ultra Light Breathable Casual Walking Shoes Fashion Sneakers Mesh Workout Sports Shoes', 199, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(10, 'Jean', 'For long days on the job, you want a work jean that keeps you comfortable. Constructed with a gusseted crotch and improved fit in the seat, thigh, and knee, this jean gives greater range of motion.', 'https://images.pexels.com/photos/603022/pexels-photo-603022.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 'Mens Riggs Workwear Advanced Comfort Five Pocket Jeans', 217, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(11, 'Headset', 'Large capacity Long battery life, built-in large-capacity lithium battery, long standby time, 6 hours of talk on a single charge.', 'https://images.pexels.com/photos/205926/pexels-photo-205926.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 'Wireless Bluetooth Headphones Over Ear,5.0 Foldable Gaming Wireless Headset Noise Cancelling Earphones Built-in Mic Macaron Bass Call Headset Hi-Fi Stereo Headsets Earbuds for Cell Phone PC Laptop', 99, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(12, 'Keyboard', 'Vibrant Color Multimedia Screen: The DIY multimedia display screen design in the upper right corner of the keyboard', 'https://images.pexels.com/photos/1772123/pexels-photo-1772123.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', '75% Keyboard with Color Multimedia Display Mechanical Gaming Keyboard, Hot Swappable Keyboard, Gasket Mount RGB Custom Keyboard, Pre-lubed Stabilizer for Mac/Win, Black Kanagawa Theme', 799, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(13, 'Keyboard', '65% Compact Hot-swappable keyboard Gateron Mechanical 3 pins switches can be hotswapped at wish and replaced by other switches.', 'https://cdn.pixabay.com/photo/2023/04/10/10/30/keyboard-7913431_1280.jpg', ' Joker Hot Swap RGB Tri-Mode Mechanical Keyboard,Wireless Bluetooth 5.0/2.4G/Wired Type-C PBT Sublimation Keycaps 65% Gaming Keyboard', 699, '2024-11-02 18:54:22', NULL, NULL, NULL),
	(14, 'bag', '444', '444', '444', 100, '2024-11-03 13:08:41', NULL, NULL, NULL),
	(15, 'tttttttttttttt', 'rrr', '444', 'rrr', 100, '2024-11-03 13:14:17', NULL, NULL, NULL),
	(16, 'kk01', 'kkk', 'kk', 'kkk', 500, '2024-11-03 14:46:32', NULL, NULL, NULL),
	(17, 'kk01', 'kkk', 'kk', 'kkk', 500, '2024-11-03 14:46:32', NULL, NULL, NULL);
/*!40000 ALTER TABLE `t_products` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

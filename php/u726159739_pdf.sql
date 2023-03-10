-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 09, 2023 at 02:06 PM
-- Server version: 10.5.16-MariaDB-cll-lve
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u726159739_pdf`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_category`
--

CREATE TABLE `tbl_category` (
  `id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `active` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_category`
--

INSERT INTO `tbl_category` (`id`, `title`, `img`, `category`, `active`) VALUES
(1, 'English', 'english.webp', 'english', 0),
(2, 'Maths', 'maths.webp', 'maths', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_chapter`
--

CREATE TABLE `tbl_chapter` (
  `id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pdf` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `active` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_chapter`
--

INSERT INTO `tbl_chapter` (`id`, `title`, `pdf`, `category`, `active`) VALUES
(1, 'Maths Chapter 1', 'maths.pdf', 'maths', 0),
(2, 'English Chapter 1', 'english.pdf', 'english', 0),
(3, 'English Chapter 2', 'english2.pdf', 'english', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_settings`
--

CREATE TABLE `tbl_settings` (
  `id` int(11) NOT NULL,
  `app_name` varchar(50) NOT NULL,
  `app_icon` varchar(255) NOT NULL,
  `api_key` varchar(255) NOT NULL,
  `app_logo` varchar(255) NOT NULL,
  `app_email` varchar(255) NOT NULL,
  `app_version` varchar(50) NOT NULL,
  `app_author` varchar(255) NOT NULL,
  `app_contact` varchar(255) NOT NULL,
  `app_website` varchar(255) NOT NULL,
  `app_version_code` varchar(255) NOT NULL,
  `app_developed_by` varchar(255) NOT NULL,
  `app_description` text NOT NULL,
  `app_privacy_policy` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_settings`
--

INSERT INTO `tbl_settings` (`id`, `app_name`, `app_icon`, `api_key`, `app_logo`, `app_email`, `app_version`, `app_author`, `app_contact`, `app_website`, `app_version_code`, `app_developed_by`, `app_description`, `app_privacy_policy`) VALUES
(1, 'Police Exam App', 'home_icon_logo.webp', 'vimal', '7784-2023-02-21.jpg', 'help@technovimal.in', '2.0.0', 'Vimal K. Vishwakarma ', '+91-8882683887', 'https://technovimal.in', '6', 'VimalCVS', '<p>Current Affairs Articles &amp; Quiz, Daily Test, Govt Jobs, Editorial,&nbsp;Notes and many more.</p>\r\n', '<p>Vimal CVS built the Up Police Exam&nbsp;as a Free app. This SERVICE is provided by Vimal CVS at no cost and is intended for use as is.</p>\r\n\r\n<p>This page informs visitors regarding my policies regarding the collection, use, and disclosure of Personal Information if anyone decides to use my Service.</p>\r\n\r\n<p>If you choose to use my Service, you agree to collect and use Information about this policy. The Personal Information I collect is used to provide and improve the Service. I will not use or share your Information with anyone except as described in this Privacy Policy.</p>\r\n\r\n<p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at Up Police Exam unless otherwise defined in this Privacy Policy.</p>\r\n\r\n<p><strong>Information Collection and Use</strong></p>\r\n\r\n<p>For a better experience while using our Service, I may require you to provide us with certain personally identifiable Information. The Information I request will be retained on your device and is not collected by me.</p>\r\n\r\n<div>\r\n<p>The app uses third-party services that may collect information to identify you.</p>\r\n\r\n<p>Link to the privacy policy of third-party service providers used by the app</p>\r\n\r\n<ul>\r\n	<li><a href=\"https://www.google.com/policies/privacy/\" rel=\"noopener noreferrer\" target=\"_blank\">Google Play Services</a></li>\r\n	<li><a href=\"https://support.google.com/admob/answer/6128543?hl=en\" rel=\"noopener noreferrer\" target=\"_blank\">AdMob</a></li>\r\n</ul>\r\n</div>\r\n\r\n<p><strong>Log Data</strong></p>\r\n\r\n<p>I want to inform you that whenever you use my Service, in a case of an error in the app, I collect data and Information (through third-party products) on your phone called Log Data. This Log Data may include Information such as your device&#39;s Internet Protocol (&quot;IP&quot;) address, device name, operating system version, the app configuration when utilizing my Service, the time and date of your use of the Service, and other statistics.</p>\r\n\r\n<p><strong>Cookies</strong></p>\r\n\r\n<p>Cookies are files with a small amount of data commonly used as unique anonymous identifiers. These are sent to your browser from the websites you visit and stored on your device&#39;s internal memory.</p>\r\n\r\n<p>This Service does not use these &quot;cookies&quot; explicitly. However, the app may use third party code and libraries that use &quot;cookies&quot; to collect information and improve their services. You can either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.</p>\r\n\r\n<p><strong>Service Providers</strong></p>\r\n\r\n<p>I may employ third-party companies and individuals due to the following reasons:</p>\r\n\r\n<ul>\r\n	<li>To facilitate our Service.</li>\r\n	<li>To provide the Service on our behalf.</li>\r\n	<li>To perform Service-related services or.</li>\r\n	<li>To assist us in analyzing how our Service is used.</li>\r\n</ul>\r\n\r\n<p>I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the Information for any other purpose.</p>\r\n\r\n<p><strong>Security</strong></p>\r\n\r\n<p>I value your trust in providing us with your Personal Information. Thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.</p>\r\n\r\n<p><strong>Links to Other Sites</strong></p>\r\n\r\n<p>This Service may contain links to other sites. You will be directed to that site if you click on a third-party link. Note that I do not operate these external sites. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for any third-party sites or services&#39; content, privacy policies, or practices.</p>\r\n\r\n<p><strong>Children&#39;s Privacy</strong></p>\r\n\r\n<div>\r\n<p>These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13. If I discover a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and know your child has provided us with personal information, please get in touch with me so I can take the necessary actions.</p>\r\n</div>\r\n\r\n<p><strong>Changes to This Privacy Policy</strong></p>\r\n\r\n<p>I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.</p>\r\n\r\n<p><strong>Contact Us</strong></p>\r\n\r\n<p>If you have any questions or suggestions about my Privacy Policy, please get in touch with me at vimalcvs29@gmail.com.</p>\r\n');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_category`
--
ALTER TABLE `tbl_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_chapter`
--
ALTER TABLE `tbl_chapter`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_category`
--
ALTER TABLE `tbl_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_chapter`
--
ALTER TABLE `tbl_chapter`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_settings`
--
ALTER TABLE `tbl_settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

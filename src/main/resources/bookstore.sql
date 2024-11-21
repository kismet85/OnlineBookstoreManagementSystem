

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


--
-- Rakenne taululle `authors`
--

CREATE TABLE `authors` (
  `author_id` bigint(20) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `authors`
--

INSERT INTO `authors` (`author_id`, `first_name`, `last_name`) VALUES
(1, 'John D.', 'Matloch'),
(2, 'Harper', 'Lee'),
(3, 'George', 'Orwell'),
(4, 'J.D.', 'Salinger'),
(5, 'Herman', 'Melville'),
(6, 'Jane', 'Austen'),
(7, 'F. Scott', 'Fitzgerald'),
(8, 'Aldous', 'Huxley'),
(9, 'Leo', 'Tolstoi'),
(10, 'Homeros', ''),
(11, 'William', 'Shakespeare'),
(12, 'Gjon', 'Buzuku'),
(13, 'Fjodor', 'Dostojevski'),
(14, 'J. R. R.', 'Tolkien'),
(15, 'James', 'Joyce'),
(16, 'Charlotte', 'Brontë'),
(17, 'William', 'Golding'),
(18, 'Charles', 'Dickens'),
(19, 'Mary', 'Shelley'),
(20, 'Fjodor', 'Dostojevski'),
(21, 'Emily', 'Brontë'),
(22, 'Miguel de', 'Cervantes'),
(23, 'Dante', 'Alighieri'),
(24, 'Victor', 'Hugo'),
(25, 'Homeros', ''),
(88, 'John', 'Doe'),
(89, 'Jane', 'Smith');

-- --------------------------------------------------------

--
-- Rakenne taululle `books`
--

CREATE TABLE `books` (
  `book_id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `isbn` varchar(13) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT 0,
  `book_condition` varchar(255) DEFAULT NULL,
  `reserved` tinyint(1) DEFAULT NULL,
  `inventory_id` bigint(20) DEFAULT NULL,
  `publisher_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `books`
--

INSERT INTO `books` (`book_id`, `title`, `isbn`, `genre`, `type`, `publication_year`, `price`, `book_condition`, `reserved`, `inventory_id`, `publisher_id`, `image_url`) VALUES
(101, 'Ashurbanipal', '9780743273565', 'Historical', 'Hardcover', 2000, 20, 'Used', 0, 1, 1, 'https://m.media-amazon.com/images/I/51VZWMUkroL._SY445_SX342_.jpg'),
(102, 'To Kill a Mockingbird', '9780061120084', 'Fiction', 'Paperback', 1960, 20, 'New', 0, 2, 2, 'https://s1.adlibris.com/images/1415391/to-kill-a-mockingbird.jpg'),
(103, '1984', '9780451524935', 'Dystopian', 'Paperback', 1949, 10, 'Used', 0, 3, 3, 'https://m.media-amazon.com/images/I/61NAx5pd6XL._AC_UF1000,1000_QL80_.jpg'),
(104, 'The Catcher in the Rye', '9780316769488', 'Fiction', 'Hardcover', 1951, 25, 'New', 1, 4, 4, 'https://m.media-amazon.com/images/I/8125BDk3l9L._AC_UF1000,1000_QL80_.jpg'),
(105, 'Moby-Dick', '9781503280786', 'Adventure', 'Paperback', 1851, 50, 'Used', 0, 5, 5, 'https://mpd-biblio-covers.imgix.net/9781466804128.jpg'),
(106, 'Pride and Prejudice', '9780141439518', 'Romance', 'Hardcover', 1813, 18, 'New', 0, 6, 1, 'https://cdn.kobo.com/book-images/afcd8653-3b27-4423-bee9-570fb1441aed/1200/1200/False/pride-and-prejudice-71.jpg'),
(107, 'The Great Gatsby', '9780743273565', 'Fiction', 'Paperback', 1925, 10, 'Used', 0, 7, 2, 'https://s2.adlibris.com/images/37255498/great-gatsby.jpg'),
(108, 'Brave New World', '9780060850524', 'Dystopian', 'Paperback', 1932, 12, 'New', 0, 8, 3, 'https://s2.adlibris.com/images/1081650/brave-new-world.jpg'),
(109, 'War and Peace', '9781853260629', 'Historical', 'Hardcover', 1869, 30, 'New', 0, 9, 2, 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRKNqgz_e3om4Tzi6SHyYss-L1RIs26ZgBe6FMH0PwlnrMkE0pNKaYVaO6bVzndbrF8PqxQeA6DBBN_zbIaw5KPf1hv99yWb_8zzD94851VueHpIUMq3Txz&usqp=CAc'),
(110, 'The Odyssey', '9780140268867', 'Epic', 'Paperback', -700, 20, 'Used', 0, 10, 3, 'https://s2.adlibris.com/images/27520907/the-odyssey.jpg'),
(111, 'Hamlet', '9780521618748', 'Drama', 'Hardcover', 1603, 25, 'New', 0, 11, 4, 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcTp9ailjoUF8Y-1WUMdB_EhKC-DJNUoTe0TwgHb8oTMQpptEerkAvVOWp1Etlvgmhqp_Czz-tMxAAzPSteVynKfF09u_RVLIXfJlY-zVwuQDRZQK4HM_TiN1g&usqp=CAc'),
(112, 'Missal', '9781853262715', 'Historical', 'Paperback', 1555, 100, 'Used', 0, 12, 5, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1zwOK_UJjoFVLEfTtXApMjll0M7FejdNWNQ&s'),
(113, 'Crime and Punishment', '9780140449136', 'Fiction', 'Hardcover', 1866, 22, 'New', 1, 13, 1, 'https://m.media-amazon.com/images/I/71O2XIytdqL._AC_UF1000,1000_QL80_.jpg'),
(114, 'The Hobbit', '9780547928227', 'Fantasy', 'Paperback', 1937, 20, 'New', 0, 14, 2, 'https://m.media-amazon.com/images/I/71jD4jMityL._AC_UF1000,1000_QL80_.jpg'),
(115, 'Ulysses', '9780141182803', 'Fiction', 'Paperback', 1922, 17, 'Used', 0, 15, 3, 'https://s2.adlibris.com/images/2650945/ulysses.jpg'),
(116, 'Jane Eyre', '9780142437209', 'Romance', 'Hardcover', 1847, 19, 'New', 0, 16, 4, 'https://www.nobleobjects.com/cdn/shop/articles/JaneEyre_1-617987_1024x1024.jpg?v=1605747658'),
(117, 'Lord of the Flies', '9780399501487', 'Fiction', 'Paperback', 1954, 14, 'New', 0, 17, 5, 'https://s2.adlibris.com/images/199787/lord-of-the-flies.jpg'),
(118, 'A Tale of Two Cities', '9780141439600', 'Historical', 'Hardcover', 1859, 21, 'Used', 0, 18, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiXGWmdvf2NaH6PLdnL8tdp-a9I0B65bJblg&s'),
(119, 'Frankenstein', '9780486282114', 'Horror', 'Paperback', 1818, 10, 'New', 1, 19, 2, 'https://s1.adlibris.com/images/60012107/frankenstein-a-readers-library-classic-hardcover.jpg'),
(120, 'The Brothers Karamazov', '9780374528379', 'Fiction', 'Hardcover', 1880, 23, 'New', 0, 20, 2, 'https://s2.adlibris.com/images/59866555/the-brothers-karamazov-royal-collectors-edition-case-laminate-hardcover-with-jacket.jpg'),
(121, 'Wuthering Heights', '9780141439556', 'Fiction', 'Paperback', 1847, 12, 'Used', 0, 21, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0e1To_lP2N4xK_R12l17PiPf_Q_jyaF0qIg&s'),
(122, 'Don Quixote', '9780060934347', 'Adventure', 'Hardcover', 1615, 35, 'New', 0, 22, 4, 'https://s2.adlibris.com/images/6372728/don-quijote.jpg'),
(123, 'The Divine Comedy', '9780142437223', 'Epic', 'Paperback', 1320, 20, 'Used', 0, 23, 5, 'https://m.media-amazon.com/images/I/51i-9SGWr-L._AC_UF1000,1000_QL80_.jpg'),
(124, 'Les Misérables', '9780451419435', 'Historical', 'Hardcover', 1862, 28, 'New', 0, 24, 1, 'https://s2.adlibris.com/images/15375427/les-miserables.jpg'),
(125, 'The Iliad', '9780140275360', 'Epic', 'Paperback', -750, 18, 'Used', 0, 25, 2, 'https://s1.adlibris.com/images/25167318/the-iliad.jpg'),
(126, 'The Sound and the Fury', '9780679732242', 'Fiction', 'Hardcover', 1929, 15, 'New', 0, 26, 3, 'https://s1.adlibris.com/images/65552243/the-sound-and-the-fury.jpg'),
(127, 'Catch-22', '9780684833392', 'Satire', 'Paperback', 1961, 20, 'Used', 0, 27, 4, 'https://s2.adlibris.com/images/1793321/catch-22-50th-anniversary-edition.jpg'),
(128, 'Moby Dick', '9781503280786', 'Adventure', 'Hardcover', 1851, 50, 'New', 0, 28, 5, 'https://s1.adlibris.com/images/13347333/moby-dick.jpg'),
(129, 'A Clockwork Orange', '9780393312836', 'Dystopian', 'Paperback', 1962, 15, 'New', 0, 29, 1, 'https://s2.adlibris.com/images/47624813/a-clockwork-orange.jpg'),
(130, 'Fahrenheit 451', '9781451673319', 'Dystopian', 'Hardcover', 1953, 18, 'Used', 1, 30, 2, 'https://s1.adlibris.com/images/3536182/fahrenheit-451.jpg');

-- --------------------------------------------------------

--
-- Rakenne taululle `books_fa`
--

CREATE TABLE `books_fa` (
  `book_id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT 0,
  `book_condition` varchar(255) DEFAULT NULL,
  `reserved` tinyint(1) DEFAULT NULL,
  `inventory_id` bigint(20) DEFAULT NULL,
  `publisher_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vedos taulusta `books_fa`
--

INSERT INTO `books_fa` (`book_id`, `title`, `isbn`, `genre`, `type`, `publication_year`, `price`, `book_condition`, `reserved`, `inventory_id`, `publisher_id`, `image_url`) VALUES
(101, 'آشوربانیپال', '9780743273565', 'تاریخی', 'جلد سخت', 2000, 20, 'دست دوم', 0, 1, 1, 'https://m.media-amazon.com/images/I/51VZWMUkroL._SY445_SX342_.jpg'),
(102, 'کشتن مرغ مقلد', '9780061120084', 'داستانی', 'جلد کاغذی', 1960, 20, 'نو', 0, 2, 2, 'https://s1.adlibris.com/images/1415391/to-kill-a-mockingbird.jpg'),
(103, 'هزار و نهصد و هشتاد و چهار', '9780451524935', 'پادآرمان‌شهری', 'جلد کاغذی', 1949, 10, 'دست دوم', 0, 3, 3, 'https://m.media-amazon.com/images/I/61NAx5pd6XL._AC_UF1000,1000_QL80_.jpg'),
(104, 'ناطور دشت', '9780316769488', 'داستانی', 'جلد سخت', 1951, 25, 'نو', 1, 4, 4, 'https://m.media-amazon.com/images/I/8125BDk3l9L._AC_UF1000,1000_QL80_.jpg'),
(105, 'موبی دیک', '9781503280786', 'ماجراجویی', 'جلد کاغذی', 1851, 50, 'دست دوم', 0, 5, 5, 'https://mpd-biblio-covers.imgix.net/9781466804128.jpg'),
(106, 'غرور و تعصب', '9780141439518', 'عاشقانه', 'جلد سخت', 1813, 18, 'نو', 0, 6, 1, 'https://cdn.kobo.com/book-images/afcd8653-3b27-4423-bee9-570fb1441aed/1200/1200/False/pride-and-prejudice-71.jpg'),
(107, 'گتسبی بزرگ', '9780743273565', 'داستانی', 'جلد کاغذی', 1925, 10, 'دست دوم', 0, 7, 2, 'https://s2.adlibris.com/images/37255498/great-gatsby.jpg'),
(108, 'دنیای قشنگ نو', '9780060850524', 'پادآرمان‌شهری', 'جلد کاغذی', 1932, 12, 'نو', 0, 8, 3, 'https://s2.adlibris.com/images/1081650/brave-new-world.jpg'),
(109, 'جنگ و صلح', '9781853260629', 'تاریخی', 'جلد سخت', 1869, 30, 'نو', 0, 9, 2, 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRKNqgz_e3om4Tzi6SHyYss-L1RIs26ZgBe6FMH0PwlnrMkE0pNKaYVaO6bVzndbrF8PqxQeA6DBBN_zbIaw5KPf1hv99yWb_8zzD94851VueHpIUMq3Txz&usqp=CAc'),
(110, 'اودیسه', '9780140268867', 'حماسی', 'جلد کاغذی', -700, 20, 'دست دوم', 0, 10, 3, 'https://s2.adlibris.com/images/27520907/the-odyssey.jpg'),
(111, 'هملت', '9780521618748', 'درام', 'جلد سخت', 1603, 25, 'نو', 0, 11, 4, 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcTp9ailjoUF8Y-1WUMdB_EhKC-DJNUoTe0TwgHb8oTMQpptEerkAvVOWp1Etlvgmhqp_Czz-tMxAAzPSteVynKfF09u_RVLIXfJlY-zVwuQDRZQK4HM_TiN1g&usqp=CAc'),
(112, 'کتاب مقدس', '9781853262715', 'تاریخی', 'جلد کاغذی', 1555, 100, 'دست دوم', 0, 12, 5, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1zwOK_UJjoFVLEfTtXApMjll0M7FejdNWNQ&s'),
(113, 'جنایت و مکافات', '9780140449136', 'داستانی', 'جلد سخت', 1866, 22, 'نو', 1, 13, 1, 'https://m.media-amazon.com/images/I/71O2XIytdqL._AC_UF1000,1000_QL80_.jpg'),
(114, 'هابیت', '9780547928227', 'فانتزی', 'جلد کاغذی', 1937, 20, 'نو', 0, 14, 2, 'https://m.media-amazon.com/images/I/71jD4jMityL._AC_UF1000,1000_QL80_.jpg'),
(115, 'اولیس', '9780141182803', 'داستانی', 'جلد کاغذی', 1922, 17, 'دست دوم', 0, 15, 3, 'https://s2.adlibris.com/images/2650945/ulysses.jpg'),
(116, 'جین ایر', '9780142437209', 'عاشقانه', 'جلد سخت', 1847, 19, 'نو', 0, 16, 4, 'https://www.nobleobjects.com/cdn/shop/articles/JaneEyre_1-617987_1024x1024.jpg?v=1605747658'),
(117, 'ارباب مگس‌ها', '9780399501487', 'داستانی', 'جلد کاغذی', 1954, 14, 'نو', 0, 17, 5, 'https://s2.adlibris.com/images/199787/lord-of-the-flies.jpg'),
(118, 'داستان دو شهر', '9780141439600', 'تاریخی', 'جلد سخت', 1859, 21, 'دست دوم', 0, 18, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiXGWmdvf2NaH6PLdnL8tdp-a9I0B65bJblg&s'),
(119, 'فرانکنشتاین', '9780486282114', 'ترسناک', 'جلد کاغذی', 1818, 10, 'نو', 1, 19, 2, 'https://s1.adlibris.com/images/60012107/frankenstein-a-readers-library-classic-hardcover.jpg'),
(120, 'برادران کارامازوف', '9780374528379', 'داستانی', 'جلد سخت', 1880, 23, 'نو', 0, 20, 2, 'https://s2.adlibris.com/images/59866555/the-brothers-karamazov-royal-collectors-edition-case-laminate-hardcover-with-jacket.jpg'),
(121, 'بلندی‌های بادگیر', '9780141439556', 'داستانی', 'جلد کاغذی', 1847, 12, 'دست دوم', 0, 21, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0e1To_lP2N4xK_R12l17PiPf_Q_jyaF0qIg&s'),
(122, 'دن کیشوت', '9780060934347', 'ماجراجویی', 'جلد سخت', 1615, 35, 'نو', 0, 22, 4, 'https://s2.adlibris.com/images/6372728/don-quijote.jpg'),
(123, 'کمدی الهی', '9780142437223', 'حماسی', 'جلد کاغذی', 1320, 20, 'دست دوم', 0, 23, 5, 'https://m.media-amazon.com/images/I/51i-9SGWr-L._AC_UF1000,1000_QL80_.jpg'),
(124, 'بینوایان', '9780451419435', 'تاریخی', 'جلد سخت', 1862, 28, 'نو', 0, 24, 1, 'https://s2.adlibris.com/images/15375427/les-miserables.jpg'),
(125, 'ایلیاد', '9780140275360', 'حماسی', 'جلد کاغذی', -750, 18, 'دست دوم', 0, 25, 2, 'https://s1.adlibris.com/images/25167318/the-iliad.jpg'),
(126, 'صدا و خشم', '9780679732242', 'داستانی', 'جلد سخت', 1929, 15, 'نو', 0, 26, 3, 'https://s1.adlibris.com/images/65552243/the-sound-and-the-fury.jpg'),
(127, 'گرفتن-۲۲', '9780684833392', 'طنز', 'جلد کاغذی', 1961, 20, 'دست دوم', 0, 27, 4, 'https://s2.adlibris.com/images/1793321/catch-22-50th-anniversary-edition.jpg'),
(128, 'موبی دیک', '9781503280786', 'ماجراجویی', 'جلد سخت', 1851, 50, 'نو', 0, 28, 5, 'https://s1.adlibris.com/images/13347333/moby-dick.jpg'),
(129, 'برتقال کوکی', '9780393312836', 'پادآرمان‌شهری', 'جلد کاغذی', 1962, 15, 'نو', 0, 29, 1, 'https://s2.adlibris.com/images/47624813/a-clockwork-orange.jpg'),
(130, 'فارنهایت ۴۵۱', '9781451673319', 'پادآرمان‌شهری', 'جلد سخت', 1953, 18, 'دست دوم', 1, 30, 2, 'https://s1.adlibris.com/images/3536182/fahrenheit-451.jpg'),
(146, 'مثال', '1234567890123', 'داستانی', 'جلد کاغذی', 2024, 30, 'نو', 0, 40, NULL, 'http://example.com/image.jpg');

-- --------------------------------------------------------

--
-- Rakenne taululle `books_jp`
--

CREATE TABLE `books_jp` (
  `book_id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `isbn` varchar(13) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT 0,
  `book_condition` varchar(255) DEFAULT NULL,
  `reserved` tinyint(1) DEFAULT NULL,
  `inventory_id` bigint(20) DEFAULT NULL,
  `publisher_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vedos taulusta `books_jp`
--

INSERT INTO `books_jp` (`book_id`, `title`, `isbn`, `genre`, `type`, `publication_year`, `price`, `book_condition`, `reserved`, `inventory_id`, `publisher_id`, `image_url`) VALUES
(101, 'アッシュールバニパル', '9780743273565', '歴史', 'ハードカバー', 2000, 20, '中古', 0, 1, 1, 'https://m.media-amazon.com/images/I/51VZWMUkroL._SY445_SX342_.jpg'),
(102, 'アラバマ物語', '9780061120084', 'フィクション', 'ペーパーバック', 1960, 20, '新品', 0, 2, 2, 'https://s1.adlibris.com/images/1415391/to-kill-a-mockingbird.jpg'),
(103, '一九八四年', '9780451524935', 'ディストピア', 'ペーパーバック', 1949, 10, '中古', 0, 3, 3, 'https://m.media-amazon.com/images/I/61NAx5pd6XL._AC_UF1000,1000_QL80_.jpg'),
(104, 'ライ麦畑でつかまえて', '9780316769488', 'フィクション', 'ハードカバー', 1951, 25, '新品', 1, 4, 4, 'https://m.media-amazon.com/images/I/8125BDk3l9L._AC_UF1000,1000_QL80_.jpg'),
(105, '白鯨', '9781503280786', '冒険', 'ペーパーバック', 1851, 50, '中古', 0, 5, 5, 'https://mpd-biblio-covers.imgix.net/9781466804128.jpg'),
(106, '高慢と偏見', '9780141439518', 'ロマンス', 'ハードカバー', 1813, 18, '新品', 0, 6, 1, 'https://cdn.kobo.com/book-images/afcd8653-3b27-4423-bee9-570fb1441aed/1200/1200/False/pride-and-prejudice-71.jpg'),
(107, 'グレート・ギャツビー', '9780743273565', 'フィクション', 'ペーパーバック', 1925, 10, '中古', 0, 7, 2, 'https://s2.adlibris.com/images/37255498/great-gatsby.jpg'),
(108, 'すばらしい新世界', '9780060850524', 'ディストピア', 'ペーパーバック', 1932, 12, '新品', 0, 8, 3, 'https://s2.adlibris.com/images/1081650/brave-new-world.jpg'),
(109, '戦争と平和', '9781853260629', '歴史', 'ハードカバー', 1869, 30, '新品', 0, 9, 2, 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRKNqgz_e3om4Tzi6SHyYss-L1RIs26ZgBe6FMH0PwlnrMkE0pNKaYVaO6bVzndbrF8PqxQeA6DBBN_zbIaw5KPf1hv99yWb_8zzD94851VueHpIUMq3Txz&usqp=CAc'),
(110, 'オデュッセイア', '9780140268867', '叙事詩', 'ペーパーバック', -700, 20, '中古', 0, 10, 3, 'https://s2.adlibris.com/images/27520907/the-odyssey.jpg'),
(111, 'ハムレット', '9780521618748', 'ドラマ', 'ハードカバー', 1603, 25, '新品', 0, 11, 4, 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcTp9ailjoUF8Y-1WUMdB_EhKC-DJNUoTe0TwgHb8oTMQpptEerkAvVOWp1Etlvgmhqp_Czz-tMxAAzPSteVynKfF09u_RVLIXfJlY-zVwuQDRZQK4HM_TiN1g&usqp=CAc'),
(112, 'ミサ', '9781853262715', '歴史', 'ペーパーバック', 1555, 100, '中古', 0, 12, 5, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1zwOK_UJjoFVLEfTtXApMjll0M7FejdNWNQ&s'),
(113, '罪と罰', '9780140449136', 'フィクション', 'ハードカバー', 1866, 22, '新品', 1, 13, 1, 'https://m.media-amazon.com/images/I/71O2XIytdqL._AC_UF1000,1000_QL80_.jpg'),
(114, 'ホビットの冒険', '9780547928227', 'ファンタジー', 'ペーパーバック', 1937, 20, '新品', 0, 14, 2, 'https://m.media-amazon.com/images/I/71jD4jMityL._AC_UF1000,1000_QL80_.jpg'),
(115, 'ユリシーズ', '9780141182803', 'フィクション', 'ペーパーバック', 1922, 17, '中古', 0, 15, 3, 'https://s2.adlibris.com/images/2650945/ulysses.jpg'),
(116, 'ジェーン・エア', '9780142437209', 'ロマンス', 'ハードカバー', 1847, 19, '新品', 0, 16, 4, 'https://www.nobleobjects.com/cdn/shop/articles/JaneEyre_1-617987_1024x1024.jpg?v=1605747658'),
(117, '蠅の王', '9780399501487', 'フィクション', 'ペーパーバック', 1954, 14, '新品', 0, 17, 5, 'https://s2.adlibris.com/images/199787/lord-of-the-flies.jpg'),
(118, '二都物語', '9780141439600', '歴史', 'ハードカバー', 1859, 21, '中古', 0, 18, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiXGWmdvf2NaH6PLdnL8tdp-a9I0B65bJblg&s'),
(119, 'フランケンシュタイン', '9780486282114', 'ホラー', 'ペーパーバック', 1818, 10, '新品', 1, 19, 2, 'https://s1.adlibris.com/images/60012107/frankenstein-a-readers-library-classic-hardcover.jpg'),
(120, 'カラマーゾフの兄弟', '9780374528379', 'フィクション', 'ハードカバー', 1880, 23, '新品', 0, 20, 2, 'https://s2.adlibris.com/images/59866555/the-brothers-karamazov-royal-collectors-edition-case-laminate-hardcover-with-jacket.jpg'),
(121, '嵐が丘', '9780141439556', 'フィクション', 'ペーパーバック', 1847, 12, '中古', 0, 21, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0e1To_lP2N4xK_R12l17PiPf_Q_jyaF0qIg&s'),
(122, 'ドン・キホーテ', '9780060934347', '冒険', 'ハードカバー', 1615, 35, '新品', 0, 22, 4, 'https://s2.adlibris.com/images/6372728/don-quijote.jpg'),
(123, '神曲', '9780142437223', '叙事詩', 'ペーパーバック', 1320, 20, '中古', 0, 23, 5, 'https://m.media-amazon.com/images/I/51i-9SGWr-L._AC_UF1000,1000_QL80_.jpg'),
(124, 'レ・ミゼラブル', '9780451419435', '歴史', 'ハードカバー', 1862, 28, '新品', 0, 24, 1, 'https://s2.adlibris.com/images/15375427/les-miserables.jpg'),
(125, 'イーリアス', '9780140275360', '叙事詩', 'ペーパーバック', -750, 18, '中古', 0, 25, 2, 'https://s1.adlibris.com/images/25167318/the-iliad.jpg'),
(126, '響きと怒り', '9780679732242', 'フィクション', 'ハードカバー', 1929, 15, '新品', 0, 26, 3, 'https://s1.adlibris.com/images/65552243/the-sound-and-the-fury.jpg'),
(127, 'キャッチ＝２２', '9780684833392', '風刺', 'ペーパーバック', 1961, 20, '中古', 0, 27, 4, 'https://s2.adlibris.com/images/1793321/catch-22-50th-anniversary-edition.jpg'),
(128, '白鯨', '9781503280786', '冒険', 'ハードカバー', 1851, 50, '新品', 0, 28, 5, 'https://s1.adlibris.com/images/13347333/moby-dick.jpg'),
(129, '時計じかけのオレンジ', '9780393312836', 'ディストピア', 'ペーパーバック', 1962, 15, '新品', 0, 29, 1, 'https://s2.adlibris.com/images/47624813/a-clockwork-orange.jpg'),
(130, '華氏451度', '9781451673319', 'ディストピア', 'ハードカバー', 1953, 18, '中古', 1, 30, 2, 'https://s1.adlibris.com/images/3536182/fahrenheit-451.jpg');

-- --------------------------------------------------------

--
-- Rakenne taululle `inventory`
--

CREATE TABLE `inventory` (
  `stock_level_used` int(11) NOT NULL,
  `stock_level_new` int(11) NOT NULL,
  `inventory_id` bigint(20) NOT NULL,
  `reserved_stock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `inventory`
--

INSERT INTO `inventory` (`stock_level_used`, `stock_level_new`, `inventory_id`, `reserved_stock`) VALUES
(100, 100, 1, 5),
(100, 95, 2, 10),
(99, 100, 3, 0),
(100, 99, 4, 2),
(99, 100, 5, 1),
(150, 60, 6, 3),
(20, 80, 7, 6),
(50, 100, 8, 2),
(100, 40, 9, 4),
(100, 50, 10, 1),
(30, 80, 11, 10),
(40, 100, 12, 5),
(100, 90, 13, 3),
(100, 75, 14, 2),
(50, 60, 15, 8),
(100, 40, 16, 4),
(35, 55, 17, 6),
(45, 65, 18, 7),
(55, 85, 19, 9),
(65, 95, 20, 10),
(70, 100, 21, 12),
(80, 110, 22, 14),
(90, 120, 23, 16),
(100, 130, 24, 18),
(110, 140, 25, 20),
(5, 15, 26, 1),
(10, 20, 27, 2),
(20, 25, 28, 5),
(15, 35, 29, 3),
(29, 50, 30, 8),
(55, 85, 31, 9),
(65, 95, 32, 10),
(70, 100, 33, 12),
(80, 110, 34, 14),
(90, 120, 35, 16),
(100, 130, 36, 18),
(110, 140, 37, 20),
(5, 10, 40, 2),
(0, 0, 41, 0),
(0, 0, 42, 0),
(10, 9, 43, 0),
(12, 11, 44, 0),
(0, 0, 45, 0),
(12, 0, 47, 0),
(0, 0, 51, 0);


--
-- Rakenne taululle `orders`
--

CREATE TABLE `orders` (
  `order_id` bigint(20) NOT NULL,
  `order_date` date NOT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `orders`
--
-- --------------------------------------------------------

--
-- Rakenne taululle `order_items`
--

CREATE TABLE `order_items` (
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `book_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


--
-- Herättimet `order_items`
--
DELIMITER $$
CREATE TRIGGER `update_order_total_after_delete` AFTER DELETE ON `order_items` FOR EACH ROW BEGIN
    
    UPDATE orders
    SET total = (
        SELECT COALESCE(SUM(quantity * price), 0)
        FROM order_items
        WHERE order_id = OLD.order_id
    )
    WHERE order_id = OLD.order_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_order_total_after_insert` AFTER INSERT ON `order_items` FOR EACH ROW BEGIN
    
    UPDATE orders
    SET total = (
        SELECT COALESCE(SUM(quantity * price), 0)
        FROM order_items
        WHERE order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_order_total_after_update` AFTER UPDATE ON `order_items` FOR EACH ROW BEGIN
    
    UPDATE orders
    SET total = (
        SELECT COALESCE(SUM(quantity * price), 0)
        FROM order_items
        WHERE order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Rakenne taululle `publishers`
--

CREATE TABLE `publishers` (
  `publisher_id` bigint(20) NOT NULL,
  `country` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `publishers`
--

INSERT INTO `publishers` (`publisher_id`, `country`, `name`) VALUES
(1, 'Finland', 'Samuseta'),
(2, 'United Kingdom', 'Samuseta'),
(3, 'Canada', ''),
(4, 'Germany', 'Samuseta'),
(5, 'France', ''),
(6, '', ''),
(7, '', ''),
(8, 'aasi', 'aasi'),
(9, 'asd', 'asd'),
(10, '', ''),
(11, 'asd', 'asd'),
(12, 'Finland', 'Example Publisher'),
(13, 'Finland', 'Samuseta'),
(14, 'Finland', 'Samuseta'),
(15, '', ''),
(16, '', ''),
(17, 'Finland', 'Samuseta');


--
-- Rakenne taululle `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `street_number` int(11) DEFAULT NULL,
  `street_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `postal_code` int(11) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `street_number`, `street_name`, `phone_number`, `postal_code`, `province`, `password`, `role`, `email`) VALUES
(1, 'admin', 'admin', 12, 'a', '1515215215', 2780, 'Espoo', '$2a$10$L6oV118qi73t2Wp0WAEyyOOibzRwCIgjI37PG3eCpLrL9hmBa7T/G', 'ADMIN', 'admin@admin');

-- --------------------------------------------------------

--
-- Rakenne taululle `written_by`
--

CREATE TABLE `written_by` (
  `book_id` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Vedos taulusta `written_by`
--

INSERT INTO `written_by` (`book_id`, `author_id`) VALUES
(101, 1),
(102, 2),
(103, 3),
(104, 4),
(105, 5),
(106, 6),
(107, 7),
(108, 8),
(109, 9),
(110, 10),
(111, 11),
(112, 12),
(113, 13),
(114, 14),
(115, 15),
(116, 16),
(117, 17),
(118, 18),
(119, 19),
(120, 20),
(121, 21),
(122, 22),
(123, 23),
(124, 24),
(125, 25),
(146, 88),
(146, 89),
(151, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`author_id`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`),
  ADD KEY `books_ibfk_1` (`inventory_id`),
  ADD KEY `books_ibfk_2` (`publisher_id`);

--
-- Indexes for table `books_fa`
--
ALTER TABLE `books_fa`
  ADD PRIMARY KEY (`book_id`),
  ADD KEY `books_ibfk_1` (`inventory_id`),
  ADD KEY `books_ibfk_2` (`publisher_id`);

--
-- Indexes for table `books_jp`
--
ALTER TABLE `books_jp`
  ADD PRIMARY KEY (`book_id`),
  ADD KEY `books_ibfk_1` (`inventory_id`),
  ADD KEY `books_ibfk_2` (`publisher_id`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`inventory_id`);

--
-- Indexes for table `loan`
--
ALTER TABLE `loan`
  ADD PRIMARY KEY (`user_id`,`book_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `orders_ibfk_1` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_id`,`book_id`),
  ADD KEY `order_items_ibfk_2` (`book_id`);

--
-- Indexes for table `publishers`
--
ALTER TABLE `publishers`
  ADD PRIMARY KEY (`publisher_id`);

--
-- Indexes for table `subscription`
--
ALTER TABLE `subscription`
  ADD PRIMARY KEY (`start_date`,`end_date`,`user_id`),
  ADD KEY `subscription_ibfk_1` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `written_by`
--
ALTER TABLE `written_by`
  ADD PRIMARY KEY (`book_id`,`author_id`),
  ADD KEY `written_by_ibfk_2` (`author_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `authors`
--
ALTER TABLE `authors`
  MODIFY `author_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `book_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=152;

--
-- AUTO_INCREMENT for table `books_fa`
--
ALTER TABLE `books_fa`
  MODIFY `book_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=151;

--
-- AUTO_INCREMENT for table `books_jp`
--
ALTER TABLE `books_jp`
  MODIFY `book_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=151;

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `inventory_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;

--
-- AUTO_INCREMENT for table `publishers`
--
ALTER TABLE `publishers`
  MODIFY `publisher_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- Rajoitteet vedostauluille
--

--
-- Rajoitteet taululle `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`inventory_id`),
  ADD CONSTRAINT `books_ibfk_2` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`publisher_id`);

--
-- Rajoitteet taululle `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Rajoitteet taululle `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`);

--
-- Rajoitteet taululle `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `subscription_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Rajoitteet taululle `written_by`
--
ALTER TABLE `written_by`
  ADD CONSTRAINT `written_by_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`),
  ADD CONSTRAINT `written_by_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `authors` (`author_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- 데이터베이스
CREATE DATABASE greentable;
USE greentable;

-- 테이블 삭제
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS issued_coupons;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS product_farms;
DROP TABLE IF EXISTS farms;

-- 리뷰 테이블 삭제
DROP TABLE IF EXISTS review_images;
DROP TABLE IF EXISTS sub_review_images;
DROP TABLE IF EXISTS product_reviews;
DROP TABLE IF EXISTS subscription_reviews;

-- 주문, 결제 테이블 삭제
DROP TABLE IF EXISTS refunds;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_seq;
DROP TABLE IF EXISTS sub_delivery_schedules;
DROP TABLE IF EXISTS subscription_orders;

DROP TABLE IF EXISTS product_qna;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS favorites;

-- 상품 테이블 삭제
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS subscription_images;
DROP TABLE IF EXISTS product_details;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS subscription_details;
DROP TABLE IF EXISTS subscriptions;

-- 유저 테이블 삭제
DROP TABLE IF EXISTS user_infos;
DROP TABLE IF EXISTS user_login;


-- 테이블 생성 
CREATE TABLE user_login (
	user_id		INT					PRIMARY KEY auto_increment,
	email		varchar(50)			NOT NULL 	UNIQUE,
	password	varchar(60)			NOT NULL,
	status		ENUM('정상', '탈퇴')	NOT NULL    DEFAULT '정상',
	user_type	ENUM('일반','oauth')	NOT NULL	DEFAULT '일반',
	provider	ENUM('카카오','구글')				DEFAULT NULL,
	oauth_id	varchar(100)					DEFAULT NULL,
	created_at	datetime			NOT NULL	DEFAULT now(),
	last_login	datetime			NOT NULL
);


CREATE TABLE user_infos (
	user_id			INT 			PRIMARY KEY,
	user_name		VARCHAR(50)		NOT NULL,
	phone			VARCHAR(20),
	zip_code		INT,
	address			VARCHAR(70),
	detail_address	VARCHAR(40),
	order_count		INT 			NOT NULL 	DEFAULT 0,
	total_amount	INT 			NOT NULL 	DEFAULT 0,
	user_grade		ENUM('브론즈','실버','골드') 	DEFAULT '브론즈',
	point			INT 			NOT NULL 	DEFAULT 0,
	FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE products (
	product_id		INT					PRIMARY KEY auto_increment,
	name			VARCHAR(50)			NOT NULL,
	sub_name		TEXT,
	price			INT					NOT NULL,
	stock			INT					NOT NULL,
	category		ENUM('도시락', '샐러드') NOT NULL,
	discount_rate	INT	  				NOT NULL	DEFAULT 0
);

CREATE TABLE product_details (
	product_id		INT		PRIMARY KEY,
	description		TEXT,
	ingredients		VARCHAR(100),
	kcal			INT,
	amount			INT,
	nutrition		ENUM('냉장','냉동'),
	created_date	DATE	NOT NULL	DEFAULT (current_date),
	updated_date	DATE	NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE product_images (
	products_image_id	INT				PRIMARY KEY auto_increment,
	image_name			VARCHAR(100)	NOT NULL,
	is_main				TINYINT(1)		NOT NULL	DEFAULT 0,
	product_id			INT				NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE subscriptions (
	subscription_id	INT			PRIMARY KEY auto_increment,
	name			VARCHAR(50)	NOT NULL,
	sub_name		VARCHAR(50),
	description		TEXT,
	price			INT			NOT NULL,
	discount_rate	INT			NOT NULL	DEFAULT 0,
	created_date	DATE		NOT NULL	DEFAULT (current_date),
	updated_date	DATE		NOT NULL
);

CREATE TABLE subscription_details (
	subscription_detail_id	INT	PRIMARY KEY AUTO_INCREMENT,
	subscription_id			INT	NOT NULL,
	product_id				INT	NOT NULL,
	FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE subscription_images (
	subscription_image_id	INT	PRIMARY KEY AUTO_INCREMENT,
	image_name				VARCHAR(100)	NOT NULL,
	is_main					TINYINT(1)		NOT NULL	DEFAULT 0,
	subscription_id			INT				NOT NULL,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE favorites (
	favorite_id	INT	PRIMARY KEY AUTO_INCREMENT,
	product_id	INT	NOT NULL,
	user_id		INT	NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE carts (
	cart_id 	INT			PRIMARY KEY AUTO_INCREMENT,
	quantity	INT			NOT NULL	DEFAULT 1,
	create_at	DATETIME	NOT NULL	DEFAULT now(),
	product_id	INT			NOT NULL,
	user_id		INT			NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uq_user_product (user_id, product_id)
);

CREATE TABLE product_qna (
	qna_id			INT	PRIMARY KEY AUTO_INCREMENT,
	title			VARCHAR(255)	NOT NULL,
	content			TEXT			NOT NULL,
	answer			TEXT,
	is_answered		ENUM('Y', 'N')	NOT NULL	DEFAULT 'N',
	created_at		DATETIME		NOT NULL	DEFAULT now(),
	answered_at		DATETIME,
	user_id			INT,
	product_id		INT	NOT NULL,
	subscription_id	INT,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_seq (
	order_id 	INT PRIMARY KEY AUTO_INCREMENT,
    order_type  ENUM('일반','정기') NOT NULL
);

CREATE TABLE orders (
	order_id			INT				PRIMARY KEY,
	merchant_uid		VARCHAR(50)		NOT NULL	UNIQUE,
	customer_name		VARCHAR(20)		NOT NULL,
	customer_phone		VARCHAR(20)		NOT NULL,
	customer_email 		VARCHAR(50) 	NOT NULL,
    recipient			VARCHAR(20)		NOT NULL,
    recipient_phone		VARCHAR(20)		NOT NULL,
    zip_code			VARCHAR(20)  	NOT NULL,
	address				VARCHAR(255)	NOT NULL,
	address_detail		VARCHAR(255),
	guest_password      VARCHAR(255),
    total_amount		INT				NOT NULL,
	used_point			INT				NOT NULL	DEFAULT 0,
	order_status	ENUM('배송준비중','배송중','배송완료','주문취소','환불신청','환불완료')	NOT NULL	DEFAULT '배송준비중',
	order_at			DATETIME		NOT NULL	DEFAULT now(),
	user_id				INT,
    FOREIGN KEY (order_id) REFERENCES order_seq(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_details (
	order_detail_id	INT	PRIMARY KEY AUTO_INCREMENT,
	quantity		INT	NOT NULL,
	price			INT	NOT NULL,
	order_id		INT	NOT NULL,
	product_id		INT	NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE subscription_orders (
	order_id			INT				PRIMARY KEY,
	merchant_uid		VARCHAR(100)	NOT NULL,
	customer_name		VARCHAR(20)		NOT NULL,
	customer_phone		VARCHAR(20)		NOT NULL,
    customer_email 		VARCHAR(50) 	NOT NULL,
    recipient			VARCHAR(20)		NOT NULL,
    recipient_phone		VARCHAR(20)		NOT NULL,
	zip_code			VARCHAR(20)  	NOT NULL,
	address				VARCHAR(255)	NOT NULL,
	address_detail		VARCHAR(255),
	total_amount		INT				NOT NULL,
	used_point			INT				NOT NULL	DEFAULT 0,
	order_status	ENUM('배송준비중','배송중', '배송완료','주문취소','환불신청', '환불완료')	NOT NULL	DEFAULT '배송준비중',
	order_at			DATETIME		NOT NULL	DEFAULT now(),
	user_id				INT				NOT NULL,
	subscription_id		INT				NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_seq(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sub_delivery_schedules (
	schedule_id		INT	PRIMARY KEY AUTO_INCREMENT,
	delivery_date	DATETIME	NOT NULL,
	delivery_status	ENUM('배송 준비중', '배송중', '배송 완료')	DEFAULT '배송 준비중',
	quantity		INT			NOT NULL	DEFAULT 1,
	order_id		INT			NOT NULL,
	product_id		INT			NOT NULL,
    FOREIGN KEY (order_id) REFERENCES subscription_orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE payments (
	payment_id		INT PRIMARY KEY AUTO_INCREMENT,
	pay_method		ENUM('CREDIT_CARD', 'KAKAO_PAY', 'TOSS_PAY'),
	paid_amount		INT	NOT NULL,
	payment_status	ENUM('결제 성공', '결제 실패', '결제 대기', '환불')	DEFAULT '결제 대기',
	paid_at			DATETIME	NOT NULL	DEFAULT now(),
	imp_uid			VARCHAR(200),
    merchant_uid 	varchar(100),
    order_id		INT	NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_seq(order_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE refunds (
	refund_id			INT	PRIMARY KEY AUTO_INCREMENT,
	refund_amount		INT	NOT NULL,
	refund_reason		TEXT,
	refund_status		ENUM('요청', '승인', '거절', '완료')	DEFAULT '요청',	
	refund_requested_at	DATETIME	DEFAULT now(),
	refund_processed_at	DATETIME,
	payment_id			INT	NOT NULL,
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE product_reviews (
	review_id		INT	PRIMARY KEY AUTO_INCREMENT,
	rating			INT	NOT NULL,
	content			TEXT,
	created_at		DATETIME	DEFAULT now(),
	product_id		INT	NOT NULL,
	order_detail_id	INT	NOT NULL,
	user_id			INT	NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_detail_id) REFERENCES order_details(order_detail_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE    
);

CREATE TABLE review_images (
	review_image_id	INT	PRIMARY KEY AUTO_INCREMENT,
	real_name		VARCHAR(100)NOT NULL,
	original_name	VARCHAR(50)	NOT NULL,
	is_main			TINYINT(1)	NOT NULL	DEFAULT 0,
	review_id		INT			NOT NULL,
    FOREIGN KEY (review_id) REFERENCES product_reviews(review_id) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE subscription_reviews (
	review_id		INT	PRIMARY KEY AUTO_INCREMENT,
	rating			INT	NOT NULL,
	content			TEXT,
	created_at		DATETIME		DEFAULT now(),
	subscription_id	INT	NOT NULL,
	order_id		INT NOT NULL,
	user_id			INT	NOT NULL,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_id) REFERENCES subscription_orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sub_review_images (
	review_image_id	INT			 NOT NULL,
	real_name		VARCHAR(100) NOT NULL,
	original_name	VARCHAR(50)	 NOT NULL,
	is_main			TINYINT(1)	 NOT NULL	DEFAULT 0,
	review_id		INT			 NOT NULL,
    FOREIGN KEY (review_id) REFERENCES subscription_reviews(review_id) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE farms (
	farm_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	address VARCHAR(255),
	farm_img VARCHAR(255),
	latitude DOUBLE,
	longitude DOUBLE,
	contract_status VARCHAR(50) DEFAULT '활성',
	category VARCHAR(50) DEFAULT '일반'
);

CREATE TABLE product_farms (
	product_farm_id	INT	PRIMARY KEY AUTO_INCREMENT,
	farm_id	INT	NOT NULL,
	product_id	INT	NOT NULL,
	FOREIGN KEY (farm_id) REFERENCES farms(farm_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);
	
CREATE TABLE coupons (
	coupon_id		INT	PRIMARY KEY AUTO_INCREMENT,
	coupon_name		VARCHAR(100) NOT NULL,
	discount_rate	INT			 NOT NULL,
	min_order_price	INT,
	coupon_grade	ENUM('브론즈','실버','골드'),
	expiration_date	DATETIME
);

CREATE TABLE issued_coupons (
	issued_coupon_id	INT	PRIMARY KEY AUTO_INCREMENT,
	issued_date			DATETIME	NOT NULL	DEFAULT now(),
	is_used				TINYINT(1)	DEFAULT 0,
	used_date			DATETIME,
	coupon_id			INT	NOT NULL,
	user_id				INT	NOT NULL,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE notice (
	notice_id	INT	PRIMARY KEY AUTO_INCREMENT,
	title		VARCHAR(50)	NOT NULL,
	content		TEXT,
	created_at	DATETIME	NOT NULL	DEFAULT NOW(),
	read_count	INT			NOT NULL	DEFAULT 0
);

CREATE TABLE events (
	event_id	INT	PRIMARY KEY AUTO_INCREMENT,
	title		VARCHAR(50)	NOT NULL,
	content		TEXT,
	start_date	DATE,
	end_date	DATE,
	event_image	VARCHAR(100),
	read_count	INT 	 	NOT NULL	DEFAULT 0,
	created_at	DATETIME 	NOT NULL	DEFAULT now()
);

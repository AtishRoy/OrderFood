DROP TABLE IF EXISTS address;
CREATE TABLE address (
  address_id bigint NOT NULL AUTO_INCREMENT,
  address varchar(255) NOT NULL,
  area varchar(255) NOT NULL,
  city varchar(255) NOT NULL,
  landmark varchar(255) DEFAULT NULL,
  latitude varchar(255) NOT NULL,
  longitude varchar(255) NOT NULL,
  pin_code varchar(255) NOT NULL,
  state varchar(255) NOT NULL,
  PRIMARY KEY (address_id));
	
	
	
	
	
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  customer_id bigint NOT NULL AUTO_INCREMENT,
  creation_date datetime NOT NULL,
  date_of_birth datetime DEFAULT NULL,
  email varchar(255) NOT NULL,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  phone_number varchar(255) NOT NULL,
  status varchar(255) NOT NULL,
  address bigint(20) DEFAULT NULL,
  PRIMARY KEY (customer_id),
  FOREIGN KEY (address) REFERENCES address(address_id));
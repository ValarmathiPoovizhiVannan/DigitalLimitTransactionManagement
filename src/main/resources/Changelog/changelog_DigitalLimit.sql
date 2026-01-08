--liquibase formatted sql

--changeset valar:1
CREATE TABLE customer (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    mobile VARCHAR(15),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--changeset valar:2
CREATE TABLE account (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    account_number VARCHAR(30) UNIQUE,
    balance DECIMAL(15,2) DEFAULT 0,
    daily_limit DECIMAL(15,2) DEFAULT 5000,
    monthly_limit DECIMAL(15,2) DEFAULT 20000,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

--changeset valar:3
CREATE TABLE transaction_history (
    txn_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT,
    txn_type VARCHAR(10),
    amount DECIMAL(15,2),
    txn_date DATE,
    status VARCHAR(20),
    reason VARCHAR(50),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(account_id)
);

--changeset valar:4
CREATE TABLE users (
id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(30) NOT NULL);

--changeset valar:5
ALTER TABLE customer
ADD password VARCHAR(50) NOT NULL;

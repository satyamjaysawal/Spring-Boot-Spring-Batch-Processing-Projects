create database sb;
use sb;
drop database sb;

CREATE TABLE products (
    prod_id INT,
    prod_code VARCHAR(255),
    prod_cost DOUBLE,
    prod_gst DOUBLE,
    prod_disc DOUBLE
);
CREATE TABLE products (
    prod_id INT PRIMARY KEY,       // Duplicate Not Allowed
    prod_code VARCHAR(255),
    prod_cost DOUBLE,
    prod_gst DOUBLE,
    prod_disc DOUBLE
);
select * from sb.products;
truncate sb.products;
SELECT * FROM sb.batch_job_execution;
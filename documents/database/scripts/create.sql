DROP DATABASE IF EXISTS electrical_data;

create database electrical_data;
use electrical_data;

CREATE TABLE place (
place_id Integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
name varchar(50),
meter varchar(50)
);

CREATE TABLE users (
user_id Integer PRIMARY KEY AUTO_INCREMENT,
name varchar(100) NOT NULL,
email varchar(50) NOT NULL UNIQUE,
password varchar (50) NOT NULL
);

CREATE TABLE electrical_data (
data_id Integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
place_id Integer NOT NULL,
measurement_date_time datetime NOT NULL,
current_phase_a Float,
current_phase_b Float,
current_phase_c Float,
current_total Float,
factor_power Float,
frequency Float,
apparent_power Float,
active_power Float,
reactive_power Float,
energy_production Float,
phase_voltage_ab Float,
phase_voltage_bc Float,
phase_voltage_ca Float,
FOREIGN KEY(place_id) REFERENCES place (place_id)
);
--liquibase formatted sql
--changeset mysql:1
create table if not exists credentials
(
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    primary key (id),
    unique(name)
);

--changeset mysql:2
create table if not exists network_devices
(
    id int NOT NULL AUTO_INCREMENT,
    device_type int DEFAULT NULL,
    firmware varchar(255) DEFAULT NULL,
    ip_address varchar(255) NOT NULL,
    label varchar(255) NOT NULL,
    mac_address varchar(255) DEFAULT NULL,
    model varchar(255) DEFAULT NULL,
    serial varchar(255) DEFAULT NULL,
    ssh_port int NOT NULL,
    state int NOT NULL DEFAULT '0',
    credential_id int NOT NULL,
    primary key (id),
    unique(ip_address),
    unique(label),
    foreign key (credential_id) references credentials (id)
);

--changeset mysql:3
CREATE TABLE ports
(
    id int NOT NULL AUTO_INCREMENT,
    connector varchar(255) DEFAULT NULL,
    mac_address varchar(255) DEFAULT NULL,
    name varchar(255) NOT NULL,
    state int DEFAULT NULL,
    device_id int DEFAULT NULL,
    primary key (id),
    unique(name, device_id),
    foreign key (device_id) references network_devices (id)
);

--changeset mysql:4
create table if not exists interfaces
(
    id int NOT NULL AUTO_INCREMENT,
    dhcp int NOT NULL,
    gateway varchar(255) DEFAULT NULL,
    ip_address varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    netmask varchar(255) DEFAULT NULL,
    state int NOT NULL,
    device_id int DEFAULT NULL,
    port_id int DEFAULT NULL,
    primary key (id),
    unique (name, device_id),
    foreign key (device_id) references network_devices (id),
    foreign key (port_id) references ports (id)
);

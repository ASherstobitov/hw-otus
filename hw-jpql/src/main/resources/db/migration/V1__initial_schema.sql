-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table phone
(
    id   bigint not null primary key,
    client_id bigint,
    number varchar(50),
    phone_id bigint
);

create table address
(
    id   bigint not null primary key,
    any_street varchar(50)
);

create table client
(
    id   bigint not null primary key,
    address_id bigint,
    name varchar(50)
);


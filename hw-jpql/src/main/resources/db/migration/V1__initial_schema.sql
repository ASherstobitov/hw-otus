-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
-- create sequence hibernate_sequence start with 1 increment by 1;
--
-- create table phones
-- (
--     id   bigint not null primary key,
--     client_id bigint,
--     number varchar(50)
-- );
--
-- create table addresses
-- (
--     id   bigint not null primary key,
--     any_street varchar(50)
-- );
--
-- create table clientes
-- (
--     id   bigint not null primary key,
--     address_id bigint,
--     name varchar(50)
-- );



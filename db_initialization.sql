CREATE DATABASE trentoticketing;
USE DATABASE trentoticketing;

create table utente
(
    nome         varchar(20) not null,
    cognome      varchar(20) not null,
    data_nascita date        not null,
    email        varchar(45) not null unique,
    telefono     char(10)    not null,
    username     varchar(20) not null primary key,
    password     varchar(45) not null,
    is_admin     boolean     not null default false
);

insert into utente (nome, cognome, data_nascita, email, telefono, username, password, is_admin) values
('admin', 'admin', '11/11/2011', 'admin@tticketing.it', '0123456789', '05nimda!', '05nimda!', true),
('Mario', 'Rossi', '11/10/1955', 'mario@bros.jp', '9468126502', 'mario', 'utente!05', false),
('Luigi', 'Verdi', '11/10/1980', 'luigi@bros.jp', '9813065009', 'luigi', 'utente!05', false);
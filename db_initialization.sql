CREATE DATABASE trentoticketing;
USE DATABASE trentoticketing;

create table utente
(
    nome                varchar(20) not null,
    cognome             varchar(20) not null,
    data_nascita        date        not null,
    email               varchar(45) not null,
    telefono            char(10)    not null,
    username            varchar(20) primary key,
    password            varchar(45) not null,
    is_admin            boolean     not null default false,
    biglietti_aquistati integer not null default 0;
);

insert into utente (nome, cognome, data_nascita, email, telefono, username, password, is_admin, biglietti_aquistati) values
('admin', 'admin', '11/11/2011', 'admin@tticketing.it', '0123456789', '05nimda!', '05nimda!', true, 0),
('Mario', 'Rossi', '11/10/1955', 'mario@bros.jp', '9468126502', 'mario', 'utente!05', false, 0),
('Luigi', 'Verdi', '11/10/1980', 'luigi@bros.jp', '9813065009', 'luigi', 'utente!05', false, 0);

create table EVENTO
(
    id                 integer primary key,
    NOME               varchar(30)       not null,
    DATA               date              not null,
    ORA                time              not null,
    TIPO               integer           not null,
    LUOGO              integer           not null,
    BIGLIETTO_POLTRONA boolean           not null,
    BIGLIETTO_PIEDI    boolean           not null,
    PREZZO_BPOLTRONA   double            not null,
    PREZZO_BPIEDI      double            not null,
    BIGLIETTI_VENDUTI  integer default 0 not null,
    SALE               integer default 0 not null,
);


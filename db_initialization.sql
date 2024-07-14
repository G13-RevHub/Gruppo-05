-- COMANDI DERBY
-- avvio derby dbms:                  java -jar %DERBY_HOME%/lib/derbyrun.jar server start
-- apertura di una connessione derby: java -jar %DERBY_HOME%/lib/derbyrun.jar ij
-- connessione (e creazione) del db:  CONNECT 'jdbc:derby://localhost:1527/TrentoTicketing;create=true';


CREATE TABLE UTENTE
(
    nome                varchar(20) not null,
    cognome             varchar(20) not null,
    data_nascita        date        not null,
    email               varchar(45) not null,
    telefono            char(10)    not null,
    username            varchar(20) primary key,
    password            varchar(45) not null,
    is_admin            boolean     not null default false,
    biglietti_acquistati integer not null default 0
);

INSERT INTO UTENTE (nome, cognome, data_nascita, email, telefono, username, password, is_admin, biglietti_acquistati) VALUES
('admin', 'admin', '11/11/2011', 'admin@tticketing.it', '0123456789', '05nimda!', '05nimda!', true, 0),
('Mario', 'Rossi', '11/10/1955', 'mario@bros.jp', '9468126502', 'mario', 'utente!05', false, 12),
('Luigi', 'Verdi', '11/10/1980', 'luigi@bros.jp', '9813065009', 'luigi', 'utente!05', false, 7),
('Wario', 'Gialli', '10/21/1992', 'wario@bros.jp', '9813065009', 'wario', 'utente!05', false, 32);

CREATE TABLE EVENTO
(
    ID                 integer primary key,
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
    SALE               integer default 0 not null
);

INSERT INTO EVENTO (ID, NOME, DATA, ORA, TIPO, LUOGO, BIGLIETTO_POLTRONA, BIGLIETTO_PIEDI, PREZZO_BPOLTRONA, PREZZO_BPIEDI, BIGLIETTI_VENDUTI, SALE) VALUES
(1, 'Back to 90s', '08/16/2024', '21:00:00', 0, 0, false, true, 0, 12.50, 125, 0),
(2, '2024 Summer Festival', '08/01/2024', '22:30:00', 0, 1, false, true, 0, 10.00, 167, 5),
(3, '2024 Winter Festival', '12/12/2024', '20:40:00', 0, 4, true, true, 10.00, 5.00, 31, 15),
(4, 'Romeo e Giulietta', '09/05/2024', '19:20:00', 1, 0, true, false, 18.00, 0, 43, 20),
(5, 'Gara di Atletica', '09/15/2024', '19:40:00', 1, 3, false, true, 0, 4.00, 36, 0),
(6, 'Gran premio dei Kart', '05/23/2025', '13:10:00', 1, 1, false, true, 0, 21.00, 11, 0),
(7, 'Visita UniTn DISI', '09/04/2024', '09:00:00', 3, 1, false, true, 0, 3.00, 49, 0),
(8, 'Mostra Arte Contemporanea', '10/06/2024', '10:00:00', 4, 2, false, true, 0, 8.00, 6, 10),
(9, 'Museo di Storia Naturale', '11/16/2024', '08:00:00', 5, 0, false, true, 0, 5.00, 29, 0);

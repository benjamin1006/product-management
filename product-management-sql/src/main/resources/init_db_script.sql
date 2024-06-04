USE SUPER_DUPER_MARKET;
create table product
(
    prod_type       varchar(31)  not null,
    id              bigint auto_increment
        primary key,
    expiration_date date         null,
    price           double       not null,
    quality         int          not null,
    type            varchar(255) null,
    base_price      double       null,
    entry_date      date         null
);

INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Wine', 1, null, 3.99, -1, 'wein', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Wine', 2, null, 4.99, 0, 'wein', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Wine', 3, null, 5.99, 10, 'wein', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Wine', 4, null, 10.99, 50, 'wein', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Cheese', 5, null, 0.99, 30, 'käse', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Cheese', 6, null, 1.99, 40, 'käse', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Cheese', 7, null, 2.99, 50, 'käse', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Cheese', 8, null, 3.99, 50, 'käse', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Fish', 9, null, 3.99, 30, 'fisch', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Fish', 10, null, 4.99, 40, 'fisch', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Fish', 11, null, 5.99, 45, 'fisch', null, null);
INSERT INTO SUPER_DUPER_MARKET.product (prod_type, id, expiration_date, price, quality, type, base_price, entry_date)
VALUES ('Fish', 12, null, 7.99, 50, 'fisch', null, null);



create sequence if not exists hibernate_sequence start with 2 increment by 1;

CREATE TABLE IF NOT EXISTS user
(
    user_id  bigint(11)     NOT NULL,
    email    varchar(45)    NOT NULL,
    password varchar(100)   NOT NULL,
    name     varchar(45)    NOT NULL,
    balance  DECIMAL(10, 0) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO user (user_id, email, password, name, balance)
VALUES (1, 'test@gmail.com', '$2a$10$Q8leaY8sqkudaig/SX4p/eUwVjZeaWDLj6Eq/lcY/HfKnr3CCkauu', 'Test',
        50);

CREATE TABLE IF NOT EXISTS category
(
    category_id bigint(11)   NOT NULL,
    name        varchar(255) NOT NULL,
    PRIMARY KEY (category_id)
);
INSERT INTO category
VALUES (hibernate_sequence.NEXTVAL, 'cups'),
       (hibernate_sequence.NEXTVAL, 'plates');

CREATE TABLE IF NOT EXISTS discount
(
    discount_id bigint(11) NOT NULL,
    percent     int(3)     NOT NULL,
    PRIMARY KEY (discount_id)
);

INSERT INTO discount
VALUES (hibernate_sequence.NEXTVAL, 15),
       (hibernate_sequence.NEXTVAL, 20);

CREATE TABLE IF NOT EXISTS good
(
    good_id     bigint(11)     NOT NULL,
    name        varchar(255)   NOT NULL,
    price       DECIMAL(10, 0) NOT NULL,
    category_id bigint(11)     NOT NULL,
    discount_id bigint(11)     NOT NULL,
    PRIMARY KEY (good_id)
);

INSERT INTO good
VALUES (hibernate_sequence.NEXTVAL, 'cup pink', 5, select category_id from category where name = 'cups',
        select discount_id from discount where percent = 15),
       (hibernate_sequence.NEXTVAL, 'cup blue', 5, select category_id from category where name = 'cups',
        select discount_id from discount where percent = 15),
       (hibernate_sequence.NEXTVAL, 'plate pink small', 10, select category_id from category where name = 'plates',
        select discount_id from discount where percent = 20),
       (hibernate_sequence.NEXTVAL, 'plate blue small', 10, select category_id from category where name = 'plates',
        select discount_id from discount where percent = 20),
       (hibernate_sequence.NEXTVAL, 'plate black small', 10, select category_id from category where name = 'plates',
        select discount_id from discount where percent = 20),
       (hibernate_sequence.NEXTVAL, 'plate blue large', 15, select category_id from category where name = 'plates',
        null);

CREATE TABLE IF NOT EXISTS order_item
(
    order_item_id bigint(11) default hibernate_sequence.nextval,
    good_id       bigint(11) NOT NULL,
    amount        int(3)     NOT NULL,
    order_id      bigint(11),
    useDiscount   BOOLEAN    NOT NULL,
    PRIMARY KEY (order_item_id)
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id bigint(11) default hibernate_sequence.nextval,
    user_id  bigint(11) NOT NULL,
    amount   int(3)     NOT NULL,
    PRIMARY KEY (order_id)
);


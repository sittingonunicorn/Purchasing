DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    user_id  bigint(11)   NOT NULL,
    email    varchar(45)  NOT NULL,
    password varchar(100) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO user
VALUES (hibernate_sequence.NEXTVAL, 'test@gmail.com', '$2a$10$Q8leaY8sqkudaig/SX4p/eUwVjZeaWDLj6Eq/lcY/HfKnr3CCkauu');

DROP TABLE IF EXISTS good;

CREATE TABLE good
(
    good_id     bigint(11)     NOT NULL AUTO_INCREMENT,
    name        varchar(45)    NOT NULL,
    price       DECIMAL(10, 0) NOT NULL,
    category_id bigint(11)     NOT NULL,
    PRIMARY KEY (good_id)
);

DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    category_id bigint(11)  NOT NULL AUTO_INCREMENT,
    name        varchar(45) NOT NULL,
    PRIMARY KEY (category_id)
);
DELETE
FROM user_roles;
DELETE
FROM votes;
DELETE
FROM dishes;
DELETE
from restaurants;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 1;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@mail.ru', '{noop}password1'),
       ('User2', 'user2@mail.ru', '{noop}password2'),
       ('User3', 'user3@mail.ru', '{noop}password3'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('ADMIN', 4),
       ('USER', 4);

INSERT INTO restaurants (name)
VALUES ('restaurant1'),
       ('Restaurant2'),
       ('Restaurant3'),
       ('Restaurant4');

INSERT INTO dishes (name, price, date, restaurant_id)
VALUES ('dish1_rest1', 10.00, '2020-03-05', 5),
       ('Dish2_rest1', 15.00, '2020-03-05', 5),
       ('Dish3_rest1', 15.00, '2020-03-06', 5),
       ('Dish1_rest2', 5.00, '2020-03-05', 6),
       ('Dish1_rest4', 7.00, '2020-03-05', 8);

INSERT INTO votes (date, user_id, restaurant_id)
VALUES ('2020-03-05', 1, 5),
       ('2020-03-05', 3, 5),
       ('2020-03-05', 2, 6),
       ('2020-03-06', 1, 5),
       ('2020-03-06', 4, 5);

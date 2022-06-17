drop table if exists review;
drop table if exists order_product;
drop table if exists `order`;
drop table if exists cart;
drop table if exists product;
drop table if exists Category;

create table "Сategory"(
                     id bigint not null auto_increment,
                     account varchar(255) not null unique,
                     password varchar(128) not null,
                     name varchar(128) not null,
                     address varchar(255) not null,
                     phone_no varchar(16) not null,
                     email varchar(128) not null,
                     birth date,
                     create_at datetime default now(),
                     password_update_at datetime default now(),
                     role enum('ROLE_USER', 'ROLE_ADMIN'),
                     primary key (id)
);

create table product(
                        id bigint not null auto_increment,
                        name varchar(128) not null,
                        price integer not null,
                        stock integer not null,
                        primary key (id)
);

create table cart(
                     id bigint not null auto_increment,
                     user_id bigint,
                     product_id bigint,
                     product_quantity integer default 1,
                     primary key (id),
                     foreign key (user_id) references user (id),
                     foreign key (product_id) references product (id)
);

create table `order`(
                        id bigint not null auto_increment,
                        user_id bigint not null,
                        amount integer,
                        shipping_address varchar(255),
                        recipient varchar(100),
                        recipient_phone varchar(16),
                        delivery_charge integer,
                        order_date date,
                        primary key (id),
                        foreign key (user_id) references user (id)
);

create table order_product(
                              id bigint not null auto_increment,
                              order_id bigint,
                              product_id bigint,
                              product_quantity integer default 1,
                              primary key (id),
                              foreign key (order_id) references `order` (id),
                              foreign key (product_id) references product (id)
);

create table review(
                       id bigint not null auto_increment,
                       order_id bigint,
                       product_id bigint,
                       title varchar(255),
                       content varchar(5000),
                       date date,
                       `like` integer default 0,
                       primary key (id),
                       foreign key (order_id) references `order` (id),
                       foreign key (product_id) references product (id)
);
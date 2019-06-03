CREATE TABLE authtable (
    id serial primary key,
    login varchar(255) not null unique,
    password varchar(255) not null,
    username varchar(100) not null unique,
    email varchar(255) unique,
    create_at date not null,
    update_at date not null,
    delete_at date
);

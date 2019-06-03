CREATE TABLE clanstorage (
    clanId serial primary key,
    clanName varchar(255) not null unique
);

CREATE TABLE inventorystorage (
    itemId serial primary key,
    itemName varchar(100),
    itemType integer not null,
    neededLevel smallint not null,
    points smallint not null,
    price smallint
);

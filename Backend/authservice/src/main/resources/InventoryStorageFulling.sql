TRUNCATE inventorystorage;
/*
*   Item types:
*       0 - empty(uses only one time)
*       1 - helmet
*       2 - armor
*       3 - sword
*       4 - shoes
*/
INSERT INTO inventorystorage (itemid, itemtype, neededlevel, points, price, itemName) VALUES (0, 0, 0, 0, 0, 'empty');
INSERT INTO inventorystorage (itemtype, neededlevel, points, price, itemName) VALUES (1, 1, 1, 1, 'helmet of leaves');
INSERT INTO inventorystorage (itemtype, neededlevel, points, price, itemName) VALUES (2, 1, 2, 3, 'armor of leaves');
INSERT INTO inventorystorage (itemtype, neededlevel, points, price, itemName) VALUES (3, 1, 3, 0, 'cane');
INSERT INTO inventorystorage (itemtype, neededlevel, points, price, itemName) VALUES (4, 1, 1, 2, 'old shoes');
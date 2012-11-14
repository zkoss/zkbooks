-- Initialize database. 
-- Put this file in classpath, Hsql will read and execute following statements.

delete from orders;
insert into orders (id, userId, status, createDate, description) values (1, 1, 'processing', CURRENT_TIMESTAMP, ' nobody at home in day time');
insert into orders (id, userId, status, createDate, description) values (2, 1, 'processing', CURRENT_TIMESTAMP, ' urgent case');
delete from OrderedItems;
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 1, 1, 'Cookies', 4.0, 10);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 1, 2, 'Toast', 3.1, 5);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 2, 1, 'Chocolate', 5.1, 30);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 2, 2, 'Butter', 2.5, 50);

delete from users;
insert into users (id, username, password, userrole) values (1, 'zk', 'zk', 'admin');

delete from products;
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (1, 'Cookies', CURRENT_TIMESTAMP, 4.0, 30, true, '/image/cookie.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (2, 'Toast', CURRENT_TIMESTAMP, 3.0, 43, true, '/image/toast.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (3, 'Chocolate', CURRENT_TIMESTAMP, 5.1, 12, true, '/image/chocolate.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (4, 'Butter', CURRENT_TIMESTAMP, 2.5, 60, true, '/image/butter.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (5, 'Milk', CURRENT_TIMESTAMP, 3.1, 71, true, '/image/milk.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (6, 'Coffee Powder', CURRENT_TIMESTAMP, 10.4, 59, true, '/image/coffee.jpg');

delete from cartitems;
insert into cartitems (id, prodId, userId, amount, createDate) values (1, 1, 1, 3, '2012-07-31 12:00:00');







delete from users;
insert into users (id, username, password, userrole) values (null, 'zk', 'zk', 'admin');

delete from products;
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Cookies', CURRENT_TIMESTAMP, 4.0, 30, true, '/image/cookie.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Toast', CURRENT_TIMESTAMP, 3.0, 43, true, '/image/toast.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Chocolate', CURRENT_TIMESTAMP, 5.1, 12, true, '/image/chocolate.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Butter', CURRENT_TIMESTAMP, 2.5, 60, true, '/image/butter.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Milk', CURRENT_TIMESTAMP, 3.1, 71, true, '/image/milk.jpg');
insert into products (id, productname, createDate, price, quantity, available, imgPath) values (null, 'Coffee Powder', CURRENT_TIMESTAMP, 10.4, 59, true, '/image/coffee.jpg');
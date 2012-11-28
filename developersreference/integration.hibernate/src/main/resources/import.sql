-- Initialize database. 
-- Put this file in classpath, Hsql will read and execute following statements.

delete from orders;
insert into orders (id, status, createDate, description) values (1, 'processing', CURRENT_TIMESTAMP, ' nobody at home in day time');
insert into orders (id, status, createDate, description) values (2, 'processing', CURRENT_TIMESTAMP, ' urgent case');
delete from OrderedItems;
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 1, 1, 'Cookies', 4.0, 10);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 1, 2, 'Toast', 3.1, 5);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 2, 1, 'Chocolate', 5.1, 30);
insert into OrderedItems (id, orderId, prodid, name, price, quantity) values (null, 2, 2, 'Butter', 2.5, 50);





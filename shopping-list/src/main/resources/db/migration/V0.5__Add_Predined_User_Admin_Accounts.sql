delete from USERS;
delete from AUTHORITIES;
insert into USERS (USERNAME, PASSWORD, ENABLED) values ('user', '$2a$10$yI4weFl5/v/k6lgTGPnPueoG0H.jRIgCiCwAP6CPm/CRtMk44rBu6', true);
insert into USERS (USERNAME, PASSWORD, ENABLED) values ('admin', '$2a$10$jikPiCrIlDNT8bF7Y.37oOoKDfuAb1LH1qhNMfdcYEJWsCXQxUwey', true);
insert into AUTHORITIES(USERNAME, AUTHORITY) values('user', 'user');
insert into AUTHORITIES(USERNAME, AUTHORITY) values('admin', 'admin');

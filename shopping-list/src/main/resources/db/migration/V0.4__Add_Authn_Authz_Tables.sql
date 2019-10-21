create table USERS (
    USERNAME varchar(50) not null primary key,
    PASSWORD varchar(100) not null,
    ENABLED boolean not null
);

create table AUTHORITIES (
    USERNAME varchar(50) not null,
    AUTHORITY varchar(100) not null,
    constraint FK_AUTHORITIES_USERS foreign key(USERNAME) references USERS(USERNAME)
);

create unique index IX_AUTH_USERNAME on AUTHORITIES (USERNAME,AUTHORITY);

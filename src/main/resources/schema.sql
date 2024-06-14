DROP TABLE IF EXISTS USERS cascade;
DROP TABLE IF EXISTS ITEM_REQUEST cascade;
DROP TABLE IF EXISTS ITEMS cascade;
DROP TABLE IF EXISTS BOOKINGS cascade;

create table IF NOT EXISTS USERS
(
    USER_ID    LONG     GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    USER_NAME  VARCHAR(25) not null,
    USER_EMAIL VARCHAR(50) not null UNIQUE,
    constraint USERS_pk
        primary key (USER_ID)
);
create table IF NOT EXISTS ITEM_REQUEST
(
    ITEM_REQUEST_ID           INTEGER GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    ITEM_REQUEST_DESCRIPTION  CHARACTER VARYING(250) not null,
    ITEM_REQUEST_TIME_CREATED TIMESTAMP              not null,
    constraint ITEM_REQUEST_PK
        primary key (ITEM_REQUEST_ID)
);
create table IF NOT EXISTS ITEMS
(
    ITEM_ID LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    ITEM_NAME VARCHAR(100) not null,
    ITEM_DESCRIPTION varchar(250) not null,
    ITEM_AVAILABLE boolean not null,
    ITEM_OWNER_ID INTEGER not null,
    constraint ITEMS_pk primary key (ITEM_ID),
    constraint ITEMS_USERS_USER_ID_fk foreign key (ITEM_OWNER_ID) references USERS ON DELETE CASCADE
--     ITEM_REQUEST_ID INTEGER not null,
--     constraint  ITEMS_REQUEST_pk foreign key (ITEM_REQUEST_ID) references ITEM_REQUEST ON DELETE CASCADE
);
create table IF NOT EXISTS BOOKING
(
    BOOKING_ID      LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    BOOK_START_DATE timestamp without time zone not null,
    BOOK_END_DATE   timestamp without time zone not null,
    BOOK_ITEM_ID    INTEGER                     not null,
    BOOKER_ID       INTEGER                     not null,
    BOOK_STATUS  VARCHAR                     not null,
    constraint BOOKINGS_pk
        primary key (BOOKING_ID),
    constraint BOOKER_fk
        foreign key (BOOKER_ID) references USERS ON DELETE CASCADE,
    constraint BOOK_ITEM_fk
        foreign key (BOOK_ITEM_ID) references ITEMS ON DELETE CASCADE
);



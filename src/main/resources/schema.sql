DROP TABLE IF EXISTS USERS cascade;
DROP TABLE IF EXISTS ITEM_REQUEST cascade;
DROP TABLE IF EXISTS ITEMS cascade;
DROP TABLE IF EXISTS BOOKINGS cascade;
DROP TABLE IF EXISTS COMMENTS cascade;

create table IF NOT EXISTS USERS
(
    USER_ID    LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    USER_NAME  VARCHAR(25)                                   NOT NULL,
    USER_EMAIL VARCHAR(50)                                   NOT NULL UNIQUE,
    constraint USERS_pk
        primary key (USER_ID)
);
create table IF NOT EXISTS ITEM_REQUEST
(
    ITEM_REQUEST_ID           LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    ITEM_REQUEST_DESCRIPTION  CHARACTER VARYING(250)                        NOT NULL,
    CREATED                   TIMESTAMP                                     NOT NULL,
    REQUESTOR_ID               LONG NOT NULL,
    constraint ITEM_REQUEST_PK
        primary key (ITEM_REQUEST_ID),
    constraint REQUESTOR_id_fk
    foreign key (REQUESTOR_ID) references USERS ON DELETE CASCADE
);
create table IF NOT EXISTS ITEMS
(
    ITEM_ID          LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    ITEM_NAME        VARCHAR(100)                                  NOT NULL,
    ITEM_DESCRIPTION VARCHAR(250)                                  NOT NULL,
    ITEM_AVAILABLE   BOOLEAN                                       NOT NULL,
    ITEM_OWNER_ID    LONG                                          NOT NULL,
    ITEM_REQUEST_ID LONG,
    constraint ITEMS_pk primary key (ITEM_ID),
    constraint ITEMS_USER_ID_fk
    foreign key (ITEM_OWNER_ID) references USERS ON DELETE CASCADE,
    constraint ITEMS_REQUEST_pk
    foreign key (ITEM_REQUEST_ID) references ITEM_REQUEST ON DELETE CASCADE
);
create table IF NOT EXISTS BOOKING
(
    BOOKING_ID      LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    BOOK_START_DATE TIMESTAMP WITHOUT TIME ZONE                   NOT NULL,
    BOOK_END_DATE   TIMESTAMP WITHOUT TIME ZONE                   NOT NULL,
    BOOK_ITEM_ID    LONG                                          NOT NULL,
    BOOKER_ID       LONG                                          NOT NULL,
    BOOK_STATUS     VARCHAR                                       NOT NULL,
    constraint BOOKINGS_pk
        primary key (BOOKING_ID),
    constraint BOOKER_fk
        foreign key (BOOKER_ID) references USERS ON DELETE CASCADE,
    constraint BOOK_ITEM_fk
        foreign key (BOOK_ITEM_ID) references ITEMS ON DELETE CASCADE
);
create table IF NOT EXISTS COMMENTS
(
    COMMENT_ID      LONG GENERATED AlWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    COMMENT_TEXT    VARCHAR(250)                                  NOT NULL,
    COMMENT_CREATED TIMESTAMP WITHOUT TIME ZONE                   NOT NULL,
    COMMENT_ITEM_ID LONG                                          NOT NULL,
    COMMENT_USER_ID VARCHAR                                       NOT NULL,
    constraint COMMENTS_pk
        primary key (COMMENT_ID),
    constraint ITEM_id_fk
        foreign key (COMMENT_ITEM_ID) references ITEMS ON DELETE CASCADE,
    constraint USER_id_fk
        foreign key (COMMENT_USER_ID) references USERS ON DELETE CASCADE
);





create schema meethere collate utf8mb4_0900_ai_ci;

create table news
(
    id bigint not null
        primary key,
    user_id bigint not null,
    title varchar(32) not null,
    image varchar(128) default '' not null,
    content text not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null
);

create index idx_time_create
    on news (time_create);

create table news_comment
(
    id bigint not null
        primary key,
    news_id bigint not null,
    user_id bigint not null,
    content text not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null
);

create index idx_news_id_time_create
    on news_comment (news_id, time_create);

create index idx_user_id_time_create
    on news_comment (user_id, time_create);

create table site
(
    id bigint not null
        primary key,
    name varchar(16) not null,
    description varchar(64) not null,
    location varchar(32) not null,
    rent decimal(8,2) not null,
    image varchar(128) not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null
);

create index idx_time_create
    on site (time_create);

create table site_booked_time
(
    id bigint not null
        primary key,
    site_id bigint not null,
    start_time datetime not null,
    end_time datetime not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null
);

create index idx_site_id_end_time
    on site_booked_time (site_id, end_time);

create index idx_site_id_start_time
    on site_booked_time (site_id, start_time);

create table site_booking_order
(
    id bigint not null
        primary key,
    user_id bigint not null,
    site_id bigint not null,
    site_name varchar(16) not null,
    site_image varchar(128) not null,
    rent decimal(8,2) not null,
    status tinyint not null,
    start_time datetime not null,
    end_time datetime not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null
);

create index idx_site_id_status_time_create
    on site_booking_order (site_id, status, time_create);

create index idx_user_id_status_time_create
    on site_booking_order (user_id, status, time_create);

create table user
(
    id bigint not null
        primary key,
    username varchar(16) not null,
    password varchar(16) not null,
    avatar varchar(128) default '' not null,
    is_administrator tinyint(1) default 0 not null,
    time_create datetime default CURRENT_TIMESTAMP not null,
    time_modified datetime default CURRENT_TIMESTAMP not null,
    constraint uk_username
        unique (username)
);


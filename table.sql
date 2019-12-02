drop schema if exists meethere;
create schema meethere;
use meethere;

drop table if exists user;

create table user
(
    id               bigint primary key not null,
    username         varchar(16)        not null,
    password         varchar(16)        not null,
    avatar           varchar(128)       not null default '',
    is_administrator boolean            not null default false,
    time_create      datetime           not null default current_timestamp,
    time_modified    datetime           not null default current_timestamp
);

create unique index uk_username on user (username);


drop table if exists site;

create table site
(
    id            bigint primary key not null,
    name          varchar(16)        not null,
    description   varchar(64)        not null,
    location      varchar(32)        not null,
    rent          decimal(8, 2)      not null,
    avatar        varchar(128)       not null,
    time_create   datetime           not null default current_timestamp,
    time_modified datetime           not null default current_timestamp
);

drop table if exists site_booking_time;

create table site_booking_time
(
    id            bigint primary key not null,
    site_id       bigint             not null,
    start_time    datetime           not null,
    end_time      datetime           not null,
    is_in_use     boolean            not null default false,
    time_create   datetime           not null default current_timestamp,
    time_modified datetime           not null default current_timestamp
);

create index idx_start_time on site_booking_time (start_time);

create index idx_end_time on site_booking_time (end_time desc);

drop table if exists site_booking_order;

create table site_booking_order
(
    id            bigint primary key not null,
    user_id       bigint             not null,
    site_id       bigint             not null,
    site_name     varchar(16)        not null,
    site_avatar   varchar(128)       not null,
    rent          decimal(8, 2)      not null,
    status        tinyint            not null,
    start_time    datetime           not null,
    end_time      datetime           not null,
    time_create   datetime           not null default current_timestamp,
    time_modified datetime           not null default current_timestamp
);

create index idx_start_time on site_booking_order (start_time);

create index idx_end_time on site_booking_order (end_time desc);

drop table if exists news;

create table news
(
    id            bigint primary key not null,
    user_id       bigint             not null,
    title         varchar(32)        not null,
    content       text               not null,
    time_create   datetime           not null default current_timestamp,
    time_modified datetime           not null default current_timestamp
);

create index idx_time_create on news (time_create desc);

drop table if exists news_comment;

create table news_comment
(
    id            bigint primary key not null,
    news_id       bigint             not null,
    user_id       bigint             not null,
    content       text               not null,
    time_create   datetime           not null default current_timestamp,
    time_modified datetime           not null default current_timestamp
);

create index idx_news_id_time_create on news_comment (news_id, time_create desc);

create index idx_user_id_time_create on news_comment (user_id, time_create desc);

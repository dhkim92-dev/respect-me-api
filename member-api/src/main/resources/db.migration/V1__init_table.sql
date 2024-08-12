create table if not exists member(
    id uuid,
    nickname varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null,
    isBlocked boolean default false,
    blockReason varchar(255) default null,
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    primary key (id)
);
create table if not exists member(
    id uuid,
    nickname varchar(64) not null,
    email varchar(64) not null,
    password varchar(255) not null,
    role smallint not null,
    is_blocked boolean default false,
    block_reason varchar(255) default null,
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    primary key (id)
);

create table if not exists device_token(
    id uuid,
    member_id uuid,
    type smallint,
    token varchar(255),
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    is_activated boolean default true,
    last_used_at timestamp(6) default current_timestamp,
    primary key (id)
);

ALTER TABLE IF EXISTS device_token
    ADD CONSTRAINT fk_device_token_member_id
    FOREIGN KEY (member_id)
    REFERENCES member(id)
    ON DELETE CASCADE;

create table if not exists member(
    id uuid,
    nickname varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role tinyint not null,
    is_blocked boolean default false,
    block_reason varchar(255) default null,
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    primary key (id)
);

create table if not exist member_device_token(
    id uuid,
    member_id uuid,
    token_type tinyint,
    token varchar(255),
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    primary key (id),
);

alter table if exist member_device_token
    add constraint fk_member_device_token_member_id foreign key (member_id) references member(id) cascade on delete;
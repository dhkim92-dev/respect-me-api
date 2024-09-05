create table notification_group (
    id uuid not null,
    name varchar(64),
    owner_id uuid not null,
    description varchar(512),
    password varchar(255),
    type integer,
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    primary key (id)
);

create table notification_group_member (
    group_id uuid not null,
    member_id uuid not null,
    nickname varchar(255),
    member_role smallint,
    profile_image_url varchar(255),
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    primary key (group_id, member_id),
    constraint fk_group_member_group_id
        foreign key (group_id)
        references notification_group (id)
        on delete cascade
);

create table group_notification(
    id uuid not null,
    group_id uuid,
    sender_id uuid,
    content varchar(255),
    status smallint,
    type smallint,
    scheduled_at timestamp(6) without time zone,
    day_of_weeks integer,
    day_interval integer,
    trigger_time time without time zone,
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    last_sent_at timestamp(6) without time zone,
    primary key (id),
    constraint fk_group_notification_group_id
        foreign key (group_id, sender_id)
        references notification_group_member(group_id, member_id)
        on delete cascade
);
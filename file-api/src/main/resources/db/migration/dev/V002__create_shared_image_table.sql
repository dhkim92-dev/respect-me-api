create table group_shared_file (
    file_id uuid not null,
    group_id uuid not null,
    uploader_id uuid not null,
    name varchar(255),
    path varchar(255),
    format integer,
    file_size bigint check(file_size > 0),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    deleted boolean default false,
    primary key (file_id)
);

alter table if exists image_info
    alter column created_at set data type timestamp(6) without time zone;

alter table if exists image_info
    alter column image_format set data type smallint;

alter table if exists image_info
    alter column image_type set data type smallint ;
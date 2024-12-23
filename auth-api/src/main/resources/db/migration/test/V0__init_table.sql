create table member_auth_info (
    id uuid not null,
    password varchar(255),
    email varchar(64),
    oidc_auth_platform integer,
    oidc_auth_user_identifier varchar(255),
    last_login_at timestamp(6) with time zone,
    primary key (id)
);
ALTER TABLE IF EXISTS member
    ADD COLUMN is_deleted boolean default false;

ALTER TABLE IF EXISTS device_token
    ADD COLUMN is_deleted boolean default false;
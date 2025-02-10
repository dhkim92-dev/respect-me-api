ALTER TABLE IF EXISTS notification_group
    DROP COLUMN thumbnail;
ALTER TABLE IF EXISTS notification_group
    ADD COLUMN thumbnail bigint default null;
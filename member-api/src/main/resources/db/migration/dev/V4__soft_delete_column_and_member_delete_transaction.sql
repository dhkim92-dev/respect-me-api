ALTER TABLE IF EXISTS member
    ADD COLUMN is_deleted boolean default false;

ALTER TABLE IF EXISTS device_token
    ADD COLUMN is_deleted boolean default false;

CREATE TABLE IF NOT EXISTS member_delete_transaction(
    transaction_id: UUID,
    member_id: UUID,
    created_at: TIMESTAMP not null,
    transaction_status int not null,
    group_service_completed int not null,
    auth_service_completed int not null
);
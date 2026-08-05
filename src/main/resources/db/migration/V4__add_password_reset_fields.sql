ALTER TABLE app_user ADD COLUMN reset_token_hash VARCHAR(64);

ALTER TABLE app_user ADD COLUMN reset_token_expiry TIMESTAMP;

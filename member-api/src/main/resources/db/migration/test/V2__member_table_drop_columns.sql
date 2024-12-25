-- Version: 3
-- Title: Drop nickname and password columns from member table
-- Date 2024-12-26
-- Author: dhkim92.dev@gmail.com
-- Description: Drop nickname and password columns, these are not necessary for the member table.
-- password managed by auth-service, and nickname field is useless on current requirements.

alter table member drop column nickname;
alter table member drop column password;
CREATE TABLE IF NOT EXISTS lingvobot.users
(
    user_id bigint PRIMARY KEY,
    user_vkid bigint,
    user_name text COLLATE pg_catalog."default"
)
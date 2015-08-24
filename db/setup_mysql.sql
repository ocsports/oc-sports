CREATE USER comocspo_admin@localhost IDENTIFIED BY 'ajdv19a';

CREATE DATABASE comocspo_pool;

GRANT ALL ON comocspo_pool.* TO comocspo_admin@localhost;

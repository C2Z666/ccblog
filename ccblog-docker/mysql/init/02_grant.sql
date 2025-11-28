-- 给ccblog授权
CREATE USER IF NOT EXISTS 'ccblog'@'%' IDENTIFIED WITH mysql_native_password BY '123';
GRANT ALL PRIVILEGES ON ccblog.* TO 'ccblog'@'%';
GRANT SELECT ON performance_schema.* TO 'ccblog'@'%';
FLUSH PRIVILEGES;

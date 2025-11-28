-- V1.0.0__
-- 用户模块

-- 给user相关的四张数据表插入简单的数据

-- 插入user
insert into user(id,user_name,password,login_type,deleted,user_role) values(1,'czc','60c350c917582a367bae9ffbeada49b7',0,0,1);
insert into user(id,user_name,password,login_type,deleted,user_role) values(2,'ccblog','60c350c917582a367bae9ffbeada49b7',0,0,1);
insert into user(id,user_name,password,login_type,deleted,user_role) values(3,'lcx','60c350c917582a367bae9ffbeada49b7',0,0,0);
insert into user(id,user_name,password,login_type,deleted,user_role) values(4,'ywl','60c350c917582a367bae9ffbeada49b7',0,0,0);
insert into user(id,user_name,password,login_type,deleted,user_role) values(5,'rk','60c350c917582a367bae9ffbeada49b7',0,0,0);

-- 插入user_info,ip相关功能未开发
insert into user_info(user_id, user_name, photo, `position`, company, profile, ip)
values (1, 'czc', 'https://i.cetsteam.com/imgs/2025/11/27/addcf0cec5bad6af.jpg', '学校', '学生', 'ccblog创始人', '{"firstIp": "121.40.134.96", "latestIp": "58.48.23.111", "firstRegion": "浙江·杭州", "latestRegion": "陕西·西安"}');
insert into user_info(user_id, user_name, photo, `position`, company, profile, ip)
values (2, 'ccblog', 'https://i.cetsteam.com/imgs/2025/11/27/6d1e202e120ae6d2.png', 'ccblog', '系统', '系统账号', '{"firstIp": "121.40.126.98", "latestIp": "58.48.24.131", "firstRegion": "浙江·杭州", "latestRegion": "浙江·杭州"}');
insert into user_info(user_id, user_name, photo, `position`, company, profile, ip)
values (3, 'lcx', 'https://i.cetsteam.com/imgs/2025/11/27/86fdabbc593c8b06.png', '学校', '研究生', '划水账号', '{"firstIp": "121.40.124.96", "latestIp": "58.48.23.121", "firstRegion": "浙江·台州", "latestRegion": "浙江·宁波"}');
insert into user_info(user_id, user_name, photo, `position`, company, profile, ip)
values (4,'ywl', 'https://i.cetsteam.com/imgs/2025/11/27/6eedc12d1271beb0.png ', '家里', '电商创业', '已躺平', '{"firstIp": "121.40.154.96", "latestIp": "58.48.23.118", "firstRegion": "浙江·台州", "latestRegion": "浙江·台州"}');
insert into user_info(user_id, user_name, photo, `position`, company, profile, ip)
values (5, 'rk', 'https://i.cetsteam.com/imgs/2025/11/27/54ffe856a29e8c55.png ', '银行', '本科生', 'czc的朋友', '{"firstIp": "121.40.124.96", "latestIp": "58.48.23.161", "firstRegion": "浙江·杭州", "latestRegion": "浙江·热爱只有"}');

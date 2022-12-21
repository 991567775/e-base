/*初始化用户*/
# create user 'root'@'%' identified by 'wzzt991567775';
create user 'edp'@'%' identified by 'wzzt991567775';
create user 'edp'@'localhost' identified by 'wzzt991567775';
create database edp;
# grant all privileges on *.* to 'root'@'%' with grant option;
grant all privileges on *.* to 'edp'@'%' with grant option;
grant all privileges on *.* to 'edp'@'localhost' with grant option;
FLUSH PRIVILEGES;


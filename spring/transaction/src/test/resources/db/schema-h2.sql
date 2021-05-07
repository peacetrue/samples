DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id    BIGINT(20) primary key auto_increment COMMENT '主键ID',
    name  VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age   INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱'
);

-- auto-generated definition
create table user
(
    id            bigint auto_increment
        primary key,
    username      varchar(255)                        null comment '用户名称',
    user_account  varchar(255)                        null comment '账号',
    avatar_url    varchar(1024)                       null comment '用户头像',
    gender        tinyint                             null comment '性别',
    user_password varchar(512)                        not null comment '密码',
    phone         varchar(128)                        null comment '用户手机号',
    email         varchar(512)                        null comment '用户邮箱',
    user_status   int unsigned zerofill     default 0 null comment '用户状态：0-正常',
    create_time   datetime                            null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time   datetime                            null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint unsigned zerofill default 0 not null comment '是否删除',
    user_role     int unsigned zerofill     default 0 not null comment '用户的角色0：正常用户，1管理员',
    tags          varchar(1024)                       null comment '标签列表',
    id_number     varchar(512)                        null comment '身份证号'
);
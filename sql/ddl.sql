-- auto-generated definition
create table interface_info
(
    id              int auto_increment comment '自增ID'
        primary key,
    name            varchar(256) default ''                not null comment '接口名称',
    url             varchar(256) default ''                not null comment '请求url',
    description     varchar(256) default ''                not null comment '描述',
    request_header  varchar(256) default ''                not null comment '请求头',
    response_header varchar(256) default ''                null comment '响应头',
    status          tinyint      default 0                 not null comment '状态，0-关闭，1-开启',
    method          varchar(256) default ''                not null comment '请求类型',
    userId          int          default 0                 null comment '创建人',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    deleted         tinyint      default 0                 not null comment '是否删除'
)
    comment '接口信息表' charset = utf8mb4;
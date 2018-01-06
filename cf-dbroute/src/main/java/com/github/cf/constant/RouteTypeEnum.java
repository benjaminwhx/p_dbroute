package com.github.cf.constant;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-07
 * Time: 02:16 am
 */
public enum RouteTypeEnum {
    /**
     * 数据库
     */
    DB(0),
    /**
     * 表
     */
    TABLE(1),
    /**
     * 数据库和表
     */
    DT(2);

    int type;

    RouteTypeEnum(int type) {
        this.type = type;
    }
}

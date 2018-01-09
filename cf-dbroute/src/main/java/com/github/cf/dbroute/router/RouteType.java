package com.github.cf.dbroute.router;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 2:25 pm
 *
 * 路由类型
 */
public enum RouteType {
    /**
     * 分库不分表
     */
    DB,
    /**
     * 不分库分表
     */
    TABLE,
    /**
     * 分库和分表
     */
    DB_AND_TABLE;

    /**
     * 是否支持分库
     * @return
     */
    public boolean supportShardingDB() {
        return this == RouteType.DB || this == RouteType.DB_AND_TABLE;
    }

    /**
     * 是否支持分表
     * @return
     */
    public boolean supportShardingTable() {
        return this == RouteType.TABLE || this == RouteType.DB_AND_TABLE;
    }
}

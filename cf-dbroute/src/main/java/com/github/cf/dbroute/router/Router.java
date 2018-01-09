package com.github.cf.dbroute.router;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 8:06 pm
 *
 * 路由器
 */
public interface Router {

    /**
     * 路由操作，根据shardingFieldValue获取到分片信息
     * @param shardingFieldValue
     */
    void route(String shardingFieldValue, RouteType routeType);
}

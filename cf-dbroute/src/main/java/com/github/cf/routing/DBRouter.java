package com.github.cf.routing;

import com.github.cf.DoRoute;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-07
 * Time: 02:39 am
 */
public interface DBRouter {

    /**
     * 获取分片字段
     * @param doRoute
     * @return
     */
    String findRouteField(DoRoute doRoute);

    /**
     * 计算分片的字段值
     * @param routeField
     * @return
     */
    int computeCode(String routeField);

    /**
     * 进行路由
     * @param routeField
     * @return
     * @throws
     */
    public String doRoute(String routeField);
}

package com.github.cf.routing.router;

import com.github.cf.DBRuleConfig;
import com.github.cf.DoRoute;
import com.github.cf.routing.DBRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-07
 * Time: 02:44 am
 *
 * Router实现，其实现了数据水平切分后的路由方式
 */
public class DBRouterImpl implements DBRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBRouterImpl.class);

    /**
     * 配置列表
     */
    private List<DBRuleConfig> dbRuleConfigList;

    @Override
    public String findRouteField(DoRoute doRoute) {
        return doRoute.routeField();
    }

    @Override
    public int computeCode(String routeField) {
        int i = routeField.hashCode();
        if (i < 0) {
            i = -i;
        }
        return i;
    }

    @Override
    public String doRoute(String routeField) {
        if (StringUtils.isEmpty(routeField)) {
            throw new IllegalArgumentException("you must be specify routeField!");
        }
        return null;
    }
}

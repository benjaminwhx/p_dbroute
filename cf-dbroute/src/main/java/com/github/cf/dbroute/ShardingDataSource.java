package com.github.cf.dbroute;

import com.github.cf.dbroute.constant.ShardingConstant;
import com.github.cf.dbroute.context.ShardingContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 7:33 pm
 *
 * 分片数据源
 */
public class ShardingDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dbKey = ShardingContextHolder.getDbKey();
        return dbKey == null ? ShardingConstant.SINGLE_DATA_SOURCE : dbKey;
    }
}

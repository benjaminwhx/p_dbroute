package com.github.cf.dbroute.config;

import java.util.Set;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 2:45 pm
 */
public class ShardingConfig {

    /**
     * 需要分表的集合
     */
    private Set<String> shardingTables;

    public Set<String> getShardingTables() {
        return shardingTables;
    }

    public void setShardingTables(Set<String> shardingTables) {
        this.shardingTables = shardingTables;
    }
}

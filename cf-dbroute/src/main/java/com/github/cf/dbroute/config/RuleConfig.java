package com.github.cf.dbroute.config;

import java.util.List;
import java.util.Set;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 7:31 pm
 *
 * 分片公共配置
 */
public class RuleConfig {

    /**
     * 根据整型区间
     */
    public final static int RULE_TYPE_INT = 0;
    /**
     * 根据时间区间
     */
    public final static int RULE_TYPE_DATE = 1;
    /**
     * 根据字符串
     */
    public final static int RULE_TYPE_STR = 2;

    /**
     * 数据库表的逻辑KEY,与数据源MAP配置中的key一致
     */
    private List<String> dbKeyList;
    /**
     * 分片规则类型
     */
    private int ruleType;
    /**
     * 数据表index样式
     */
    private String tableIndexStyle;
    /**
     * table的idx是否连续
     */
    private boolean seqForTableIndex;
    /**
     * 后缀开始索引，默认从0开始
     */
    private int tableStartSuffixIndex;
    /**
     * 默认分片字段名，可以配置多个，当不指定@DoRoute的shardingField时使用
     */
    private Set<String> defaultShardingFieldName;

    public Set<String> getDefaultShardingFieldName() {
        return defaultShardingFieldName;
    }

    public void setDefaultShardingFieldName(Set<String> defaultShardingFieldName) {
        this.defaultShardingFieldName = defaultShardingFieldName;
    }

    public List<String> getDbKeyList() {
        return dbKeyList;
    }

    public void setDbKeyList(List<String> dbKeyList) {
        this.dbKeyList = dbKeyList;
    }

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public String getTableIndexStyle() {
        return tableIndexStyle;
    }

    public void setTableIndexStyle(String tableIndexStyle) {
        this.tableIndexStyle = tableIndexStyle;
    }

    public boolean isSeqForTableIndex() {
        return seqForTableIndex;
    }

    public void setSeqForTableIndex(boolean seqForTableIndex) {
        this.seqForTableIndex = seqForTableIndex;
    }

    public int getTableStartSuffixIndex() {
        return tableStartSuffixIndex;
    }

    public void setTableStartSuffixIndex(int tableStartSuffixIndex) {
        this.tableStartSuffixIndex = tableStartSuffixIndex;
    }
}

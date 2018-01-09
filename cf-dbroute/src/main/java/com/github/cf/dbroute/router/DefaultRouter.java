package com.github.cf.dbroute.router;

import com.github.cf.dbroute.config.RuleConfig;
import com.github.cf.dbroute.config.ShardingConfig;
import com.github.cf.dbroute.constant.ShardingConstant;
import com.github.cf.dbroute.context.ShardingContextHolder;
import com.github.cf.dbroute.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 9:26 pm
 *
 * 默认的路由器
 */
public class DefaultRouter implements Router {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouter.class);

    private final RuleConfig ruleConfig;
    private ShardingConfig shardingConfig;

    public DefaultRouter(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public DefaultRouter(RuleConfig ruleConfig, ShardingConfig shardingConfig) {
        this.ruleConfig = ruleConfig;
        this.shardingConfig = shardingConfig;
    }

    @Override
    public void route(String shardingFieldValue, RouteType routeType) {
        int hashFieldValue = computeHashCode(shardingFieldValue);
        int ruleType = ruleConfig.getRuleType();
        switch (ruleType) {
            case RuleConfig.RULE_TYPE_INT:
            case RuleConfig.RULE_TYPE_DATE:
            case RuleConfig.RULE_TYPE_STR:
                routeByStr(hashFieldValue, routeType);
                break;
            default:
                throw new IllegalArgumentException("you must specify a correct ruleType, in{0,1,2}");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RouteType:{} route fieldValue:{} result: dbKey->{}, tableSuffix:{}",
                    routeType, shardingFieldValue, ShardingContextHolder.getDbKey(), ShardingContextHolder.getTableSuffix());
        }
    }

    private void routeByStr(int hashFieldValue, RouteType routeType) {
        List<String> dbKeyList = ruleConfig.getDbKeyList();
        if (dbKeyList != null) {
            int dbNumber = dbKeyList.size();

            int dbIndex = -1;
            int tbIndex = -1;

            if (routeType == RouteType.DB) {
                dbIndex = hashFieldValue % dbNumber;
            } else if (routeType == RouteType.TABLE) {
                int tableNum = getShardingTableNum(routeType);
                tbIndex = hashFieldValue % tableNum;
            } else if (routeType == RouteType.DB_AND_TABLE) {
                int tableNum = getShardingTableNum(routeType);
                int mode = dbNumber * tableNum;
                dbIndex = hashFieldValue / mode % dbNumber;
                tbIndex = hashFieldValue % tableNum;
                if (ruleConfig.isSeqForTableIndex()) {
                    tbIndex = tableNum * dbIndex + tbIndex;
                }
            }

            // 改变后缀table index样式
            if (routeType.supportShardingTable()) {
                tbIndex += ruleConfig.getTableStartSuffixIndex();
                String tableSuffix = formatTableSuffix(ruleConfig.getTableIndexStyle(), tbIndex);
                ShardingContextHolder.setTableSuffix(tableSuffix);
            }

            // 没有进行分库，返回单库key
            if (dbIndex == -1) {
                ShardingContextHolder.setDbKey(ShardingConstant.SINGLE_DATA_SOURCE);
            } else {
                ShardingContextHolder.setDbKey(ruleConfig.getDbKeyList().get(dbIndex));
            }
        }
    }

    private static final ConcurrentHashMap<String, DecimalFormat> DECIMAL_FORMAT_CACHE =
            new ConcurrentHashMap<String, DecimalFormat>();

    private String formatTableSuffix(String tableIndexStyle, int tbIndex) {
        if (StringUtils.isEmpty(tableIndexStyle)) {
            tableIndexStyle = ShardingConstant.DEFAULT_TABLE_SUFFIX_STYLE;
        }
        DecimalFormat df;
        if (DECIMAL_FORMAT_CACHE.containsKey(tableIndexStyle)) {
            df = DECIMAL_FORMAT_CACHE.get(tableIndexStyle);
        } else {
            df = new DecimalFormat();
            df.applyPattern(tableIndexStyle);
            DECIMAL_FORMAT_CACHE.putIfAbsent(tableIndexStyle, df);
        }
        return df.format(tbIndex);
    }

    private int getShardingTableNum(RouteType routeType) {
        if (shardingConfig == null || shardingConfig.getShardingTables() == null) {
            throw new IllegalArgumentException("the routeType:" + routeType + " support sharding table, but you not specify the sharding tables for shardingConfig");
        }
        return shardingConfig.getShardingTables().size();
    }

    /**
     * 计算字段值的hash值（正数）
     * @param shardingFieldValue
     * @return
     */
    protected int computeHashCode(String shardingFieldValue) {
        int i = shardingFieldValue.hashCode();
        if (i < 0) {
            i = -i;
        }
        return i;
    }
}

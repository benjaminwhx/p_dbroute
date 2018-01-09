package com.github.cf.dbroute.context;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 7:34 pm
 *
 * 分片threadLocal上下文，保存db key以及表名后缀
 */
public class ShardingContextHolder {

    /**
     * 保存db的key
     */
    private static final ThreadLocal<String> DB_KEY_HOLDER = new ThreadLocal<>();
    /**
     * 保存table的后缀，真实表名=逻辑表名+后缀
     */
    private static final ThreadLocal<String> TABLE_SUFFIX_HOLDER = new ThreadLocal<>();

    public static void setDbKey(String dbKey) {
        DB_KEY_HOLDER.set(dbKey);
    }

    public static String getDbKey() {
        return DB_KEY_HOLDER.get();
    }

    public static void clearDbKey() {
        DB_KEY_HOLDER.remove();
    }

    public static void setTableSuffix(String tableSuffix) {
        TABLE_SUFFIX_HOLDER.set(tableSuffix);
    }

    public static String getTableSuffix() {
        return TABLE_SUFFIX_HOLDER.get();
    }

    public static void clearTableSuffix() {
        TABLE_SUFFIX_HOLDER.remove();
    }
}

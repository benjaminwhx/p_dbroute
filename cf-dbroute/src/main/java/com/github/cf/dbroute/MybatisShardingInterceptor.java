package com.github.cf.dbroute;

import com.github.cf.dbroute.config.ShardingConfig;
import com.github.cf.dbroute.context.ShardingContextHolder;
import com.github.cf.dbroute.utils.ReflectUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 5:50 pm
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class})
})
public class MybatisShardingInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisShardingInterceptor.class);
    private ThreadLocal<String> sqlCache = new ThreadLocal<>();
    private ShardingConfig shardingConfig;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        BoundSql sql;
        if (invocation.getTarget() instanceof Executor) {
            String sqlId = null;
            try {
                MappedStatement statement = (MappedStatement) args[0];
                Object parameterObject = args[1];
                sql = statement.getBoundSql(parameterObject);
                sqlId = statement.getId();
                if (sql == null) {
                    throw new IllegalStateException("获取boundSql失败");
                }
                String orgSql = sql.getSql().replaceAll("[\\s]+", " ");
                LOGGER.info("MybatisShardingInterceptor.intercept: orgSql->{}", orgSql);
                String newSql = parseSql(orgSql);
                // 保存sql
                sqlCache.set(newSql);
            } catch (Exception e) {
                // TODO: 报警
                LOGGER.warn("转换表名异常(忽略处理,保证业务正常运行),sqlId:{}", sqlId, e);
                sqlCache.remove();
            }
        } else if (invocation.getTarget() instanceof RoutingStatementHandler) {
            // 这里面才能真正修改sql
            try {
                RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) invocation.getTarget();
                StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(routingStatementHandler, "delegate");
                sql = delegate.getBoundSql();
                String oldSql = sql.getSql().replaceAll("[\\s]+", " ");
                String newSql = sqlCache.get();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("MybatisShardingInterceptor.intercept, oldSql:{}, newSql:{}", oldSql, newSql);
                }
                ReflectUtil.setFieldValue(sql, "sql", newSql);
            } catch (Exception e) {
                // TODO: 报警
                LOGGER.warn("转换表名异常(忽略处理,保证业务正常运行):", e);
            }
        } else {
            String name = invocation.getTarget().getClass().getName();
            LOGGER.warn("目标对象类型是{},而不是RoutingStatementHandler? bug", name);
            // TODO: 报警
        }
        Object proceed = null;
        try {
            proceed = invocation.proceed();
            return proceed;
        } finally {
            sqlCache.remove();
        }
    }

    /**
     * 解析原来的sql，取到所有表名，如果是分表加上后缀，替换原来的sql中的表名
     * @param oldSql 旧的sql
     * @return 新的sql
     */
    private String parseSql(String oldSql) throws JSQLParserException {
        List<String> tableNames = getAllTableNames(oldSql);
        String newSql = oldSql;
        Set<String> shardingTables = shardingConfig.getShardingTables();
        for (String tableName : tableNames) {
            if (shardingTables.contains(tableName)) {
                newSql = newSql.replaceAll(tableName, tableName + ShardingContextHolder.getTableSuffix());
            }
        }
        return newSql;
    }

    private List<String> getAllTableNames(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        TablesNamesFinder finder = new TablesNamesFinder();
        List<String> tableList = finder.getTableList(statement);
        return tableList;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setShardingConfig(ShardingConfig shardingConfig) {
        this.shardingConfig = shardingConfig;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

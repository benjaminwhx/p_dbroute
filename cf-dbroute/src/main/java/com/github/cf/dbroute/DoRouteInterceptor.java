package com.github.cf.dbroute;

import com.github.cf.dbroute.config.RuleConfig;
import com.github.cf.dbroute.context.ShardingContextHolder;
import com.github.cf.dbroute.router.Router;
import com.github.cf.dbroute.utils.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 7:49 pm
 *
 * 拦截注解 @DoRoute 进行路由操作
 */
@Aspect
public class DoRouteInterceptor implements InitializingBean, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoRouteInterceptor.class);
    private final ExpressionParser parser = new SpelExpressionParser();
    private static String FIELD_EXPR_OPEN = "#{";
    private static String FIELD_EXPR_CLOSE = "}";

    private Router router;
    private RuleConfig ruleConfig;
    // 默认优先级
    private int order = 50;

    @Pointcut("@annotation(com.github.cf.dbroute.DoRoute)")
    public void aopPoint() {
    }

    @Before("aopPoint()")
    public void doRoute(JoinPoint jp) throws Throwable {
        long begin = System.currentTimeMillis();

        // 1、获取注解DoRoute
        DoRoute doRoute = getDoRoute(jp);
        String shardingField = doRoute.shardingField();

        // 2、获取分片字段对应的值
        Object[] args = jp.getArgs();
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("方法:" + getMethod(jp).getName() + "必须拥有一个分片参数!");
        }
        String shardingFieldValue = fetchShardingFieldValue(shardingField, args);

        if (StringUtils.isNotEmpty(shardingFieldValue)) {
            // 3、根据字段值进行路由
            router.route(shardingFieldValue, doRoute.routeType());
        } else {
            LOGGER.warn("获取分片值错误@DoRoute.shardingField:{}", shardingField);
            // TODO: 报警
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doRoute花费时间:{}ms", System.currentTimeMillis() - begin);
        }
    }

    @After("aopPoint()")
    public void clear() {
        ShardingContextHolder.clearDbKey();
        ShardingContextHolder.clearTableSuffix();
    }

    /**
     * 获取分片字段对应的值，用来路由db和table
     * 如果没有指定@DoRoute中的shardingField，拿到shardingConfig的defaultShardingFieldName来遍历所有参数
     * @param shardingField
     * @param args
     * @return
     */
    private String fetchShardingFieldValue(String shardingField, Object[] args) {
        if (checkRule(shardingField)) {
            return fetchValueWithExpr(shardingField, args);
        } else if (ruleConfig.getDefaultShardingFieldName() != null && ruleConfig.getDefaultShardingFieldName().size() > 0){
            for (int i = 0; i < args.length; i++) {
                Object obj = args[i];
                Set<String> defaultShardingFieldName = ruleConfig.getDefaultShardingFieldName();
                for (String field : defaultShardingFieldName) {
                    try {
                        return BeanUtils.getProperty(obj, field);
                    } catch (IllegalAccessException e) {
                        LOGGER.warn("从[{}]读取[{}]属性失败.{}", obj, defaultShardingFieldName, e.getMessage());
                    } catch (InvocationTargetException e) {
                        LOGGER.warn("从[{}]读取[{}]属性失败.{}", obj, defaultShardingFieldName, e.getMessage());
                    } catch (NoSuchMethodException e) {
                        LOGGER.warn("从[{}]读取[{}]属性失败.{}", obj, defaultShardingFieldName, e.getMessage());
                    }
                }
            }
        }
        throw new IllegalArgumentException("you must specify at least one value of @DoRoute.shardingField or ShardingConfig.defaultShardingFieldName");
    }

    private boolean checkRule(String shardingField) {
        return StringUtils.isNotEmpty(shardingField) &&
                shardingField.startsWith(FIELD_EXPR_OPEN) &&
                shardingField.endsWith(FIELD_EXPR_CLOSE);
    }

    private String fetchValueWithExpr(String expressionStr, Object obj) {
        // 去掉标示符号,spring不要这个.
        String str = StringUtils.substringBetween(expressionStr, FIELD_EXPR_OPEN, FIELD_EXPR_CLOSE);
        Expression exp = parser.parseExpression(str);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(obj);
        return exp.getValue(context, String.class);
    }

    /**
     * 获取DoRoute注解
     * @param jp
     * @return
     * @throws NoSuchMethodException
     */
    private DoRoute getDoRoute(JoinPoint jp) throws NoSuchMethodException {
        return getMethod(jp).getAnnotation(DoRoute.class);
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setRuleConfig(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (ruleConfig == null || router == null) {
            throw new IllegalArgumentException("DoRouteInteceptor must specify ruleConfig and router!");
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}

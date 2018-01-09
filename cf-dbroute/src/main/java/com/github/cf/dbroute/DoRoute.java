package com.github.cf.dbroute;

import com.github.cf.dbroute.router.RouteType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 7:48 pm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoRoute {

    /**
     * 兼容复杂的对象组合结构.使用spring表达式实现.
     *
     * @return
     * @WARN 如果使用el表达式取值, 要求:
     * <pre>
     * 1.属性值必须以'#{'开头,'}'结尾;
     * 2.参数列表为多个的时候,需要当成数组来访问.
     *
     * eg: commit(Loan loan,Plan plan);
     * 可以写成 #{[0].platPin},代表,从第一个参数loan对象中获取platPin属性.
     * </pre>
     */
    String shardingField() default "";

    RouteType routeType() default RouteType.DB_AND_TABLE;
}


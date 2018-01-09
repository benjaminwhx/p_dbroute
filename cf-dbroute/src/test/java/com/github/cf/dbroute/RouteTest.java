package com.github.cf.dbroute;

import com.github.cf.dbroute.bean.Loan;
import com.github.cf.dbroute.bean.Order;
import com.github.cf.dbroute.bean.OrderItem;
import com.github.cf.dbroute.service.ShardingService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 4:26 pm
 */
public class RouteTest {

    private ShardingService shardingService;

    @Before
    public void before() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        shardingService = (ShardingService) context.getBean("shardingService");
    }

    @Test
    public void testHaveTransactionHaveSharding() {
        shardingService.insertAndSelectOrder(buildOrder());
    }

    @Test
    public void testNoTransactionNoSharding() {
        shardingService.insertAndSelectLoan(buildLoan());
    }

    @Test
    public void testHaveTransactionNoSharding() {
        shardingService.insertAndSelectLoanWithTransaction(buildLoan());
    }

    @Test
    public void testOrderItem() {
        shardingService.insertOrderItem(buildOrderItem());
    }

    private static OrderItem buildOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(123);
        orderItem.setItemId(1);
        orderItem.setOrderId(555);
        return orderItem;
    }

    private static Order buildOrder() {
        Order order = new Order();
        order.setUserId(100);
        order.setOrderId(108);
        return order;
    }

    private static Loan buildLoan() {
        Loan loan = new Loan();
        loan.setLoanId("A12390234234");
        loan.setAmount(new BigDecimal("3000"));
        loan.setUserId("100");
        return loan;
    }
}

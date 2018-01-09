package com.github.cf.dbroute.service;

import com.github.cf.dbroute.DoRoute;
import com.github.cf.dbroute.bean.Loan;
import com.github.cf.dbroute.bean.Order;
import com.github.cf.dbroute.bean.OrderItem;
import com.github.cf.dbroute.dao.LoanMapper;
import com.github.cf.dbroute.dao.OrderItemMapper;
import com.github.cf.dbroute.dao.OrderMapper;
import com.github.cf.dbroute.router.RouteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 7:02 pm
 */
@Service
public class ShardingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingService.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 有事务 分库分表
     * @param order
     */
    @DoRoute
    public void insertAndSelectOrder(Order order) {
        LOGGER.info("ShardingService.insertAndSelectOrder begin...");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                orderMapper.insert(order);
                List<Order> newOrder = orderMapper.selectByUserId(order.getUserId());
                LOGGER.info("selectOrder finish, new order:{}", newOrder);
            }
        });
        LOGGER.info("ShardingService.insertAndSelectOrder order end...");
    }

    /**
     * 无事务 不分库分表
     * @param loan
     */
    public void insertAndSelectLoan(Loan loan) {
        loanMapper.insert(loan);
        List<Loan> newLoan = loanMapper.selectByUserId(loan.getUserId());
        LOGGER.info("selectLoan finish, new loan:{}", newLoan);
    }

    /**
     * 有事务 不分库分表
     * @param loan
     */
    @Transactional
    public void insertAndSelectLoanWithTransaction(Loan loan) {
        loanMapper.insert(loan);
        List<Loan> newLoan = loanMapper.selectByUserId(loan.getUserId());
        LOGGER.info("selectLoan finish, new loan:{}", newLoan);
    }

    @Transactional
    @DoRoute(routeType = RouteType.TABLE)
    public void insertOrderItem(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
        List<OrderItem> newOrderItem = orderItemMapper.selectByUserId(orderItem.getUserId());
        LOGGER.info("selectOrderItem finish, new OrderItem:{}", newOrderItem);
    }
}

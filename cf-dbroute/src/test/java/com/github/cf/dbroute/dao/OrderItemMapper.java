package com.github.cf.dbroute.dao;

import com.github.cf.dbroute.bean.OrderItem;

import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 8:03 pm
 */
public interface OrderItemMapper {

    int insert(OrderItem orderItem);

    List<OrderItem> selectByUserId(Integer userId);
}

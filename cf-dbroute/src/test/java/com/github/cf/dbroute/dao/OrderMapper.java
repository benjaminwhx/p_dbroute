package com.github.cf.dbroute.dao;

import com.github.cf.dbroute.bean.Order;

import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 7:04 pm
 */
public interface OrderMapper {

    int insert(Order order);

    List<Order> selectByUserId(Integer userId);
}

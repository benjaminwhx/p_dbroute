package com.github.cf.dbroute.bean;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 4:07 pm
 */
public class Order {
    private Integer orderId;
    private Integer userId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                '}';
    }
}

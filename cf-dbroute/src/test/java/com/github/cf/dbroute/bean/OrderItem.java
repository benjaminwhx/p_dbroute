package com.github.cf.dbroute.bean;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 9:05 pm
 */
public class OrderItem {
    private Integer itemId;
    private Integer orderId;
    private Integer userId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

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
}

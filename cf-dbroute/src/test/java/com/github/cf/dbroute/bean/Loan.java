package com.github.cf.dbroute.bean;

import java.math.BigDecimal;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 8:21 pm
 */
public class Loan {
    private Long id;
    private String loanId;
    private String userId;
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", loanId='" + loanId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                '}';
    }
}

package com.github.cf.dbroute.dao;

import com.github.cf.dbroute.bean.Loan;

import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-09
 * Time: 8:03 pm
 */
public interface LoanMapper {

    int insert(Loan loan);

    List<Loan> selectByUserId(String userId);
}

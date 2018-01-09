package com.github.cf.dbroute;

import com.github.cf.dbroute.bean.Loan;
import com.github.cf.dbroute.utils.StringUtils;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 8:11 pm
 */
public class SpringElTest {

    @Test
    public void testELExpression() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object[] obj = new Object[1];
        obj[0] = buildLoan();
        String expressionStr = StringUtils.substringBetween("#{[0].userId}", "#{", "}");// 去掉标示符号,spring不要这个.
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expressionStr);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(obj);
        String value = exp.getValue(context, String.class);
        System.out.println(value);
    }

    private Loan buildLoan() {
        Loan loan = new Loan();
        loan.setId(12345L);
        loan.setLoanId("A12390234234");
        loan.setAmount(new BigDecimal("3000"));
        loan.setUserId("userA");
        return loan;
    }
}

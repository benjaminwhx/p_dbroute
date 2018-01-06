package com.github.sharding.jdbc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-06
 * Time: 11:18 pm
 */
public class DBRouteTest {

    public static void main(String[] args) throws SQLException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        DataSource dataSource = (DataSource) context.getBean("shardingDataSource");
        Connection connection = dataSource.getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement("select * from t_order where user_id = 100 and order_id = 101");
        ResultSet resultSet = prepareStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            System.out.println(resultSet.getInt(2));
        }
    }
}

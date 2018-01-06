package com.github.cf.constant;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-07
 * Time: 02:06 am
 */
public enum RuleTypeEnum {
    INT(0),
    DATE(1),
    /**
     * 根据字符串
     */
    STR(2);

    int type;

    RuleTypeEnum(int type) {
        this.type = type;
    }
}

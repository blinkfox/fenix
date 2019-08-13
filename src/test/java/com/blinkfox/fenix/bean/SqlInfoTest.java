package com.blinkfox.fenix.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * SqlInfo Test.
 *
 * @author blinkfox on 2019-08-13.
 */
public class SqlInfoTest {

    /**
     * 测试移除的功能.
     */
    @Test
    public void removeIfExist() {
        // 初始化参数.
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql("SELECT * FROM t_user WHERE 1 = 1 AND name LIKE 'zhang%' 1 <> 1 OR age = 13");

        sqlInfo.removeIfExist(" 1 = 1 AND").removeIfExist(" 1 <> 1").removeIfExist(" abc  ");
        assertEquals("SELECT * FROM t_user WHERE name LIKE 'zhang%' OR age = 13", sqlInfo.getSql());
    }

}

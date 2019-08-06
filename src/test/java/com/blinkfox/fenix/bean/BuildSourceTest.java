package com.blinkfox.fenix.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.blinkfox.fenix.consts.Const;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * BuildSource Test.
 *
 * @author blinkfox on 2019-08-06.
 */
public class BuildSourceTest {

    /**
     * 构建 SQL 的资源.
     */
    private static BuildSource source;

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        source = new BuildSource("", new SqlInfo(), null, null);
    }

    /**
     * 重置前缀的方法.
     */
    @Test
    public void testResetPrefix() {
        source.setPrefix(null).resetPrefix();
        assertEquals(Const.ONE_SPACE, source.getPrefix());
    }

    /**
     * 重置后缀的方法.
     */
    @Test
    public void testResetSuffix() {
        source.setSymbol(null).resetSymbol();
        assertEquals(Const.ONE_SPACE, source.getSymbol());
    }

    /**
     * 测试设置SqlInfo对象的方法.
     */
    @Test
    public void testSetSqlInfo() {
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setJoin(new StringBuilder("SELECT id, name").append(" FROM user"));
        source.setSqlInfo(sqlInfo);
        assertEquals("SELECT id, name FROM user", source.getSqlInfo().getJoin().toString());
    }

    /**
     * 测试设置Node节点的方法.
     */
    @Test
    public void testSetNode() {
        source.setNode(null);
        assertNull(source.getNode());
    }

}

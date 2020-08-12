package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.provider.SqlExceptionProvider;
import com.blinkfox.fenix.provider.UserSqlInfoProvider;
import java.util.HashMap;
import org.junit.Test;

/**
 * ClassMethodInvoker Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class ClassMethodInvokerTest {

    /**
     * 测试调用一个不存在的方法时抛出异常的情况.
     */
    @Test(expected = FenixException.class)
    public void invoke() {
        ClassMethodInvoker.invoke(UserSqlInfoProvider.class, "abc", new HashMap<>());
    }

    /**
     * 测试 SQL 提供器无公共构造方法时调用异常的情况.
     */
    @Test(expected = FenixException.class)
    public void invoke2() {
        ClassMethodInvoker.invoke(SqlExceptionProvider.class, "build", new HashMap<>());
    }

    /**
     * 测试调用一个私有方法时抛出异常的情况.
     */
    @Test(expected = FenixException.class)
    public void invoke3() {
        ClassMethodInvoker.invoke(UserSqlInfoProvider.class, "privateMethod", new HashMap<>());
    }

}

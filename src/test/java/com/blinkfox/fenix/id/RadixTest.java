package com.blinkfox.fenix.id;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Radix} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
public class RadixTest {

    private static final int N = 2019;

    /**
     * 测试转换的方法.
     */
    @Test
    public void testToString() {
        Assert.assertEquals("11111100011", Radix.toString(N, 2));
        Assert.assertEquals("3743", Radix.toString(N, 8));
        Assert.assertEquals("7E3", Radix.toString(N, 16));
        Assert.assertEquals("7e3", Long.toString(N, 16));
        Assert.assertEquals("1K3", Radix.toString(N, 36));
        Assert.assertEquals("1k3", Long.toString(N, 36));
        Assert.assertEquals("WZ", Radix.toString(N, 62));
    }

    /**
     * 测试转换的方法.
     */
    @Test
    public void testToNumber() {
        Assert.assertEquals(N, Radix.toNumber("11111100011", 2));
        Assert.assertEquals(N, Radix.toNumber("3743", 8));
        Assert.assertEquals(N, Radix.toNumber("2019", 10));
        Assert.assertEquals(N, Radix.toNumber("7E3", 16));
        Assert.assertEquals(N, Radix.toNumber("1K3", 36));
        Assert.assertEquals(N, Radix.toNumber("WZ", 62));
    }

    /**
     * 随机一个十进制的数字对其进行 36 进制的处理的准确性测试.
     */
    @Test
    public void testWithRandom() {
        for (int i = 0; i < 1000; i++) {
            long n = new Random().nextLong();
            Assert.assertEquals(Long.toString(n, 2), Radix.toString(n, 2));
            Assert.assertEquals(Long.toString(n, 8), Radix.toString(n, 8));
            // 16 进制和 36 进制，这里要忽略大小写来比较，Java 生成的字母默认是小写，
            // 为了保证 ASCII 的大小顺序，Radix 类生成的进制顺序是先数字，大写字母，再小写字母的顺序.
            Assert.assertTrue(Long.toString(n, 16).equalsIgnoreCase(Radix.toString(n, 16)));
            Assert.assertTrue(Long.toString(n, 36).equalsIgnoreCase(Radix.toString(n, 36)));
        }
    }

    /**
     * 生成测试数据的大小比较.
     */
    @Test
    public void testWithMoreThan() {
        for (int i = 0; i < 10000; i++) {
            long n = new Random().nextInt(235000121) + 232301212;
            long m = n + new Random().nextInt(434823023) + 1;
            Assert.assertTrue(Radix.toString(m, 62).compareTo(Radix.toString(n, 62)) > 0);
            Assert.assertTrue(Long.toString(m, 36).compareTo(Long.toString(n, 36)) > 0);
        }
    }

}
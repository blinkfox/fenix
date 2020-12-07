package com.blinkfox.fenix.id;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 进制转换相关的工具类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Radix {

    /**
     * 62 进制数需要的 char 数组字符表，
     * 为了保证生成的各个进制数的字符串保证 ASCII 的大小顺序，Radix 类生成的进制顺序是先数字，大写字母，再小写字母的顺序.
     */
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 默认的 10 进制常量.
     */
    private static final int RADIX_10 = 10;

    /**
     * 支持的最大进制数.
     */
    static final int RADIX_62 = DIGITS.length;

    private static final Map<Character, Integer> digitMap = new HashMap<>();

    static {
        for (int i = 0, len = DIGITS.length; i < len; i++) {
            digitMap.put(DIGITS[i], i);
        }
    }

    /**
     * 将长整型数值转换为指定的进制数（最大支持 62 进制，字母数字已经用尽）.
     *
     * @param i 待转换数字
     * @param radix 进制数
     * @return 转换后的字符串
     */
    public static String toString(long i, int radix) {
        if (radix < Character.MIN_RADIX || radix > RADIX_62) {
            radix = RADIX_10;
        }

        if (radix == RADIX_10) {
            return Long.toString(i);
        }

        boolean negative = (i < 0);
        if (!negative) {
            i = -i;
        }

        final int size = 65;
        int charPos = 64;
        char[] buf = new char[size];

        while (i <= -radix) {
            buf[charPos--] = DIGITS[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = DIGITS[(int) (-i)];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (size - charPos));
    }

    /**
     * 抛出数字格式化异常.
     *
     * @param s 字符串s
     * @return 异常
     */
    private static NumberFormatException throwNumberFormatException(String s) {
        return new NumberFormatException("For input string: \"" + s + "\"");
    }

    /**
     * 将指定 {@code radix} 进制数的字符串转换为 {@code 10} 进制的长整型数字.
     *
     * @param s 数字字符串
     * @param radix 进制数
     * @return 10 进制长整型数字
     */
    public static long toNumber(String s, int radix) {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Numbers.MIN_RADIX (2).");
        }
        if (radix > RADIX_62) {
            throw new NumberFormatException("radix " + radix + " greater than Numbers.MAX_RADIX (62).");
        }

        int len = s.length();
        if (len == 0) {
            throw throwNumberFormatException(s);
        }

        boolean negative = false;
        int i = 0;
        long limit = -Long.MAX_VALUE;
        long multmin;

        char firstChar = s.charAt(0);
        if (firstChar < '0') {
            if (firstChar == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
            } else if (firstChar != '+') {
                throw throwNumberFormatException(s);
            }

            if (len == 1) {
                throw throwNumberFormatException(s);
            }
            i++;
        }
        multmin = limit / radix;

        long result = 0;
        Integer digit;
        while (i < len) {
            digit = digitMap.get(s.charAt(i++));
            if (digit == null || digit < 0 || result < multmin) {
                throw throwNumberFormatException(s);
            }

            result *= radix;
            if (result < limit + digit) {
                throw throwNumberFormatException(s);
            }
            result -= digit;
        }
        return negative ? result : -result;
    }

    /**
     * 根据对应的整数和移位数求得对应的字符串值.
     *
     * @param val 整数
     * @param digits 移位数
     * @return 字符串值
     */
    public static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return toString(hi | (val & (hi - 1)), RADIX_62).substring(1);
    }

}

package com.blinkfox.fenix.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 字符串操作的工具类.
 *
 * @author blinkfox on 2019-08-04.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringHelper {
    
    /**
     * 替换空格、换行符、制表符等为一个空格的预编译正则表达式模式对象.
     */
    private static final Pattern BLANK_PATTERN = Pattern.compile("\\|\t|\r|\n");

    /**
     * XML文件扩展名常量.
     */
    private static final String XML_EXT = ".xml";

    /**
     * Java文件扩展名常量.
     */
    private static final String JAVA_EXT = ".java";

    /**
     * Java 编译后的 class 文件扩展名.
     */
    private static final String CLASS_EXT = ".class";

    /**
     * 将字符串中的“大空格（包括换行、回车、制表符）”等转成普通空格来处理，最后去掉所有多余空格.
     *
     * @param str 待处理的字符串
     * @return 替换后的字符串
     */
    public static String replaceBlank(String str) {
        Matcher m = BLANK_PATTERN.matcher(str);
        return m.replaceAll("").replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * 判断给定的字符串是否为空字符串，包括空格也算.
     *
     * @param str 待判断字符串
     * @return 是否的布尔结果
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }

        // 遍历每个空格是否有非空格元素
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空.
     *
     * @param str 待判断字符串
     * @return 是否的布尔结果
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 快速连接参数中的字符串.
     *
     * @param objs 各对象参数
     * @return 连接后的字符串
     */
    public static String concat(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object o: objs) {
            sb.append(o == null ? "" : o.toString());
        }
        return sb.toString();
    }

    /**
     * 判断文件全路径是否是已某扩展名结尾的文件.
     *
     * @param filePath 文件全路径名
     * @param ext 文件扩展名
     * @return 布尔值
     */
    private static boolean isExtFile(String filePath, String ext) {
        return filePath != null && filePath.endsWith(ext);
    }

    /**
     * 根据给定的文件路径判断文件是否是XML文件.
     *
     * @param filePath 文件路径
     * @return 布尔值
     */
    public static boolean isXmlFile(String filePath) {
        return isExtFile(filePath, XML_EXT);
    }

    /**
     * 根据给定的文件路径判断文件是否是.java文件.
     *
     * @param filePath 文件路径
     * @return 布尔值
     */
    public static boolean isJavaFile(String filePath) {
        return isExtFile(filePath, JAVA_EXT);
    }

    /**
     * 根据给定的文件路径判断文件是否是.class文件.
     *
     * @param filePath 文件路径
     * @return 布尔值
     */
    public static boolean isClassFile(String filePath) {
        return isExtFile(filePath, CLASS_EXT);
    }

}
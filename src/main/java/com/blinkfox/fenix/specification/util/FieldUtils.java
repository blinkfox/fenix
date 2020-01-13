package com.blinkfox.fenix.specification.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * FieldUtils
 * @description 直接重apache库中复制得来，仅使用了少数方法，避免引入整个库。
 * @author YangWenpeng
 * @date 2019年9月16日 下午2:56:28
 * @version v1.0.0
 */
public class FieldUtils {
    
    private FieldUtils() {
    }

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls
     *            the {@link Class} to query
     * @return an array of Fields (possibly empty).
     * @throws IllegalArgumentException
     *             if the class is {@code null}
     * @since 3.2
     */
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls
     *            the {@link Class} to query
     * @return an array of Fields (possibly empty).
     * @throws IllegalArgumentException
     *             if the class is {@code null}
     * @since 3.2
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Assert.notNull(cls, "The class must not be null");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
}

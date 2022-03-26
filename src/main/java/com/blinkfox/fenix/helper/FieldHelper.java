package com.blinkfox.fenix.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 字段操作的相关工具类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldHelper {

    /**
     * 从一个类的 {@code Class} 类中获取所有的 {@link Field} 字段的数组.
     *
     * @param cls {@code Class} 类实例
     * @return 所有的 {@link Field} 字段的数组
     */
    public static Field[] getAllFields(final Class<?> cls) {
        return getAllFieldsList(cls).toArray(new Field[0]);
    }

    /**
     * 从一个类的 {@code Class} 类中获取所有的 {@link Field} 字段的集合.
     *
     * @param cls {@code Class} 类实例
     * @return 所有的 {@link Field} 字段的 List 集合
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Assert.notNull(cls, "The class must not be null");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            Collections.addAll(allFields, currentClass.getDeclaredFields());
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

}

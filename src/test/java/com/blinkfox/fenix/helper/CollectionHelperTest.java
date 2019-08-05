package com.blinkfox.fenix.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.blinkfox.fenix.consts.Const;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * CollectionHelper Test.
 *
 * @author blinkfox on 2019-08-05.
 */
public class CollectionHelperTest {

    /**
     * 测试是否不为空的方法.
     */
    @Test
    public void testIsNotEmpty() {
        assertFalse(CollectionHelper.isNotEmpty(null));
        assertFalse(CollectionHelper.isNotEmpty(new Object[]{}));
        assertTrue(CollectionHelper.isNotEmpty(new Object[]{1}));
    }

    /**
     * 测试是否不为空的方法.
     */
    @Test
    public void testCollectionIsEmpty() {
        List<String> names = new ArrayList<>();
        assertTrue(CollectionHelper.isEmpty(null));
        assertTrue(CollectionHelper.isEmpty(names));
        names.add("Tom");
        assertFalse(CollectionHelper.isEmpty(names));
    }

    /**
     * 测试转换为数组的方法.
     */
    @Test
    public void testToArray() {
        assertArrayEquals(new Object[]{3}, CollectionHelper.toArray(3, 0));
        assertArrayEquals(new Object[]{1, 2}, CollectionHelper.toArray(new Object[]{1, 2}, Const.OBJTYPE_ARRAY));

        List<String> lists = new ArrayList<>();
        lists.add("hello");
        lists.add("world");
        assertArrayEquals(new Object[]{"hello", "world"}, CollectionHelper.toArray(lists, Const.OBJTYPE_COLLECTION));
    }

}

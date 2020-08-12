package com.blinkfox.fenix.specification.handler.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二元组实体类.
 *
 * @author blinkfox on 2020-01-29.
 * @since v2.2.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Pair<L, R> {

    /**
     * 左边的元素.
     */
    private final L left;

    /**
     * 右边的元素.
     */
    private final R right;

    /**
     * 构造二元组 {@link Pair} 的实例.
     *
     * @param left 左边的元素
     * @param right 第二个元素
     * @param <L> 左边的元素的类型
     * @param <R> 右边的元素的类型
     * @return {@link Pair} 的实例
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

}
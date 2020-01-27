package com.blinkfox.fenix.specification.handler.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于区间查询的开始值和结束值的实体类.
 *
 * @author blinkfox on 2020-01-28.
 * @since v2.2.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BetweenValue<T extends Comparable<T>> {

    /**
     * 开始值.
     */
    private T start;

    /**
     * 结束值.
     */
    private T end;

    /**
     * 构造含有开始值和结束值的用于区间查询 {@link BetweenValue} 实例.
     *
     * @param start 开始值，该值的类型必须实现了 {@link Comparable} 接口，表明才能进行大小的比较
     * @param end 结束值，该值的类型必须实现了 {@link Comparable} 接口，表明才能进行大小的比较
     * @param <T> 区间比较的值类型，该类型必须实现了 {@link Comparable} 接口
     * @return {@link BetweenValue} 实例
     */
    public static <T extends Comparable<T>> BetweenValue<T> of(T start, T end) {
        return new BetweenValue<>(start, end);
    }

    /**
     * 构造仅含有开始值的用于区间查询的 {@link BetweenValue} 实例.
     *
     * @param start 开始值，该值的类型必须实现了 {@link Comparable} 接口，表明才能进行大小的比较
     * @param <T> 区间比较的值类型，该类型必须实现了 {@link Comparable} 接口
     * @return {@link BetweenValue} 实例
     */
    public static <T extends Comparable<T>> BetweenValue<T> ofStart(T start) {
        return new BetweenValue<>(start, null);
    }

    /**
     * 构造仅含有结束值的用于区间查询的 {@link BetweenValue} 实例.
     *
     * @param end 结束值，该值的类型必须实现了 {@link Comparable} 接口，表明才能进行大小的比较
     * @param <T> 区间比较的值类型，该类型必须实现了 {@link Comparable} 接口
     * @return {@link BetweenValue} 实例
     */
    public static <T extends Comparable<T>> BetweenValue<T> ofEnd(T end) {
        return new BetweenValue<>(null, end);
    }

}

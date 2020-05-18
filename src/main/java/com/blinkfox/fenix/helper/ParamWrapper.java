package com.blinkfox.fenix.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于将上下文参数包装为 Map 的包装器工具类.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
public final class ParamWrapper {

    /**
     * 用于封装参数的 map.
     */
    private Map<String, Object> paramMap;

    /**
     * 仅有 map 的私有构造方法.
     *
     * @param paramMap Map型参数
     */
    private ParamWrapper(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 创建新的 ParamWrapper 实例.
     *
     * @return ParamWrapper实例
     */
    public static ParamWrapper newInstance() {
        return new ParamWrapper(new HashMap<>());
    }

    /**
     * 创建新的 ParamWrapper 实例，并传入需要作为上下文参数的 map.
     *
     * @param paramMap Map型参数
     * @return ParamWrapper实例
     */
    public static ParamWrapper newInstance(Map<String, Object> paramMap) {
        return new ParamWrapper(paramMap);
    }

    /**
     * 创建新的 ParamWrapper 实例，并注入一对 key、value 的值.
     *
     * @param key 键
     * @param value 值
     * @return ParamWrapper实例
     */
    public static ParamWrapper newInstance(String key, Object value) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(key, value);
        return new ParamWrapper(paramMap);
    }

    /**
     * 存放参数的 key 和 value 值.
     *
     * @param key 键
     * @param value 值
     * @return ParamWrapper 实例
     */
    public ParamWrapper put(String key, Object value) {
        this.paramMap.put(key, value);
        return this;
    }

    /**
     * 获取 paramMap 的 Map 对象.
     *
     * @return paramMap
     */
    public Map<String, Object> toMap() {
        return paramMap;
    }

}

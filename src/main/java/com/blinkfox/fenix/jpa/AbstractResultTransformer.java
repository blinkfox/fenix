package com.blinkfox.fenix.jpa;

import java.util.List;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

/**
 * 抽象的、公共的结果转换类.
 *
 * @see FenixResultTransformer
 * @author blinkfox on 2022-03-25.
 * @since 2.7.0
 */
public abstract class AbstractResultTransformer implements ResultTransformer {

    /**
     * 要转换类型的 class 实例.
     */
    protected Class<?> resultClass;

    /**
     * 结果类 class 的 Setter 方法.
     *
     * @param resultClass 结果类的 class
     */
    public abstract void setResultClass(Class<?> resultClass);

    /**
     * 设置默认的结果类 class 的值.
     *
     * @param resultClass 结果类的 class
     */
    protected void setResultClassValue(Class<?> resultClass) {
        Assert.notNull(resultClass, "【Fenix 异常】resultClass cannot be null.");
        this.resultClass = resultClass;
    }

    /**
     * 转换成集合，直接返回集合本身即可.
     *
     * @param list 集合.
     * @return 集合
     */
    @Override
    public List<?> transformList(List list) {
        return list;
    }

}

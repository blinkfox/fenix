package com.blinkfox.fenix.jpa;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

/**
 * 结果转换器.
 *
 * @param <T> 要转换的范型 T
 * @author blinkfox on 2019-10-08.
 */
public class FenixResultTransformer<T> implements ResultTransformer {

    /**
     * 要转换类型的 class 实例.
     */
    private Class<T> transClass;

    /**
     * Tuples are the elements making up each "row" of the query result.
     * The contract here is to transform these elements into the final
     * row.
     *
     * @param tuple   The result elements
     * @param aliases The result aliases ("parallel" array to tuple)
     * @return The transformed row.
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        return null;
    }

    /**
     * Here we have an opportunity to perform transformation on the
     * query result as a whole.  This might be useful to convert from
     * one collection type to another or to remove duplicates from the
     * result, etc.
     *
     * @param collection The result.
     * @return The transformed result.
     */
    @Override
    public List transformList(List collection) {
        return null;
    }

}

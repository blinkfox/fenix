package com.blinkfox.fenix.ar.spec;

import com.blinkfox.fenix.ar.BaseModel;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Fenix 提供的 ActiveRecord 模式的 JpaSpecificationExecutor 相关操作的 Model 接口.
 *
 * <p>当实体类实现本接口时，需要该实体类所对应的 Spring Data JPA 中的 Repository 接口继承自 {@link JpaSpecificationExecutor} 接口。
 * 本 Model 接口几乎没有实现任何快捷方法，你需要通过额外调用本类中的 {@link #getRepository()} 方法来实现
 * {@link JpaSpecificationExecutor} 中的相关功能。
 * </p>
 *
 * @param <T> 实体类的泛型参数
 * @param <R> 实体类所对应的 Repository 接口，且它继承了 JpaSpecificationExecutor 接口
 * @author blinkfox on 2022-03-30.
 * @since 2.7.0
 */
public interface SpecModel<T, R extends JpaSpecificationExecutor<T>> extends BaseModel<R> {

    /**
     * 校验 Repository 接口是否是 {@link JpaSpecificationExecutor} 类型的接口.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    @Override
    default void validExecutor(Object repository) {
        assertNotNullRepository(repository);
        if (!(repository instanceof JpaSpecificationExecutor)) {
            throw new FenixException(StringHelper.format("【Fenix 异常】获取到的 Spring Data JPA 的 Repository "
                    + "接口【{}】不是真正的 JpaSpecificationExecutor 接口。", repository.getClass().getName()));
        }
    }

}

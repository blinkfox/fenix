package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Setter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Fenix 中 ActiveRecord 模式下，用来操作 Repository 和 Model 的核心上下文处理类.
 *
 * @author blinkfox on 2022-03-29.
 * @since 2.7.0
 */
public final class RepositoryModelContext {

    /**
     * 用来缓存各个 repository 的 Map，便于后续快速判断和获取.
     */
    private static final Map<String, Object> repositoryMap = new ConcurrentHashMap<>();

    /**
     * Spring Bean 的应用上下文，用于动态获取 Spring Data JPA 中各种实体所对应的 Repository 的 Bean 实例.
     */
    @Setter
    private static ApplicationContext applicationContext;

    /**
     * 根据实体类所对应的 Repository 在 Spring 容器中 的 Bean 名称来获取对应的 Repository 对象实例.
     *
     * @param repositoryBeanName 实体类所对应的 Repository 在 Spring 容器中 的 Bean 名称
     * @param entityClassName 实体类的 class 名称
     * @param validConsumer 用于校验 Repository 类型是否正确的 Consumer
     * @return 实体类所对应的 Repository 对象实例
     */
    public static Object getRepositoryObject(String repositoryBeanName, String entityClassName,
            Consumer<Object> validConsumer) {
        return repositoryMap.computeIfAbsent(repositoryBeanName, key -> {
            // 尝试判断 Spring 容器中是否存在本实体类所对应的 Repository 的 Bean，不存在就抛出异常.
            if (!applicationContext.containsBean(key)) {
                throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取实体类【{}】所对应的 "
                        + "Spring Data JPA 的 Repository Bean【{}】的实例为 Null，请先定义该实体类的 Repository 接口，并标注"
                        + "【@Repository】注解。", entityClassName, key));
            }

            Object repository = applicationContext.getBean(key);
            validConsumer.accept(repository);
            return repository;
        });
    }

}

package com.blinkfox.fenix.jpa;

import java.util.concurrent.atomic.AtomicBoolean;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

/**
 * 为了兼容 Spring Data JPA v2.3.0 之前的版本，用来修改 {@link FenixQueryLookupStrategy} 中部分方法的类.
 *
 * @author blinkfox on 2020-05-17.
 * @since v2.3.1
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FenixJpaClassWriter {

    /**
     * {@link DefaultJpaQueryMethodFactory} 类的 class 全路径名常量.
     */
    private static final String JPA_METHOD_FACTORY_NAME =
            "org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory";

    /**
     * 是否有JPA 中的 {@link DefaultJpaQueryMethodFactory} 实现类的 class.
     * 注意该 class 是在 Spring Data JPA v2.3.0 版本才引入的.
     */
    private static Boolean hasJpaMethodClass;

    /**
     * 使用全局变量来标记，是否已经对 class 修改完毕.
     */
    private static final AtomicBoolean modified = new AtomicBoolean(false);

    /**
     * 判断当前的 JPA 版本是否有 {@link DefaultJpaQueryMethodFactory} 类.
     *
     * <p>注意：{@link JpaQueryMethodFactory} 接口和 {@link DefaultJpaQueryMethodFactory} 类是在 JPA v2.3.0 版本才引入的，
     * 为了兼容之前的 Fenix 版本，我们需要判断是否有这个接口的 {@code class}.</p>
     *
     * @return 布尔值
     */
    public static synchronized boolean hasDefaultJpaQueryMethodFactoryClass() {
        if (hasJpaMethodClass != null) {
            return hasJpaMethodClass;
        }

        try {
            Thread.currentThread().getContextClassLoader().loadClass(JPA_METHOD_FACTORY_NAME);
            hasJpaMethodClass = true;
        } catch (ClassNotFoundException e) {
            log.debug("【Fenix -> 'JPA 版本检测' 提示】检查到你的项目中没有【{}】类，说明你的 Spring Data JPA 版本"
                    + "是 v2.3.0 之前的版本.", JPA_METHOD_FACTORY_NAME);
            hasJpaMethodClass = false;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("【Fenix -> 'JPA 版本检测' 提示】检查你的项目中是否有【{}】类时出错，将默认你的 Spring Data JPA 版本"
                        + "是 v2.3.0 之前的版本.", JPA_METHOD_FACTORY_NAME, e);
            } else {
                log.error("【Fenix -> 'JPA 版本检测' 错误】检查你的项目中是否有【{}】类时出错，将默认你的 Spring Data JPA 版本"
                                + "是 v2.3.0 之前的版本，检测时的出错原因是：【{}】，若想看更全的错误堆栈日志信息，请开启 debug 日志级别.",
                        JPA_METHOD_FACTORY_NAME, e.getMessage());
            }
            hasJpaMethodClass = false;
        }
        return hasJpaMethodClass;
    }

    /**
     * 修改 {@link FenixQueryLookupStrategy} 中的两个方法，用字节码注入的方式来创建 Spring Data JPA 老版本中的一些对象.
     *
     * <p>注：</p>
     * <ul>
     *     <li>修改之前先判断 Spring Data JPA 版本是否是 v2.3.0 之前的版本，只有之前的版本才需要修改，且只需修改一次；</li>
     *     <li>由于本方法往往只是初始化时才调用，所以，使用 {@code synchronized} 关键字修饰即可；</li>
     * </ul>
     */
    public static synchronized void modify() {
        // 如果有 DefaultJpaQueryMethodFactory 类，说明 Spring Data JPA 版本是 v2.3.0 及以上，
        // 可不用修改兼容老版本 JPA 的部分 class.
        if (hasDefaultJpaQueryMethodFactoryClass()) {
            log.debug("【Fenix 提示】检测到你的 Spring Data JPA 版本是 v2.3.0 及以上，可不用修改 class 来兼容老版本的 JPA.");
            return;
        }

        // 如果已经修改过了，就直接返回即可.
        if (modified.get()) {
            log.debug("【Fenix 提示】已经修改过了【FenixQueryLookupStrategy.class】中的部分方法，将不再修改.");
            return;
        }

        log.info("【Fenix 提示】检测到你的 Spring Data JPA 版本较低，为了兼容老版本的 JPA，将修改部分 class 字节码来做兼容。"
                + "不过条件允许的话，我仍然建议你将 Spring Data JPA 版本升级到 v2.3.0 及之后的版本.");
        try {
            CtClass ctClass = ClassPool.getDefault().get("com.blinkfox.fenix.jpa.FenixQueryLookupStrategy");

            // 修改 createOldJpaQueryLookupStrategy 方法，创建 JpaQueryLookupStrategy 对象.
            CtMethod lookupStrategyMethod = ctClass.getDeclaredMethod("createOldJpaQueryLookupStrategy");
            lookupStrategyMethod.setBody("{return org.springframework.data.jpa.repository.query."
                    + "JpaQueryLookupStrategy.create($1, $2, $3, $4, $5);}");

            // 修改 createOldFenixJpaQuery 方法，创建 JpaQueryMethod 对象.
            CtMethod ctMethod = ctClass.getDeclaredMethod("createOldFenixJpaQuery");
            ctMethod.setBody("{return new com.blinkfox.fenix.jpa.FenixJpaQuery(new org.springframework.data.jpa"
                    + ".repository.query.JpaQueryMethod($1, $2, $3, $4), $5);}");

            // 冻结 class，并设置是否已修改的值为 true.
            ctClass.toClass();
            modified.getAndSet(true);
        } catch (Exception e) {
            log.error("【Fenix 错误提示】使用 Javassist 修改【FenixQueryLookupStrategy】class 中的代码出错，"
                    + "建议升级 Spring Boot 的版本为 v2.3.0 及之上.", e);
        }
    }

}

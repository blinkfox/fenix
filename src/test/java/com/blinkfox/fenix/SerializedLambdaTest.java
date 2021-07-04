package com.blinkfox.fenix;

import com.blinkfox.fenix.lambda.SerializedLambda;
import lombok.Getter;
import org.junit.Test;


public class SerializedLambdaTest {

    /**
     * 用于测试的 Model
     */
    @Getter
    private static class TestModel extends Parent implements Named {
        private String name;

    }

    @Getter
    private static abstract class Parent {
        private int id;
    }

    // 处理 ISSUE:https://gitee.com/baomidou/mybatis-plus/issues/I13Y8Y，由于 java 本身处理的问题，这里无法获取到实例
    private abstract static class BaseHolder<T extends Named> {

        SerializedLambda toLambda() {
           return SerializedLambda.resolve(T::getName, this.getClass().getClassLoader());
        }

    }

    private static class TestModelHolder extends BaseHolder<TestModel> {
    }

    private interface Named {
        String getName();
    }

    @Test
    public void testLambda() throws Exception {
        SerializedLambda lambda = SerializedLambda.resolve(TestModel::getName, SerializedLambdaTest.class.getClassLoader());
        String implMethodName = lambda.getImplMethodName();
        String className = lambda.getImplClassName();
        System.out.println(className);
        System.out.println(implMethodName);
        System.out.println(className.substring(className.lastIndexOf(".") + 1) );

        // 测试接口泛型获取
        lambda = new TestModelHolder().toLambda();
        // 无法从泛型获取到实现类，即使改泛型参数已经被实现
        System.out.println(lambda.getInstantiatedType() == Named.class);
    }

}

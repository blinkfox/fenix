package com.blinkfox.fenix;

import com.blinkfox.fenix.helper.LambdaHelper;
import lombok.Getter;
import org.junit.Test;


public class SerializedLambdaTest {

    /**
     * 用于测试的 Model
     */
    @Getter
    private static class TestModel extends Parent implements Named {
        private String name;
        private boolean valid;
        private int f;

    }

    @Getter
    private static abstract class Parent {
        private int id;
    }

    private interface Named {
        String getName();
    }

    @Test
    public void testLambda() throws Exception {
        LambdaHelper.LambdaInfo lambdaInfo = LambdaHelper.getLambdaInfo(TestModel::getName);
        String declaredClass = lambdaInfo.getDeclaredClass();
        String declaredMethodName = lambdaInfo.getDeclaredMethodName();
        System.out.println(lambdaInfo);
        System.out.println(declaredClass);
        System.out.println(declaredMethodName);

        System.out.println(LambdaHelper.getLambdaInfo(TestModel::getId));

        System.out.println(LambdaHelper.getProperty(TestModel::getName));
        System.out.println(LambdaHelper.getProperty(TestModel::getF));
        System.out.println(LambdaHelper.getProperty(TestModel::isValid));
    }

}

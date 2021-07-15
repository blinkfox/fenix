package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.exception.FenixException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public final class LambdaHelper {

    private LambdaHelper(){

    }

    public static <T, R> String getProperty(SFunction<T, R> func){
        LambdaInfo lambdaInfo = getLambdaInfo(func);
        String methodName = lambdaInfo.getDeclaredMethodName();

        return methodToProperty(methodName);
    }

    public static <T, R> LambdaInfo getLambdaInfo(SFunction<T, R> func){
        SerializedLambda lambda = getSerializedLambda(func);
        String declaredClass = lambda.getImplClass().replace('/', '.');
        String declaredMethodName = lambda.getImplMethodName();

        return new LambdaInfo(declaredClass, declaredMethodName);
    }

    public static <T, R> SerializedLambda getSerializedLambda(SFunction<T, R> func) {
        try {
            // 直接调用writeReplace
            Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            //反射调用
            return (SerializedLambda)writeReplace.invoke(func);
        } catch (Exception ex){
            throw new FenixException("获取lambda表达式失败", ex);
        }
    }

    private static String methodToProperty(String methodName) {
        return Optional.of(methodName)
                .map(p -> p.startsWith("is") ? p.substring(2) : p)
                .map(p -> (p.startsWith("get") || p.startsWith("set") ? p.substring(3) : p))
                .map(p -> p.length()==1 ? p.toLowerCase(Locale.ENGLISH) : p.substring(0, 1).toLowerCase(Locale.ENGLISH) + p.substring(1))
                .orElse(methodName); // 兜底方案
    }

    @FunctionalInterface
    public static interface SFunction<T, R> extends Function<T, R>, Serializable {

    }

    public static class LambdaInfo {

        private String declaredClass;

        private String declaredMethodName;

        public LambdaInfo(String declaredClass, String declaredMethodName) {
            this.declaredClass = declaredClass;
            this.declaredMethodName = declaredMethodName;
        }

        public String getDeclaredClass() {
            return declaredClass;
        }

        public void setDeclaredClass(String declaredClass) {
            this.declaredClass = declaredClass;
        }

        public String getDeclaredMethodName() {
            return declaredMethodName;
        }

        public void setDeclaredMethodName(String declaredMethodName) {
            this.declaredMethodName = declaredMethodName;
        }

        @Override
        public String toString() {
            return String.join("", Arrays.asList(
                    declaredClass, "::", declaredMethodName
            ));
        }
    }

}

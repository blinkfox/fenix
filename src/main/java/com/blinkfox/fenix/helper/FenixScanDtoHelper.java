package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.jpa.convert.GenericConvert;
import org.springframework.core.convert.support.DefaultConversionService;

import java.lang.annotation.Annotation;
import java.util.Map;

public class FenixScanDtoHelper {

    public static void scanDtoAndRegisterConverts(Class<? extends Annotation> annotationClass, String... basePackage){
        final DefaultConversionService sharedConversionService = (DefaultConversionService) DefaultConversionService.getSharedInstance();
        ClassScannerHelper.scan(basePackage, annotationClass)
                .forEach(clazz -> {
            // 注册类型转换器
            sharedConversionService.addConverter(Map.class, clazz, new GenericConvert(clazz));
        });
    }

}

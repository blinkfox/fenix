package com.blinkfox.fenix.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.domain.Page;

import java.lang.reflect.Method;

public class TransformMethodInterceptor implements MethodInterceptor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 获取方法链的返回结果
		Object result = invocation.proceed();
		Method method = invocation.getMethod();
		Class<?> returnType = method.getReturnType();
		if((result instanceof Page) && (CustomerPage.class.equals(returnType))) {
			CustomerPage<?> page = new PageToCuctomerPageConvert<>().convert((Page)result);
			
			return page;
		}
		
		return result;
	}

}

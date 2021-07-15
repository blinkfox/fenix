package com.blinkfox.fenix.interceptor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageToCustomerPageConvert<T> implements Converter<Page<T>, CustomerPage<T>> {

	@Override
	public CustomerPage<T> convert(Page<T> source) {
		List<T> content = source.getContent();
		Pageable pageable = source.getPageable();
		long total = source.getTotalElements();
		CustomerPage<T> page = new CustomerPage<T>(content, pageable, total);
		
		return page;
	}

}

package com.blinkfox.fenix.interceptor;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

public class CustomerPage<T> extends PageImpl<T> {

	private static final long serialVersionUID = -167746800636492072L;

	public CustomerPage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public CustomerPage(List<T> content) {
		super(content);
	}

	
	// 当前是第几页
	@JSONField(name ="pageNo")
	@Override
	public int getNumber() {
		return super.getNumber();
	}
	
	// 总页数
	@JSONField(name = "pageCount")
	@Override
	public long getTotalElements() {
		return super.getTotalElements();
	}
	
	// 总记录数
	@JSONField(name = "total")
	@Override
	public int getTotalPages() {
		return super.getTotalPages();
	}
	
	// 每页显示多少条数据
	@JSONField(name = "pageSize")
	@Override
	public int getSize() {
		return super.getSize();
	}
	
	// 当前分页的数据
	@JSONField(name = "data")
	@Override
	public List<T> getContent() {
		return super.getContent();
	}
	
	

	// 需要排除的字段 ================================== 开始
	/**
	@JSONField(serialize =false)
	@JsonIgnore
	public List<T> getResults() {
		return super.getContent();
	}
	*/
	
	@JSONField(serialize =false)
	@Override
	public boolean isFirst() {
		return super.isFirst();
	}
	
	@JSONField(serialize =false)
	@Override
	public boolean isLast() {
		return super.isLast();
	}

	@JSONField(serialize =false)
	@Override
	public int getNumberOfElements() {
		return super.getNumberOfElements();
	}

	@JSONField(serialize =false)
	@Override
	public Pageable getPageable() {
		return super.getPageable();
	}
	
	@JSONField(serialize =false)
	@Override
	public Sort getSort() {
		return super.getSort();
	}
	// 需要排除的字段 ============================ 结束
	
	
	
	// 仅仅是为了测试时间格式化(不加注解时显示的全部都是数字)
	@JSONField(format="yyyy-MM-dd HH:mm")
	public Date getTime() {
		return new Date();
	}
	
	// 不加注解时显示的全部都是数字
	public Date getTestTime() {
		return new Date();
	}
	
}

package com.leo.rpc.spring;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.leo.rpc.annotation.RpcService;

public class RpcBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

	public RpcBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

	/**
	 * @Author haien
	 * @Description 注册条件过滤器，将@Mapper注解加入允许扫描的过滤器中，
	 *              如果加入排除扫描的过滤器集合excludeFilter中，则不会扫描
	 * @Date 2019/6/11
	 * @Param []
	 * @return void
	 **/
	protected void registerFilters() {
		addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		return super.doScan(basePackages);
	}
}
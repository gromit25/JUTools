package com.jutools.spring;

import java.lang.reflect.Constructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import com.jutools.StringUtil;

/**
 * 스프링부트의 빈 관련 유틸리티 클래스
 *
 * @author jmsohn
 */
@Component
public class SpringBeanUtil {

	/** 스프링부트 어플리케이션 컨텍스트 */
	@Autowired
	private GenericApplicationContext context;


	/**
	 * 스프링 부트에 빈 수동 등록
	 * 
	 * @param beanName 등록할 빈 명칭
	 * @param obj 등록할 빈 오브젝트
	 * @return 현재 객체
	 */
	public SpringBeanUtil registerBean(String beanName, Object obj) throws Exception {
    
		// 입력값 검증
		if(StringUtil.isBlank(beanName) == true) {
			throw new IllegalArgumentException("'beanName' is null or blank.");
		}

		if(obj == null) {
			throw new IllegalArgumentException("'obj' is null.");
		}

		// 스프링 의존성 주입 수행
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBean(obj);
		factory.initializeBean(obj, beanName);

		// 컨텍스트에 등록
		this.context.getBeanFactory().registerSingleton(beanName, obj);

		// 등록된 빈 반환
		return this;
	}

	/**
	 * 스프링 부트에 빈 수동 등록
	 * 
	 * @param beanName 등록할 빈 명칭
	 * @param clazz 등록할 빈 클래스
	 * @return 현재 객체
	 */
	public SpringBeanUtil registerBean(String beanName, Class<?> clazz) throws Exception {

		// 입력값 검증
		if(clazz == null) {
			throw new IllegalArgumentException("'clazz' is null.");
		}

		// 생성자 획득
		Constructor<?> constructor = clazz.getDeclaredConstructor();

		// 클래스의 객체 생성
		Object obj = constructor.newInstance();

		// 스프링부트에 등록
		return this.registerBean(beanName, obj);
	}

	/**
	 * 스프링 부트에 빈 수동 등록
	 * 
	 * @param beanName 등록할 빈 명칭
	 * @param className 등록할 빈 클래스 문자열
	 * @return 현재 객체
	 */
	public SpringBeanUtil registerBean(String beanName, String className) throws Exception {

		// 입력값 검증
		if(StringUtil.isBlank(className) == true) {
			throw new IllegalArgumentException("'className' is null or blank.");
		}

		// 클래스 로딩
		Class<?> clazz = Class.forName(className);

		// 스프링부트에 등록
		return this.registerBean(beanName, clazz);
	}
}

package com.jutools;

@Component
public class SpringBeanUtil {
  
	@Autowired
	private GenericApplicationContext context;


  /**
   *
   */
	public StringBootUtil registerBean(String beanName, Object obj) throws Exception {
    
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
   *
   */
	public StringBootUtil registerBean(String beanName, Class<?> clazz) throws Exception {

    // 입력값 검증
    if(clazz == null) {
      throw new IllegalArgumentException("'clazz' is null.");
    }
    
    Constructor<?> constructor = clazz.getDeclaredConstructor();
    if(clazz == null) {
      throw new Exception(clazz + " has no default constructor.");
    }

    // 클래스의 객체 생성
    Object obj = constructor.newInstance();

    // 스프링부트에 등록
    return this.registerBean(beanName, obj);
  }

    /**
   *
   */
	public StringBootUtil registerBean(String beanName, String className) throws Exception {

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

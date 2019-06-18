package cn.wulin.spring.boot.temp1;


import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.io.support.ResourcePatternUtils;

import cn.wulin.spring.boot.temp1.enviroment.ObtainServerPort;

@EnableAutoConfiguration
@ComponentScan(basePackages="cn.wulin")
public class SpringBootStarter implements BeanFactoryAware{
	
	private static ConfigurableApplicationContext run;
	
	/*
	 * 
	 * class: SpringApplication.run(String...) line: 355	
	 * code: exceptionReporters = getSpringFactoriesInstances(
	 * line: 355
	 * 
	 * ------------------------------------------
	 * public abstract class AnnotationConfigUtils {

	/**
	 * The bean name of the internally managed Configuration annotation processor.
	 * 
	 * <p> 内部管理的配置注解处理器的bean名称。
	 *
	*public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
			"org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
			
			
			------------------------------------------
			
			//注解@Configuration之所以可以注册bean定义就是来自于次
		if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
	 * ------------------------------------------
	 * Configuration的源码解析:
	 * 
	 * code: AnnotatedElementUtils.getMergedAnnotationAttributes(AnnotatedElement, String, boolean, boolean) line: 390	

	 * 
	 */
	public static void main(String[] args) {
		run = SpringApplication.run(SpringBootStarter.class, args);
		
		System.out.println("-------启动端口: "+ObtainServerPort.getPort());
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory beanFactory2 = (DefaultListableBeanFactory)beanFactory;
		beanFactory2.registerSingleton("123", "456");
		Object bean = beanFactory2.getBean("123");
		Map<String, ApplicationContext> beansOfType = beanFactory2.getBeansOfType(ApplicationContext.class);
//		ConfigurableApplicationContext context
//		beanFactory2.registerBeanDefinition(beanName, beanDefinition);;
		System.out.println(beansOfType);
	}

}

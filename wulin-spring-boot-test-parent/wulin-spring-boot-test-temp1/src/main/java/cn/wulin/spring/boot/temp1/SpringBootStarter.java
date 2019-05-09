package cn.wulin.spring.boot.temp1;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;

@EnableAutoConfiguration
public class SpringBootStarter implements BeanFactoryAware{
	
	/*
	 * class: SpringApplication
	 * code: context = createApplicationContext();
	 * line: 319
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringBootStarter.class, args);
		for (String string : args) {
			continue;
		}
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory beanFactory2 = (DefaultListableBeanFactory)beanFactory;
//		beanFactory2.registerBeanDefinition(beanName, beanDefinition);;
		System.out.println(beanFactory2);
	}

}

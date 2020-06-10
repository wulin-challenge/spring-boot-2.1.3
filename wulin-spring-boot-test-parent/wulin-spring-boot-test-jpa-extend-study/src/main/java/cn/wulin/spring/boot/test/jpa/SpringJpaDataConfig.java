package cn.wulin.spring.boot.test.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import cn.wulin.spring.boot.test.jpa.intercepter.QueryReturnRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = {"org.apel.**.dao", "com.bjhy.**.dao", "cn.wulin.**.dao","com.wulin.**.dao"}, 
							repositoryBaseClass = SimpleJpaRepository.class,repositoryFactoryBeanClass=QueryReturnRepositoryFactoryBean.class)
public class SpringJpaDataConfig {
	
	
}

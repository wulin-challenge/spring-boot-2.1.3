package cn.wulin.spring.boot.test.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


@Configuration
public class JpaConfig{
	
	@Autowired
	DataSource dataSource;
	@Autowired
	private HibernatePropertiesConfig hibernatePropertiesConfig;
	
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		return hibernateJpaVendorAdapter;
	}
//	
//	@Bean
//	public EntityManagerFactory entityManagerFactory(){
//		String persistenceUnitName = "platform";
//		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//		entityManagerFactoryBean.setDataSource(dataSource);
//		entityManagerFactoryBean.setPackagesToScan(new String[] {"org.apel.**.domain", "com.bjhy.**.domain", "cn.wulin.**.domain","com.wulin.**.domain"});
////		entityManagerFactoryBean.setPersistenceUnitPostProcessors(new PlatformPersistenceUnitPostProcessor(persistenceUnitName));
////		Map<String, String> jpaPropertyMap = hibernatePropertiesConfig.hibernateProperties();
////		entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);
//		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
//		entityManagerFactoryBean.afterPropertiesSet();
//		return entityManagerFactoryBean.getObject();
//	}

}

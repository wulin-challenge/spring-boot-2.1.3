package cn.wulin.spring.boot.test.jpa.intercepter;

import javax.persistence.EntityManager;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

public class QueryReturnBeanPostProcessor implements RepositoryProxyPostProcessor{
	
	private EntityManager entityManager;
	
	public QueryReturnBeanPostProcessor(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
		factory.addAdvice(new QueryReturnMethodInterceptor(entityManager,repositoryInformation));
	}

}

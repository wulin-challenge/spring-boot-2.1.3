package cn.wulin.spring.boot.temp1.enviroment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AwareEnviromentTest implements EnvironmentAware{
	
	@Autowired
	private WebServerApplicationContext webServerApplicationContext;

	@Override
	public void setEnvironment(Environment environment) {
		environment.getProperty("server.port");
	}

}

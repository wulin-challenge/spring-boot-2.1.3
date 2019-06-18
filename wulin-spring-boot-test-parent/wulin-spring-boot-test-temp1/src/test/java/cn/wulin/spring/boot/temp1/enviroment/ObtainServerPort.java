package cn.wulin.spring.boot.temp1.enviroment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ObtainServerPort implements EnvironmentAware{
	private static Environment environment;
	
	@Autowired
	private WebServerApplicationContext webServerApplicationContext;
	
	private static int port;

	@Override
	public void setEnvironment(Environment environment) {
		ObtainServerPort.environment = environment;
		int port2 = webServerApplicationContext.getWebServer().getPort();
		ObtainServerPort.port = environment.getProperty("server.port",Integer.class,port2);
	}
	
	public static int getPort() {
		return port;
	}

	public static Environment getEnvironment() {
		return environment;
	}
	
}

package cn.wulin.spring.boot.temp1.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestSmartLifecycle implements SmartLifecycle {
	
	@Autowired
	private Environment environment;

	@Override
	public void start() {
		System.out.println("start");
	}

	@Override
	public void stop() {
		System.out.println("stop");
	}

	@Override
	public boolean isRunning() {
		System.out.println("isRunning");
		return false;
	}

}

/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Event published when a {@link SpringApplication} is starting up and the
 * {@link ApplicationContext} is prepared and ApplicationContextInitializers have been
 * called but before any bean definitions are loaded.
 * 
 * <p> 当一个 {@link SpringApplication} 正在启动并且准备好了{@link ApplicationContext}
 * 和已经调用了ApplicationContextInitializers且在任何bean定义被加载之前发布事件
 *
 * @author Artsiom Yudovin
 * @since 2.1.0
 */
@SuppressWarnings("serial")
public class ApplicationContextInitializedEvent extends SpringApplicationEvent {

	private final ConfigurableApplicationContext context;

	/**
	 * Create a new {@link ApplicationContextInitializedEvent} instance.
	 * 
	 * <p> 创建一个新的{@link ApplicationContextInitializedEvent}实例
	 * 
	 * @param application the current application - 当前应用程序
	 * @param args the arguments the application is running with - 应用程序运行的参数
	 * @param context the context that has been initialized - 已经初始化的上下文
	 */
	public ApplicationContextInitializedEvent(SpringApplication application,
			String[] args, ConfigurableApplicationContext context) {
		super(application, args);
		this.context = context;
	}

	/**
	 * Return the application context.
	 * 
	 * <p> 返回应用程序上下文
	 * @return the context
	 */
	public ConfigurableApplicationContext getApplicationContext() {
		return this.context;
	}

}

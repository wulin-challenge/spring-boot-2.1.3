/*
 * Copyright 2012-2017 the original author or authors.
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

package org.springframework.boot;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * Callback interface used to support custom reporting of {@link SpringApplication}
 * startup errors. {@link SpringBootExceptionReporter reporters} are loaded via the
 * {@link SpringFactoriesLoader} and must declare a public constructor with a single
 * {@link ConfigurableApplicationContext} parameter.
 * 
 * <p> 回调接口被用于支持自定义的SpringApplication启动错误报告,通过{@link SpringFactoriesLoader}并且必须声明一个公共构造器,
 * 该构造器伴随一个单例的{@link ConfigurableApplicationContext}参数加载{@link SpringBootExceptionReporter reporters}
 *
 * @author Phillip Webb
 * @since 2.0.0
 * @see ApplicationContextAware
 */
@FunctionalInterface
public interface SpringBootExceptionReporter {

	/**
	 * Report a startup failure to the user.
	 * 
	 * <p> 向用户报告一个启动失败
	 * 
	 * @param failure the source failure - 故障源
	 * 
	 * @return {@code true} if the failure was reported or {@code false} if default
	 * reporting should occur.
	 * 
	 * <p> 如果报告了失败,则返回true,如果发生默认报告,则返回false
	 */
	boolean reportException(Throwable failure);

}

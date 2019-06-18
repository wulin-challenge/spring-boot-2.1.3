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

package org.springframework.boot.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringValueResolver;

/**
 * A specialization of {@link FormattingConversionService} configured by default with
 * converters and formatters appropriate for most Spring Boot applications.
 * 
 * <p> 翻译: {@link FormattingConversionService}的一个专业化通过默认配置为大多数spring boot 应用程序使用
 * 转化器和适当的格式化器.
 * 
 * <p> Designed for direct instantiation but also exposes the static
 * {@link #addApplicationConverters} and
 * {@link #addApplicationFormatters(FormatterRegistry)} utility methods for ad-hoc use
 * against registry instance.
 * 
 * <P> 翻译: 设计用于直接实例化,但同样暴露了静态的 {@link #addApplicationConverters} 和 
 * {@link #addApplicationFormatters(FormatterRegistry)} 适用方法,为再次注册实例使用
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class ApplicationConversionService extends FormattingConversionService {

	private static volatile ApplicationConversionService sharedInstance;

	public ApplicationConversionService() {
		this(null);
	}

	public ApplicationConversionService(StringValueResolver embeddedValueResolver) {
		if (embeddedValueResolver != null) {
			setEmbeddedValueResolver(embeddedValueResolver);
		}
		configure(this);
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p> 返回一个共享的默认应用程序ConversionService实例，一旦需要就懒惰地构建它。
	 * 
	 * Note: This method actually returns an {@link ApplicationConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 * 
	 * <p> 翻译: 注意: 这个方法实际返回一个 {@link ApplicationConversionService} 实例,然而,为了二者兼容,
	 * {@code ConversionService} 签名已经被保留
	 * 
	 * @return the shared {@code ApplicationConversionService} instance (never
	 * {@code null})
	 * 
	 * <p> 同享的 {@code ApplicationConversionService}实例(永不为null)
	 */
	public static ConversionService getSharedInstance() {
		ApplicationConversionService sharedInstance = ApplicationConversionService.sharedInstance;
		if (sharedInstance == null) {
			synchronized (ApplicationConversionService.class) {
				sharedInstance = ApplicationConversionService.sharedInstance;
				if (sharedInstance == null) {
					sharedInstance = new ApplicationConversionService();
					ApplicationConversionService.sharedInstance = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

	/**
	 * Configure the given {@link FormatterRegistry} with formatters and converters
	 * appropriate for most Spring Boot applications.
	 * @param registry the registry of converters to add to (must also be castable to
	 * ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * 
	 * <p> 要添加到转换器的注册表(同样必须强转为 ConversionService , 例如转换为 ConfigurableConversionService)
	 * @throws ClassCastException if the given FormatterRegistry could not be cast to a
	 * ConversionService
	 */
	public static void configure(FormatterRegistry registry) {
		DefaultConversionService.addDefaultConverters(registry);
		DefaultFormattingConversionService.addDefaultFormatters(registry);
		addApplicationFormatters(registry);
		addApplicationConverters(registry);
	}

	/**
	 * Add converters useful for most Spring Boot applications.
	 * @param registry the registry of converters to add to (must also be castable to
	 * ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a
	 * ConversionService
	 */
	public static void addApplicationConverters(ConverterRegistry registry) {
		addDelimitedStringConverters(registry);
		registry.addConverter(new StringToDurationConverter());
		registry.addConverter(new DurationToStringConverter());
		registry.addConverter(new NumberToDurationConverter());
		registry.addConverter(new DurationToNumberConverter());
		registry.addConverter(new StringToDataSizeConverter());
		registry.addConverter(new NumberToDataSizeConverter());
		registry.addConverterFactory(new StringToEnumIgnoringCaseConverterFactory());
	}

	/**
	 * Add converters to support delimited strings.
	 * @param registry the registry of converters to add to (must also be castable to
	 * ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a
	 * ConversionService
	 */
	public static void addDelimitedStringConverters(ConverterRegistry registry) {
		ConversionService service = (ConversionService) registry;
		registry.addConverter(new ArrayToDelimitedStringConverter(service));
		registry.addConverter(new CollectionToDelimitedStringConverter(service));
		registry.addConverter(new DelimitedStringToArrayConverter(service));
		registry.addConverter(new DelimitedStringToCollectionConverter(service));
	}

	/**
	 * Add formatters useful for most Spring Boot applications.
	 * @param registry the service to register default formatters with
	 */
	public static void addApplicationFormatters(FormatterRegistry registry) {
		registry.addFormatter(new CharArrayFormatter());
		registry.addFormatter(new InetAddressFormatter());
		registry.addFormatter(new IsoOffsetFormatter());
	}

}

package cn.wulin.spring.boot.test.jpa.intercepter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.QueryAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@QueryAnnotation
@Documented
public @interface CustomQuery {
	
	/**
	 * 分隔符
	 * @return
	 */
	String value() default "_";
	
	/**
	 * 自定义本地查询,true:走自定义的本地sql查询,不走jpa的通用查询
	 */
	boolean nativeQuery() default false;
	
	/**
	 * 当返回类型与实际返回类型不一致时是否进行数据转换
	 * @return
	 */
	boolean convert() default false;

}

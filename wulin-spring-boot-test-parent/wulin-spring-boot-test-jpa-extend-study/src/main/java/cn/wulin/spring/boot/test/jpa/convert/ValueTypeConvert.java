package cn.wulin.spring.boot.test.jpa.convert;

/**
 * 值类型转换接口
 * @author wubo
 *
 * @param <O> original 要转换值得原始类型
 * @param <R> return 返回值得类型
 */
public interface ValueTypeConvert{
	
	/**
	 * 将 O 类型的值转换为 R类型的值
	 * @param originalValue 原始类型的值 
	 * @return 返回R类型的值
	 */
	Object convertValue(Object originalValue);
}
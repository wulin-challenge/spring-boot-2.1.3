package cn.wulin.spring.boot.test.jpa.convert;

import java.math.BigDecimal;

/**
 * 将 BigDecimal 转换为 Long或者long
 * @author wulin
 *
 */
public class BigDecimalConvertNumber extends AbstractValueTypeConvert{

	public BigDecimalConvertNumber(Class<?> returnClass) {
		super(BigDecimal.class, returnClass);
	}

	@Override
	protected Object doConvertValue(Object originalValue) {
		if(originalValue == null) {
			return null;
		}
		BigDecimal original = (BigDecimal)originalValue;
		if(int.class == returnClass || Integer.class == returnClass) {
			return original.intValue();
		}
		
		if(long.class == returnClass || Long.class == returnClass) {
			return original.longValue();
		}
		return originalValue;
	}

}

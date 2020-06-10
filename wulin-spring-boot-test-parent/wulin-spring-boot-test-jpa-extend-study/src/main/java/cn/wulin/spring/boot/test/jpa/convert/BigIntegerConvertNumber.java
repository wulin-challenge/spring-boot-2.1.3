package cn.wulin.spring.boot.test.jpa.convert;

import java.math.BigInteger;

/**
 * 将 BigInteger 转换为 Long或者long
 * @author wulin
 *
 */
public class BigIntegerConvertNumber extends AbstractValueTypeConvert{

	public BigIntegerConvertNumber(Class<?> returnClass) {
		super(BigInteger.class, returnClass);
	}

	@Override
	protected Object doConvertValue(Object originalValue) {
		if(originalValue == null) {
			return null;
		}
		BigInteger original = (BigInteger)originalValue;
		if(int.class == returnClass || Integer.class == returnClass) {
			return original.intValue();
		}
		
		if(long.class == returnClass || Long.class == returnClass) {
			return original.longValue();
		}
		return originalValue;
	}

}

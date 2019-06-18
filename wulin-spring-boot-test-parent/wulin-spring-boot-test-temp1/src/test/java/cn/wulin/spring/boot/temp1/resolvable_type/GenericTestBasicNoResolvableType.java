package cn.wulin.spring.boot.temp1.resolvable_type;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.ResolvableType;

/**
 * 基于 ResolvableType测试泛型信息
 *
 */
public class GenericTestBasicNoResolvableType {
	
	@Test
	public void genericTest() {
		GenericEventPojo<String> genericEventPojo = new GenericEventPojo<String>("123");
		ResolvableType[] generics = genericEventPojo.getResolvableType().getGenerics();
		
		GenericAB<String, String> ss = new GenericAB<String, String>("a", "b");
		GenericAB<Integer, String> ss1 = new GenericAB<Integer, String>(1, "b");
		
		ResolvableType aResolvableType = ss.getAResolvableType();
		ResolvableType bResolvableType = ss.getBResolvableType();
		
		ResolvableType aResolvableType1 = ss1.getAResolvableType();
		ResolvableType bResolvableType1 = ss1.getBResolvableType();
		
		System.out.println();
		
		Map<String,Integer> xx = new HashMap<>();
		xx.put("1", 12);
		
		ResolvableType forInstance = ResolvableType.forInstance("1");
		
		
		System.out.println();
	}

}

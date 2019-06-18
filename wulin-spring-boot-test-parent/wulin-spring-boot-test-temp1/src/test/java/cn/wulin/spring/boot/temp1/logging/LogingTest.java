package cn.wulin.spring.boot.temp1.logging;

public class LogingTest {
	
	public static void main(String[] args) {
		LogUtil.info("main");
		new LogDeduceTest().logDeduceTest();
	}

}

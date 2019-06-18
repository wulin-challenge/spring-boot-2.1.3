package cn.wulin.spring.boot.temp1.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtil {

	public static void info(String info) {
		RuntimeException runtime = new RuntimeException();
		StackTraceElement[] stackTrace = runtime.getStackTrace();
		Class<?> clazz = LogUtil.class;
		if(stackTrace != null && stackTrace.length >1) {
			try {
				clazz = Class.forName(stackTrace[1].getClassName());
				int lineNumber = stackTrace[1].getLineNumber();
				info = "第 "+lineNumber+" 行 :"+info;
			} catch (ClassNotFoundException e) {
				clazz = LogUtil.class;
			}
		}
		Log log = LogFactory.getLog(clazz);
		log.info(info);
	}
}

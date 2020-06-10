package cn.wulin.spring.boot.test.jpa.intercepter.sql;

/**
 * 存放子sql的键与值
 * @author 吴波
 *
 */
public class Group {
	private String key;
	private String value;
	public Group(){}
	public Group(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}

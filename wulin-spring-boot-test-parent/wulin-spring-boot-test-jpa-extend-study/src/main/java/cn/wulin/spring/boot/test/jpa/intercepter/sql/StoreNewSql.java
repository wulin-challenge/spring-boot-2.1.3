package cn.wulin.spring.boot.test.jpa.intercepter.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析后的新sql与子sql的存储器
 * @author 吴波
 *
 */
public class StoreNewSql {
	private String sql;
	private String newSql;
	private List<List<Group>> storeSql= new ArrayList<List<Group>>();
	
	public StoreNewSql() {
		super();
	}

	public StoreNewSql(String sql, String newSql,
			List<List<Group>> storeSql) {
		super();
		this.sql = sql;
		this.newSql = newSql;
		this.storeSql = storeSql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getNewSql() {
		return newSql;
	}

	public void setNewSql(String newSql) {
		this.newSql = newSql;
	}

	public List<List<Group>> getStoreSql() {
		return storeSql;
	}

	public void setStoreSql(List<List<Group>> storeSql) {
		this.storeSql = storeSql;
	}
	

}

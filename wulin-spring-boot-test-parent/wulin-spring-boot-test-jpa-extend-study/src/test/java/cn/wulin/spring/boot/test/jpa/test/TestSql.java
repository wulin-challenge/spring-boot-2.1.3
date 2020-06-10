package cn.wulin.spring.boot.test.jpa.test;

import java.util.List;

import org.junit.Test;

import cn.wulin.spring.boot.test.jpa.intercepter.sql.EntityHelper;

public class TestSql {
	
	@Test
	public void getAllColumn() {
		String sql = "select o.id,o.age,r.id as role_id ,r.name as role_name,(select id from xxx left join yyyy on id = xx) as xy from User o left join o.role r where o.password = ?1";
		
		List<String> allColumns = EntityHelper.getAllColumns(sql);
		
//		EntityHelper.getEntityList(em, sql, clazz)
		System.out.println();
	}

}

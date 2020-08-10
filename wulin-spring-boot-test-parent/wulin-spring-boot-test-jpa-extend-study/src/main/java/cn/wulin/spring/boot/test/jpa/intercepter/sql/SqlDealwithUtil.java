package cn.wulin.spring.boot.test.jpa.intercepter.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL处理工具类
 * @author wulin
 *
 */
public class SqlDealwithUtil {
	private static final ConcurrentMap<String,SqlCache> SQL_CACHE = new ConcurrentHashMap<String,SqlCache>();
	
	/**
	 * 通过查询sql得到countsql
	 * @param sql
	 * @return
	 */
	public static String getCountSql(String sql){
		return getSqlCache(sql).getCountSql();
	}
	
	/**
	 * 得到from后面的sql语句
	 * @param sql
	 * @return
	 */
	public static String getFromSql(String sql){
		return getSqlCache(sql).getFromSql();
	}
	
	/**
	 * 得到所有列名
	 * @param sql
	 * @return
	 */
	public static List<String> getAllColumns(String sql){
		return getSqlCache(sql).getColumns();
	}
	
	private static SqlCache getSqlCache(String sql) {
		if(SQL_CACHE.get(sql) == null) {
			synchronized (sql) {
				if(SQL_CACHE.get(sql) == null) {
					//解析countSQL
					SqlDealwithUtil testRegex = new SqlDealwithUtil();
					StoreNewSql storeNewSql= testRegex.queryParenthesis(sql);
					String countSql = testRegex.getCountSql(storeNewSql);
					
					//解析fromSQL
					String fromSql = testRegex.recoverFromAfter(storeNewSql);
					
					// 解析所有列
					List<String> columns = testRegex.getColumns(sql);
					
					SqlCache sqlCache = new SqlCache(countSql, fromSql, columns);
					SQL_CACHE.put(sql, sqlCache);
				}
			}
		}
		return SQL_CACHE.get(sql);
	}
	
	/**
	 * 得到列名
	 * @param sql
	 * @return
	 */
	private List<String> getColumns(String sql){
		StoreNewSql storeNewSql= this.queryParenthesis(sql);
		if(!StringUtils.isEmpty(storeNewSql.getNewSql())){
			sql = storeNewSql.getNewSql();
		}
		if(StringUtils.isEmpty(sql)){
			System.out.println("所传的sql为空!!");
			return new ArrayList<String>();
		}
		sql = sql.replace("from", "FROM");
		sql = sql.replace("select","SELECT");
		sql = sql.substring(6,sql.indexOf("FROM")).trim();
		return this.getColumns(sql.split(","));
	}
	
	private List<String> getColumns(String[] arr){
		List<String> columns = new ArrayList<String>();
		int blank = 0;
		int smallPoint = 0;
		for (String column : arr) {
			column = column.trim();
			blank = column.split(" ").length;
			if(blank > 1){
				column = column.substring(column.lastIndexOf(" ")).trim();
			}else{
				smallPoint = column.indexOf(".");
				if(smallPoint != -1){
					column = column.substring(smallPoint+1).trim();
				}
			}
			columns.add(column);
		}
		return columns;
	}
	
	/**得到count语句
	 * @param storeNewSql
	 * @return
	 */
	private String getCountSql(StoreNewSql storeNewSql){
		return "SELECT COUNT(*) "+this.recoverFromAfter(storeNewSql);
	}
	
	/**
	 * 恢复from后面的sql语句
	 */
	private String recoverFromAfter(StoreNewSql storeNewSql){
		String newSql = storeNewSql.getNewSql();
		List<List<Group>> storeSql = storeNewSql.getStoreSql();
		newSql = newSql.replace("select", "SELECT").replace("from", "FROM");
		newSql = newSql.substring(newSql.indexOf("FROM"),newSql.length());
		return recoverFromAfter(storeSql,newSql);
	}
	
	/**
	 * 恢复from后面的sql语句
	 */
	private String recoverFromAfter(List<List<Group>> storeSql,String sql){
		StringBuilder fromSql = new StringBuilder();
		String regex = "__\\d+__\\d+__";
		Matcher matcher = Pattern.compile(regex).matcher(sql);
		if(matcher.find()){
			matcher.reset();
			int start = 0;
			int end = 0;
			while(matcher.find()){
				String value = getGroupValue(storeSql,matcher.group());
				end = matcher.start();
				fromSql.append(sql.substring(start, end)).append(value);
				start = matcher.end();
			}
			fromSql.append(sql.substring(start));
			fromSql = new StringBuilder(recoverFromAfter(storeSql,fromSql.toString()));
		}else{
			fromSql = new StringBuilder(sql);
		}
		return fromSql.toString();
	}
	
	/**
	 * 通过key找到value值
	 * @param storeSql
	 * @param key
	 * @return
	 */
	private String getGroupValue(List<List<Group>> storeSql,String key){
		int storeSqlLength = storeSql.size()-1;
		String[] keys = key.split("__");
		Integer i = Integer.parseInt(keys[1]);
		Integer j = Integer.parseInt(keys[2]);
		Group group = storeSql.get(storeSqlLength-i).get(j);
		return group.getValue();
	}
	
	/**
	 * 将sql解析为StoreNewSql对象
	 * @return
	 */
	private StoreNewSql queryParenthesis(String sql){
		StoreNewSql storeNewSql = queryParenthesis(null, null, sql);
		if(storeNewSql.getNewSql() == null){
			storeNewSql.setNewSql(storeNewSql.getSql());
		}
		return storeNewSql;
	}
	
	/**
	 * 将sql解析为StoreNewSql对象
	 * @return
	 */
	private StoreNewSql queryParenthesis( StoreNewSql storeNewSql, Integer howMangLayers,String sql){
		StringBuilder newSql = new StringBuilder();
		String regex = "\\(([^\\(\\)]*)\\)"; 
		Matcher matcher = Pattern.compile(regex).matcher(sql);
		if(howMangLayers == null){
			howMangLayers = 0;
		}
		if(storeNewSql == null){
			storeNewSql = new StoreNewSql();
		}
		List<Group> layersSql = new ArrayList<Group>();
		if(matcher.find()){
			matcher.reset(sql);
			int start = 0;
			int end = 0;
			for(int i=0;matcher.find();i++){
				String groupValue = matcher.group();
				end = matcher.start();
				newSql.append(sql.substring(start, end));
				newSql.append("__"+howMangLayers+"__"+i+"__");
				start =matcher.end();
				layersSql.add(new Group("__"+howMangLayers+"__"+i+"__",groupValue));
			}
			if(start<(sql.length())){
				newSql.append(sql.substring(start, sql.length()));
			}
			howMangLayers ++;
			storeNewSql.setNewSql(newSql.toString());
			storeNewSql = queryParenthesis(storeNewSql,howMangLayers,newSql.toString());
		}
		if(!layersSql.isEmpty()){
			storeNewSql.getStoreSql().add(layersSql);
		}
		storeNewSql.setSql(sql);
		return storeNewSql;
	}
	
	private static class SqlCache{
		private String countSql;
		
		private String fromSql;
		
		private List<String> columns = new ArrayList<String>();
		
		public SqlCache(String countSql, String fromSql, List<String> columns) {
			super();
			this.countSql = countSql;
			this.fromSql = fromSql;
			this.columns = columns;
		}

		public String getCountSql() {
			return countSql;
		}

		public String getFromSql() {
			return fromSql;
		}

		public List<String> getColumns() {
			return columns;
		}
	}
}

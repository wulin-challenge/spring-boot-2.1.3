package cn.wulin.spring.boot.test.jpa.intercepter.sql;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;

/**
 * 将用sql语句查询出来的数据转换为 对象的帮助类
 * @author 吴波
 *
 */
@SuppressWarnings("unchecked")
public class EntityHelper {
	
	/**
	 * 通过查询sql得到countsql
	 * @param sql
	 * @return
	 */
	public static String getCountSql(String sql){
		EntityHelper testRegex = new EntityHelper();
		StoreNewSql storeNewSql= testRegex.queryParenthesis(sql);
		String countSql = testRegex.getCountSql(storeNewSql);
		return countSql;
	}
	
	/**
	 * 得到from后面的sql语句
	 * @param sql
	 * @return
	 */
	public static String getFromSql(String sql){
		EntityHelper testRegex = new EntityHelper();
		StoreNewSql storeNewSql= testRegex.queryParenthesis(sql);
		String fromSql = testRegex.recoverFromAfter(storeNewSql);
		return fromSql;
	}
	
	/**
	 * 得到单个的实体对象,该方已经过时,由getGenericSingleEntity方法替代了
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出
	 * @param params 参数可以是 Map,Object[],List<Object[]>等这些类型,
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	@Deprecated
	public static Object getSingleEntity(EntityManager em,String sql,Object params,Class<?> clazz){
		Query query = new EntityHelper().setQueryParameters(em, sql, params,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.setFirstResult(0).setMaxResults(1).getResultList();
		for (Object[] arrObj : columns) {
			return new EntityHelper().setSingleEntity(sql, arrObj, clazz);
		}
		return null;
	}
	
	/**
	 * 通过泛型得到单个的实体对象
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出
	 * @param params 参数可以是 Map,Object[],List<Object[]>等这些类型,
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	public static <T> T getGenericSingleEntity(EntityManager em,String sql,Object params,Class<?> clazz){
		Query query = new EntityHelper().setQueryParameters(em, sql, params,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.setFirstResult(0).setMaxResults(1).getResultList();
		for (Object[] arrObj : columns) {
			return (T)new EntityHelper().setSingleEntity(sql, arrObj, clazz);
		}
		return null;
	}
	
	
	/**
	 * 得到单个的实体对象,该方已经过时,由getGenericSingleEntity方法替代了
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	@Deprecated
	public static Object getSingleEntity(EntityManager em,String sql,Class<?> clazz){
		Query query = new EntityHelper().setQueryParameters(em, sql, null,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.setFirstResult(0).setMaxResults(1).getResultList();
		for (Object[] arrObj : columns) {
			return new EntityHelper().setSingleEntity(sql, arrObj, clazz);
		}
		return null;
	}
	
	/**
	 * 通过泛型得到真正的单个实体
	 * @param em
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T> T getGenericSingleEntity(EntityManager em,String sql,Class<?> clazz){
			Query query = new EntityHelper().setQueryParameters(em, sql, null,clazz);
			if(query == null){
				return null;
			}
			List<Object[]> columns= query.setFirstResult(0).setMaxResults(1).getResultList();
			for (Object[] arrObj : columns) {
				return (T) new EntityHelper().setSingleEntity(sql, arrObj, clazz);
			}
		return null;
	}
	
	/**
	 * 通过泛型得到实体对象集合
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出,
	 * @param params 参数可以是 Map,Object[],List<Object[]>等这些类型,
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	public static <T> List<T> getGenericEntityList(EntityManager em,String sql,Object params,Class<?> clazz){
		List<T> entityList = new ArrayList<T>();
		Query query = new EntityHelper().setQueryParameters(em, sql, params,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			entityList.add((T)new EntityHelper().setSingleEntity(sql, arrObj, clazz));
		}
		return entityList;
	}
	
	/**
	 * 得到实体对象集合,该方已经过时,由getGenericEntityList方法替代了
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出,
	 * @param params 参数可以是 Map,Object[],List<Object[]>等这些类型,
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	@Deprecated
	public static List<Object> getEntityList(EntityManager em,String sql,Object params,Class<?> clazz){
		List<Object> entityList = new ArrayList<Object>();
		Query query = new EntityHelper().setQueryParameters(em, sql, params,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			entityList.add(new EntityHelper().setSingleEntity(sql, arrObj, clazz));
		}
		return entityList;
	}
	
	/**
	 * 得到实体对象集合,该方已经过时,由getGenericEntityList方法替代了
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出,
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	@Deprecated
	public static List<Object> getEntityList(EntityManager em,String sql,Class<?> clazz){
		List<Object> entityList = new ArrayList<Object>();
		Query query = new EntityHelper().setQueryParameters(em, sql, null,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			entityList.add(new EntityHelper().setSingleEntity(sql, arrObj, clazz));
		}
		return entityList;
	}
	
	/**
	 * 通过泛型得到实体对象集合
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出
	 * @param clazz 要转成对象的实体
	 * @return 
	 */
	public static <T> List<T> getGenericEntityList(EntityManager em,String sql,Class<?> clazz){
		List<T> entityList = new ArrayList<T>();
		Query query = new EntityHelper().setQueryParameters(em, sql, null,clazz);
		if(query == null){
			return null;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			entityList.add((T)new EntityHelper().setSingleEntity(sql, arrObj, clazz));
		}
		return entityList;
	}
	
	/**
	 * 得到数据的List<Map<String,Object>> 集合
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出,
	 * @param params 参数可以是 Map,Object[],List<Object[]>等这些类型,
	 * @return
	 */
	public static List<Map<String,Object>> getListMap(EntityManager em,String sql,Object params){
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Query query = new EntityHelper().setQueryParameters(em, sql, params);
		if(query == null){
			return Collections.EMPTY_LIST;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			new EntityHelper().setSingleRow(sql, arrObj);
			listMap.add(new EntityHelper().setSingleRow(sql, arrObj));
		}
		return listMap;
	}
	
	/**
	 * 得到数据的List<Map<String,Object>> 集合
	 * @param em EntityManage 对象
	 * @param sql 你要查询的sql语句  注意 : 1.该sql语句暂不支持 select * from 这种查询,所有要的字段必须列出
	 * @return
	 */
	public static List<Map<String,Object>> getListMap(EntityManager em,String sql){
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Query query = new EntityHelper().setQueryParameters(em, sql, null);
		if(query == null){
			return Collections.EMPTY_LIST;
		}
		List<Object[]> columns= query.getResultList();
		for (Object[] arrObj : columns) {
			new EntityHelper().setSingleRow(sql, arrObj);
			listMap.add(new EntityHelper().setSingleRow(sql, arrObj));
		}
		return listMap;
	}
	
	/**
	 * 查看当天日期是否存在,如果存在就删除
	 * @param em EntityManage 对象
	 * @param clazz 对应实体的class
	 * @param columnName 对应实体的统计日期属性
	 * @param columnType 对应实体的统计日期属性的类型
	 */
	public static void deleteCurrentDayExist(EntityManager em,Class<?> clazz,String columnName,Class<?> columnType){
		
		if("java.util.Date".equals(columnType.getName())) {
			String jpql = "select o from "+clazz.getSimpleName()+" o where o."+columnName+" >:currentDateSmall and o."+columnName+" <:currentDateLarge";
			Query query = em.createQuery(jpql);
			Calendar calendarLarge = Calendar.getInstance();
			calendarLarge.add(Calendar.DATE,1);
			Date currentDateLarge = calendarLarge.getTime();
			Calendar calendarSmall = Calendar.getInstance();
			calendarSmall.add(Calendar.DATE,-1);
			Date currentDateSmall = calendarSmall.getTime();
			query.setParameter("currentDateLarge",currentDateLarge);
			query.setParameter("currentDateSmall",currentDateSmall);
			List<?> list = query.getResultList();
			for (Object object : list) {
				em.remove(object);
			}
		}else if("java.lang.String".equals(columnType.getName())){
			String jpql = "select o from "+clazz.getSimpleName()+" o where o."+columnName+" =:currentDate";
			Query query = em.createQuery(jpql);
			query.setParameter("currentDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			List<?> list = query.getResultList();
			for (Object object : list) {
				em.remove(object);
			}
		}
	}
	
	/**
	 * 将值封装到单个实体中
	 * @param sql
	 * @param arrObj
	 * @param clazz
	 * @return
	 */
	private Object setSingleEntity(String sql,Object[] arrObj,Class<?> clazz){
		Object instance = null;
		try {
			instance = clazz.newInstance();
			BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
			beanUtilsBean.populate(instance, this.setSingleRow(sql, arrObj));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	/**
	 * 设置单行值
	 * @return
	 */
	private Map<String,Object> setSingleRow(String sql , Object[] arrObj){
		Map<String,Object> row = new HashMap<String,Object>();
		List<String> columns = this.getColumns(sql);
		for (int i = 0; i < columns.size(); i++) {
			row.put(columns.get(i), arrObj[i]);
		}
		return row;
	}
	
	/**
	 * 设置参数
	 * @param em
	 * @param sql
	 * @param params
	 * @return Query
	 */
	private Query setQueryParameters(EntityManager em,String sql,Object params,Class<?> clazz){
		if(clazz == null){
			System.out.println("实体类型不能为空!!");
			return null;
		}
		if(em == null){
			System.out.println("EntityManager 对象不能为空 !!");
			return null;
		}
		if(StringUtils.isEmpty(sql)){
			System.out.println(" 参数 sql 不能为空 !!");
			return null;
		}
		Query query = em.createNativeQuery(sql);
		if(params != null){
			if(params instanceof List){
				List<Object> listObj = (List<Object>) params;
				for(int i=0;i<listObj.size();i++){
					query.setParameter(i+1, listObj.get(i));
				}
			}
			if(params instanceof Object[]){
				Object[] arrObj = (Object[]) params;
				for (int i = 0; i < arrObj.length; i++) {
					query.setParameter(i+1, arrObj[i]);
				}
			}
			if(params instanceof Map){
				Map<String,Object> map = (Map<String, Object>) params;
				Set<Entry<String,Object>> setEntry = map.entrySet();
				for (Entry<String, Object> entry : setEntry) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		return query;
	}
	
	/**
	 * 设置参数
	 * @param em
	 * @param sql
	 * @param params
	 * @return Query
	 */
	private Query setQueryParameters(EntityManager em,String sql,Object params){
		if(em == null){
			System.out.println("EntityManager 对象不能为空 !!");
			return null;
		}
		if(StringUtils.isEmpty(sql)){
			System.out.println(" 参数 sql 不能为空 !!");
			return null;
		}
		Query query = em.createNativeQuery(sql);
		if(params != null){
			if(params instanceof List){
				List<Object> listObj = (List<Object>) params;
				for(int i=0;i<listObj.size();i++){
					query.setParameter(i+1, listObj.get(i));
				}
			}
			if(params instanceof Object[]){
				Object[] arrObj = (Object[]) params;
				for (int i = 0; i < arrObj.length; i++) {
					query.setParameter(i+1, arrObj[i]);
				}
			}
			if(params instanceof Map){
				Map<String,Object> map = (Map<String, Object>) params;
				Set<Entry<String,Object>> setEntry = map.entrySet();
				for (Entry<String, Object> entry : setEntry) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		return query;
	}
	
	/**
	 * 得到所有列名
	 * @param sql
	 * @return
	 */
	public static List<String> getAllColumns(String sql){
		EntityHelper entityHelper = new EntityHelper();
		return entityHelper.getColumns(sql);
	}
	
	/**
	 * 得到列名
	 * @param sql
	 * @return
	 */
	public List<String> getColumns(String sql){
		StoreNewSql storeNewSql= this.queryParenthesis(sql);
		if(!StringUtils.isEmpty(storeNewSql.getNewSql())){
			sql = storeNewSql.getNewSql();
		}
		if(StringUtils.isEmpty(sql)){
			System.out.println("所传的sql为空!!");
			return Collections.EMPTY_LIST;
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
		StringBuffer fromSql = new StringBuffer();
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
			fromSql = new StringBuffer(recoverFromAfter(storeSql,fromSql.toString()));
		}else{
			fromSql = new StringBuffer(sql);
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
		StringBuffer newSql = new StringBuffer();
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
	
}

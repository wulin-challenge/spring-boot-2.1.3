package cn.wulin.spring.boot.test.jpa.intercepter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.core.RepositoryInformation;

import cn.wulin.spring.boot.test.jpa.convert.RegisterConvertUtil;
import cn.wulin.spring.boot.test.jpa.convert.ValueTypeConvert;
import cn.wulin.spring.boot.test.jpa.intercepter.sql.EntityHelper;

/**
 * 查询返回拦截器
 * @author wulin
 *
 */
public class QueryReturnMethodInterceptor implements MethodInterceptor {
	private ConcurrentHashMap<Method,QueryReturn> queryCache = new ConcurrentHashMap<>();
	
	private RepositoryInformation repositoryInformation;
	
	private EntityManager entityManager;
	
	public QueryReturnMethodInterceptor(EntityManager entityManager,RepositoryInformation repositoryInformation) {
		super();
		this.entityManager = entityManager;
		this.repositoryInformation = repositoryInformation;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object returnValue = null;
		try {
			QueryReturn queryReturn = getQueryReturn(invocation);
			if(queryReturn.getCustomNativeQuery()) {
				returnValue = nativeQuery(invocation, queryReturn);
			}else {
				returnValue = invocation.proceed();
			}
			returnValue = dealwithReturnValue(invocation, returnValue);
		} catch (Throwable e) {
			if(e instanceof ConversionFailedException) {
				ConversionFailedException conversion = (ConversionFailedException)e;
				Object value = conversion.getValue();
				returnValue = dealwithReturnValue(invocation, value);
			}else {
				throw e;
			}
		}
		return returnValue;
	}
	
	private Object nativeQuery(MethodInvocation invocation,QueryReturn queryReturn)throws Throwable {
		javax.persistence.Query query = entityManager.createNativeQuery(queryReturn.getSql());
		
		Object[] arguments = invocation.getArguments();
		
		if(arguments != null) {
			int i = 1;
			for (Object arg : arguments) {
				query.setParameter(i, arg);
			}
		}
		
		List<?> resultList = query.getResultList();
		return resultList;
	}
	
	/**
	 * 处理返回值
	 * @param invocation 调用方法的 MethodInvocation对象
	 * @param returnValue 返回值
	 * @return
	 * @throws Throwable
	 */
	private Object dealwithReturnValue(MethodInvocation invocation,Object returnValue)throws Throwable {
		// 检测目标类型与返回值实际类型是否一致,一致返回true,否则返回false
		if(checkTargetAndReturn(invocation, returnValue)) {
			return returnValue;
		}
		
		Method method = invocation.getMethod();
		Class<?> returnType = method.getReturnType();
		QueryReturn queryReturn = getQueryReturn(invocation);
		
		if(Collection.class.isAssignableFrom(returnType)) {
			Collection<?> returnValueList = (Collection<?>)returnValue;
			if(returnValueList.size() ==0) {
				return returnValue;
			}
			
			List<Object> list = new ArrayList<>();
			for (Object object : returnValueList) {
				Object val = dealwithSingleReturnValue(queryReturn, (Object[])object);
				list.add(val);
			}
			return list;
		}else if(Map.class.isAssignableFrom(returnType)) {
			
			//TODO 预留
		}else {
			return dealwithSingleReturnValue(queryReturn, (Object[])returnValue);
		}
		return null;
	}
	
	/**
	 * 检测目标类型与返回值实际类型是否一致,一致返回true,否则返回false
	 * @return
	 */
	private boolean checkTargetAndReturn(MethodInvocation invocation,Object returnValue) {
		Method method = invocation.getMethod();
		Query query = method.getAnnotation(Query.class);
		
		//若没有 Query 这个注解,直接返回true
		if(query == null) {
			return true;
		}
		
		// 若返回值为空,则不进行任何处理
		if(returnValue == null) {
			return true;
		}
		
		QueryReturn queryReturn = getQueryReturn(invocation);
		
		//当返回类型与实际返回类型不一致时是否进行数据转换,true: 进行转换,false: 不转换
		if(!queryReturn.getConvert()) {
			return true;
		}
		
		Class<?> returnType = method.getReturnType();
		if(Collection.class.isAssignableFrom(returnType)) {
			Collection<?> returnValueList = (Collection<?>)returnValue;
			
			//若返回值为集合,且为空,则不进行任何检测
			if(returnValueList.size() ==0) {
				return true;
			}
			
			for (Object object : returnValueList) {
				if(object.getClass() == queryReturn.getTargetClass()) {
					return true;
				}else {
					return false;
				}
			}
		}else if(Map.class.isAssignableFrom(returnType)) {
			
			//TODO 预留
			return true;
		}else {
			if(returnValue.getClass() == queryReturn.getTargetClass()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 得到查询返回对象
	 * @param invocation
	 * @return
	 */
	private QueryReturn getQueryReturn(MethodInvocation invocation) {
		QueryReturn queryReturn = queryCache.get(invocation.getMethod());
		if(queryReturn != null) {
			return queryReturn;
		}else {
			queryReturn = new QueryReturn();
			Method method = invocation.getMethod();
			Query query = method.getAnnotation(Query.class);
			CustomQuery customQuery = method.getAnnotation(CustomQuery.class);
			
			if(query != null) {
				String sql = query.value();
				queryReturn.setSql(sql);
				queryReturn.setColumns(EntityHelper.getAllColumns(sql));
			}
			
			if(customQuery != null) {
				queryReturn.setColumnSeparator(customQuery.value());
				queryReturn.setCustomNativeQuery(customQuery.customNativeQuery());
				queryReturn.setConvert(customQuery.convert());
			}
			
			queryReturn.setRepositoryInformation(repositoryInformation);
			setTargetClass(invocation, queryReturn);
			
			queryCache.putIfAbsent(invocation.getMethod(), queryReturn);
		}
		return queryReturn;
	}
	
	/**
	 * 设置目标类型class
	 * @param invocation
	 * @param queryReturn
	 */
	private void setTargetClass(MethodInvocation invocation,QueryReturn queryReturn) {
		Method method = invocation.getMethod();
		Class<?> returnType = method.getReturnType();
		if(Collection.class.isAssignableFrom(returnType)) {
			ResolvableType forMethodReturnType = ResolvableType.forMethodReturnType(invocation.getMethod());
			ResolvableType[] generics = forMethodReturnType.getGenerics();
			queryReturn.setTargetClass(generics[0].getRawClass());
			
		}else if(Map.class.isAssignableFrom(returnType)) {
			
			//TODO 预留
		}else {
			queryReturn.setTargetClass(returnType);
		}
	}
	
	/**
	 * 处理单个返回值的情况
	 * @param queryReturn 查询返回定义对象
	 * @param returnValueArray 返回值的数组
	 * @return
	 * @throws Throwable
	 */
	private Object dealwithSingleReturnValue(QueryReturn queryReturn,Object[] returnValueArray) throws Throwable{
		try {
			Object entity = queryReturn.getTargetClass().newInstance();
			List<String> columns = queryReturn.getColumns();
			int i = 0;
			for (String column : columns) {
				
				String[] multiColumnArray = column.split(queryReturn.getColumnSeparator());
				dealwithRecursionFieldValue(returnValueArray, entity, i, multiColumnArray);
				i++;
			}
			return entity;
		}catch (Throwable e) {
			throw e;
		}
	}

	/**
	 * 处理递归属性值的情况
	 * @param returnValueArray 返回值的数组
	 * @param entity 目标实体
	 * @param i 当前属性的索引
	 * @param multiColumnArray 当前 索引列通过分隔符分隔后的列数组
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void dealwithRecursionFieldValue(Object[] returnValueArray, Object entity, int i, String[] multiColumnArray)
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			InstantiationException {
		// 父亲值
		Object parentValue = entity;
		// 目标值,即父亲中 targetColumn 属性所对应的值
		Object targetValue = entity;
		// 父亲属性Field,即父亲中 targetColumn 属性所对应的field
		Field targetField = null;
		
		for(int j=0;j<multiColumnArray.length;j++) {
			String targetColumn = multiColumnArray[j];
			String targetMethodName= targetColumn.substring(0,1).toUpperCase()+targetColumn.substring(1);
			if(j != multiColumnArray.length-1) {
				parentValue = targetValue;
				targetField = parentValue.getClass().getDeclaredField(targetColumn);
				Method getMethod = parentValue.getClass().getMethod("get"+targetMethodName);
				targetValue = getMethod.invoke(parentValue);

				if(targetValue == null) {
					targetValue = targetField.getType().newInstance();
					
					Method setMethod = parentValue.getClass().getMethod("set"+targetMethodName,targetField.getType());
					setMethod.invoke(parentValue,targetValue);
				}
			}else if(j == multiColumnArray.length-1){
				parentValue = targetValue;
				targetField = parentValue.getClass().getDeclaredField(targetColumn);
				targetValue = returnValueArray[i];
				
				//处理转换值
				if(targetValue != null) {
					ValueTypeConvert valueTypeConvert = RegisterConvertUtil.getInstance().getValueTypeConvertRegistry().getValueTypeConvert(targetValue.getClass(), targetField.getType());
					if(valueTypeConvert != null) {
						targetValue = valueTypeConvert.convertValue(targetValue);
					}
				}
				
				Method setMethod = parentValue.getClass().getMethod("set"+targetMethodName,targetField.getType());
				setMethod.invoke(parentValue,targetValue);
			}
		}
	}
}

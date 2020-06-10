package cn.wulin.spring.boot.test.jpa.intercepter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.core.RepositoryInformation;

/**
 * 查询返回定义对象
 * @author wulin
 *
 */
public class QueryReturn {
	
	private String columnSeparator = "__";
	
	private RepositoryInformation repositoryInformation;
	
	/**
	 * 查询sql
	 */
	private String sql;
	
	private List<String> columns = new ArrayList<>();
	
	/**
	 * 目标类,返回对象的class
	 */
	private Class<?> targetClass;
	
	/**
	 * 自定义本地查询
	 */
	private boolean customNativeQuery = false;
	
	/**
	 * 当返回类型与实际返回类型不一致时是否进行数据转换,true: 进行转换,false: 不转换
	 * @return
	 */
	private boolean convert = false;
	
	public String getColumnSeparator() {
		return columnSeparator;
	}

	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public RepositoryInformation getRepositoryInformation() {
		return repositoryInformation;
	}

	public void setRepositoryInformation(RepositoryInformation repositoryInformation) {
		this.repositoryInformation = repositoryInformation;
	}
	
	public boolean getCustomNativeQuery() {
		return customNativeQuery;
	}

	public void setCustomNativeQuery(boolean customNativeQuery) {
		this.customNativeQuery = customNativeQuery;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean getConvert() {
		return convert;
	}

	public void setConvert(boolean convert) {
		this.convert = convert;
	}
}

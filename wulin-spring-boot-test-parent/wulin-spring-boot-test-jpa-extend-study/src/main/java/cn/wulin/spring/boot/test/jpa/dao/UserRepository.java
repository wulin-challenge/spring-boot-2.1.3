package cn.wulin.spring.boot.test.jpa.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.wulin.spring.boot.test.jpa.domain.User;
import cn.wulin.spring.boot.test.jpa.intercepter.CustomQuery;

public interface UserRepository extends JpaRepository<User,Long>{
	
	@Query("select o from User o where o.age = ?1")
	public User findByAge(int age);
	
	@CustomQuery(value="__",convert=true)
	@Query("select o.id,o.age,o.name,r.name as role__name,r.code as role__code from User o left join o.role r where o.age = ?1")
	public User findByAge2(int age);
	
	@Query("select o from User o left join o.role r where o.password = ?1")
	public List<User> findByPassword(String password);
	
	@CustomQuery(value="__",convert=true)
	@Query("select o.id,o.age,r.id as role__id ,r.name as role__name from User o left join o.role r where o.password = ?1")
	public List<User> findByPassword2(String password);
	
	@CustomQuery(value="__",customNativeQuery=true,convert=true)
	@Query(nativeQuery=true,value="select o.id,o.age,o.create_time as createTime,r.id as role__id ,r.name as role__name from jpa_user o left join jpa_role r on o.role_id = r.id where o.password = ?1")
	public List<User> findByPassword3(String password);
	
	@CustomQuery(convert=true,customNativeQuery=true)
	@Query(nativeQuery=true,value="select o.id,o.age,o.create_time as createTime,r.id as role__id ,r.name as role__name from jpa_user o left join jpa_role r on o.role_id = r.id where o.password = ?1")
	public List<Map<String,Object>> findByPassword4(String password);

}

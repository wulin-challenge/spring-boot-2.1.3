package cn.wulin.spring.boot.test.jpa.test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.test.context.junit4.SpringRunner;

import cn.wulin.spring.boot.test.jpa.SpringBootJpaApplication;
import cn.wulin.spring.boot.test.jpa.dao.RoleRepository;
import cn.wulin.spring.boot.test.jpa.dao.UserRepository;
import cn.wulin.spring.boot.test.jpa.domain.Role;
import cn.wulin.spring.boot.test.jpa.domain.User;

@SpringBootTest(classes=SpringBootJpaApplication.class)
@RunWith(SpringRunner.class)
public class TestUser {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void saveUser() {
		
		User user = new User();
		user.setId(1L);
		user.setName("张三");
		user.setPassword("123456789");
		user.setAge(10);
		user.setCreateTime(new Date().getTime());
		
		userRepository.save(user);
		System.out.println();
	}
	
	@Test
	public void saveUserRole() {
		
		Role role = new Role();
		role.setId(2L);
		role.setName("李四角色");
		role.setMemo("角色...");
		role.setCode("lisi_role");
		
		roleRepository.save(role);
		
		User user = new User();
		user.setId(2L);
		user.setName("李四");
		user.setPassword("123456789");
		user.setAge(11);
		user.setCreateTime(new Date().getTime());
		
		user.setRole(role);
		
		userRepository.save(user);
		System.out.println();
	}
	
	@Test
	public void findByAge() {
		User findByAge = userRepository.findByAge(10);
		System.out.println(findByAge);
	}
	
	@Test
	public void findByAge2() {
		System.out.println();
		ConversionService sharedInstance = DefaultConversionService.getSharedInstance();
		
		User findByAge = userRepository.findByAge2(11);
		
		
		System.out.println(findByAge);
	}
	
	@Test
	public void findByPassword() {
		System.out.println();
		
		List<User> findByAge = userRepository.findByPassword("123456789");
		
		
		System.out.println(findByAge);
	}
	@Test
	public void findByPassword2() {
		System.out.println();
		
		List<User> findByAge = userRepository.findByPassword2("123456789");
		
		
		System.out.println(findByAge);
	}
	
	@Test
	public void findByPassword3() {
		System.out.println();
		
		List<User> findByAge = userRepository.findByPassword3("123456789");
		System.out.println(findByAge);
	}
	
	@Test
	public void findByPassword4() {
		System.out.println();
		
		List<Map<String,Object>> findByAge = userRepository.findByPassword4("123456789");
		
//		findByAge.get(0).get("id")
		System.out.println(findByAge);
	}
	
	@Test
	public void findById() {
		System.out.println();
		
		Optional<User> findById = userRepository.findById(2L);
		
		
		System.out.println(findById.get());
	}
}

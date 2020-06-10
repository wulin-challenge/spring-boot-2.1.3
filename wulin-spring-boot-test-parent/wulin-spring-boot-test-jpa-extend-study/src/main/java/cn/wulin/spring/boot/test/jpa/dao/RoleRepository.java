package cn.wulin.spring.boot.test.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.wulin.spring.boot.test.jpa.domain.Role;
import cn.wulin.spring.boot.test.jpa.domain.User;

public interface RoleRepository extends JpaRepository<Role,Long>{
	

}

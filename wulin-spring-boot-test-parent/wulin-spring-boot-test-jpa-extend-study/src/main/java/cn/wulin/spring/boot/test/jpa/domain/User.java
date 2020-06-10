package cn.wulin.spring.boot.test.jpa.domain;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="jpa_user")
public class User {
	
	@Id
	private Long id;
	
	private String name;
	
	private int age;
	
	private String password;
	
	private Long createTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleId",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", password=" + password + ", createTime="
				+ createTime + "]";
	}
	
}

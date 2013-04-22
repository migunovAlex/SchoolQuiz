package com.schoolquiz.entity.admin;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="adminusers")
public class AdminUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name = "USER_PASSWORD")
	private String password;
	
	@OneToMany(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY, mappedBy="adminUser")
	private List<AdminUserSession> adminUserSessions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString(){
		return "id - "+id+"; userName - "+userName;
	}
	

}

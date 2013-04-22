package com.schoolquiz.entity.admin;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="adminusersession")
public class AdminUserSession {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="ADMIN_USER")
	private AdminUser adminUser;
	
	@Column(name="SESSION")
	private String session;
	
	@Column(name="LAST_UPDATE")
	private Long lastActivity;
	
	@Column(name="ACTIVE")
	private Boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Long getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(Long lastActivity) {
		this.lastActivity = lastActivity;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString(){
		return "id - "+id+"; session - "+session+"; lastActivity - "+lastActivity+"; active - "+active+"; sessionUser - "+adminUser;
	}
	

}

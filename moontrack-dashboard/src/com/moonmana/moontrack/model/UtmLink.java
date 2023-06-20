package com.moonmana.moontrack.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "utm_links", schema="public")
public class UtmLink {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private long id;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "link_body")
	private String linkBody;
	
	@Column(name = "is_active")
	private Boolean isActive;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLinkBody() {
		return linkBody;
	}

	public void setLinkBody(String linkBody) {
		this.linkBody = linkBody;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}

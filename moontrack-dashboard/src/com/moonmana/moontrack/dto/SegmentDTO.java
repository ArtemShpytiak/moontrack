package com.moonmana.moontrack.dto;

import java.util.Date;

public class SegmentDTO {

	private int id;
	private String name;
	private Date created = new Date();
	private boolean archived = false;
	private int companyId;
	private FilterExtendedDTO filter;

	int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	Date getCreated() {
		return created;
	}

	void setCreated(Date created) {
		this.created = created;
	}

	boolean isArchived() {
		return archived;
	}

	void setArchived(boolean archived) {
		this.archived = archived;
	}

	int getCompanyId() {
		return companyId;
	}

	void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	FilterExtendedDTO getFilter() {
		return filter;
	}

	void setFilter(FilterExtendedDTO filter) {
		this.filter = filter;
	}
}

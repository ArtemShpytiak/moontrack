package com.moonmana.moontrack.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import hive.model.abtest.AbTest;

@Entity
@Table(name = "games")
public class Game {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private short id;

	@Column(name = "name", length = 127)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "game_id")
	private List<AbTest> abtests = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GameWidget> widgets = new ArrayList<>();

	@Column(name = "creation_date")
	private Date creationDate = new Date();

	@Column(name = "archived", columnDefinition = "boolean default false", nullable = false)
	private boolean archived = false;

	public AbTest getABTestById(int id) {
		return abtests.stream().filter(obj -> obj.getId() == id).findFirst().orElse(null);
	}

	public GameWidget getWidgetById(int id) {
		return widgets.stream().filter(obj -> obj.getId() == id).findFirst().orElse(null);
	}

	public List<AbTest> getActiveAbtests() {
		return abtests.stream().filter(obj -> !obj.isArchived()).collect(Collectors.toCollection(ArrayList::new));
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbTest> getAbtests() {
		return abtests;
	}

	public void setAbtests(List<AbTest> abtests) {
		this.abtests = abtests;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<GameWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<GameWidget> widgets) {
		this.widgets = widgets;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

}

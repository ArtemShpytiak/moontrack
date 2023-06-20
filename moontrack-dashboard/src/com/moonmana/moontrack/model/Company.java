package com.moonmana.moontrack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import hive.model.abtest.AbTest;
import hive.model.segment.Segment;

@Entity
@Table(name = "companies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "name", length = 32)
	private String name;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Game> games = new ArrayList<>();

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> users = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "company_id")
	private List<Segment> segments = new ArrayList<>();

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Widget> widgets = new ArrayList<>();

	public Widget getWidgetById(int id) {
		return widgets.stream().filter(obj -> obj.getId() == id).findFirst().orElse(null);
	}

	public Game getGameById(short id) {
		return getGames().stream().filter(g -> g.getId() == id).findFirst().orElse(null);
	}

	public Segment getSegmentById(int id) {
		return getSegments().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
	}

	public List<AbTest> getAbtests() {
		List<AbTest> abtests = new ArrayList<AbTest>();
		getGames().forEach(game -> {
			abtests.addAll(game.getActiveAbtests());
		});
		return abtests;
	}

	public List<Game> getActiveGames() {
		return games.stream().filter(g -> !g.isArchived()).collect(Collectors.toCollection(ArrayList::new));
	}

	public void addGame(Game game) {
		games.add(game);
		game.setCompany(this);
	}

	public void removeGame(Game game) {
		games.remove(game);
		game.setCompany(null);
	}

	public void addUser(User user) {
		users.add(user);
		user.setCompany(this);
	}

	public void removeUser(User user) {
		users.remove(user);
		user.setCompany(null);
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<Widget> widgets) {
		this.widgets = widgets;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Game> getGames() {
		return games;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	// TODO remove with hibernate soft delete
	public List<Segment> getActiveSegments() {
		return segments.stream().filter(s -> !s.isArchived()).collect(Collectors.toCollection(ArrayList::new));
	}

}

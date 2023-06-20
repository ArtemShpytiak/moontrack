package com.moonmana.moontrack.model.cache;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "game_dashboard_cache")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class GameDashboardCache implements ICache {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "game_id")
	private short gameId;

	@Type(type = "jsonb")
	@Column(name = "cache", columnDefinition = "jsonb")
	private String cache;

	@Column(name = "refreshed")
	private Date refreshDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	@Override
	public Date getRefreshDate() {
		return refreshDate;
	}

	@Override
	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

	public short getGameId() {
		return gameId;
	}

	public void setGameId(short gameId) {
		this.gameId = gameId;
	}
}

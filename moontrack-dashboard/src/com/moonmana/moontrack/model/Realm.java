package com.moonmana.moontrack.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import hive.model.abtest.AbTest;

@Entity
@Table(name = "realms")
public class Realm {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "name", length = 32)
	private String name;

	@ManyToOne
	@JoinColumn(name = "ab_test_id")
	private AbTest abTestId;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game gameId;

}

package com.moonmana.moontrack.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.metric.MetricFactory;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import hive.model.filter.FilterHub;

@Entity
@Table(name = "widgets")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Widget implements Serializable {

	private static final long serialVersionUID = -7915030466130804110L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "filter_id")
	private FilterHub filter;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String cache;

	@Column(name = "metric_type")
	private byte metricType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Transient
	private Metric metric;

	public Widget() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FilterHub getFilter() {
		return filter;
	}

	public void setFilter(FilterHub filter) {
		this.filter = filter;
	}

	public byte getMetricType() {
		return metricType;
	}

	public void setMetricType(byte metricType) {
		this.metricType = metricType;
	}

	public Metric getMetric() {
		return metric;
	}

	public Metric initMetric() {
		metric = MetricFactory.initMetric(metricType, filter);
		metric.setWidgetId(id);
		return metric;
	}

	@Override
	public String toString() {
		return Widget.class.getName() + "[id=" + getId() + "]";
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

}

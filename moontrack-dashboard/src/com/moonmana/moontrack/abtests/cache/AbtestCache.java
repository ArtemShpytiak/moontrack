package com.moonmana.moontrack.abtests.cache;

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
@Table(name = "abtest_cache")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AbtestCache {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "refreshed")
	private Date refreshed;

	@Column(name = "abtest_id")
	private int abtestId;

	@Column(name = "metric_type")
	private byte metricType;

	@Type(type = "jsonb")
	@Column(name = "chart_data", columnDefinition = "jsonb")
	private String chartData;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getRefreshed() {
		return refreshed;
	}

	public void setRefreshed(Date refreshed) {
		this.refreshed = refreshed;
	}

	public int getAbtestId() {
		return abtestId;
	}

	public void setAbtestId(int abtestId) {
		this.abtestId = abtestId;
	}

	public byte getMetricType() {
		return metricType;
	}

	public void setMetricType(byte metricType) {
		this.metricType = metricType;
	}

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public boolean isValid() {
		return refreshed != null && chartData != null;
	}

}

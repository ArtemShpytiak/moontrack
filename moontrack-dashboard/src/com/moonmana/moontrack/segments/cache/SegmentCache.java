package com.moonmana.moontrack.segments.cache;

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
@Table(name = "segment_cache")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SegmentCache {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "refreshed")
	private Date refreshed;

	@Column(name = "segment_id")
	private int segmentId;

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

	public int getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
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
		return refreshed != null;
	}
}

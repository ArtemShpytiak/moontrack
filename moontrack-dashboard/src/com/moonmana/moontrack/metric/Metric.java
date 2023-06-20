package com.moonmana.moontrack.metric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.moonmana.log.Log;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.repositories.DashboardDAO;

import hive.model.filter.FilterHub;

public abstract class Metric {

	protected byte type;

	protected FilterHub filter;
	private List<Metric> subMetrics = new ArrayList<>();
	protected List<Object[]> rows;

	private Chart chart;
	protected int limit = 50; // chart width
	protected int widgetId;

	public Metric() {
	}

	public Metric(FilterHub filter) {
		this.filter = filter;
	}

	public int size() {
		return rows.size();
	}

	public void setRows(List<Object[]> list) {
		this.rows = list;
	}

	public boolean isKeyNull(int index) {
		if (rows.size() <= index) {
			return true;
		}
		return rows.get(index)[0] == null;
	}

	public boolean isValueNull(int index) {
		if (rows.size() <= index) {
			return true;
		}
		return rows.get(index)[1] == null;
	}

	public String getKey(int index) {
		return String.valueOf(rows.get(index)[0]);
	}

	public Object getValue(int index) {
		return rows.get(index)[1];
	}
	
	/**
	 * @param index of the result row
	 * @param defaultValue
	 * @return the value of [1]  in selected row, or
     * {@code defaultValue} if there is no row with {@code index} or if its value is null  
	 */
	@SuppressWarnings("unchecked")
	public <T>T getValueOr(int index, T defaultValue) {
		T result = null;
		if (rows.size() > index) {
			result = ((T)rows.get(index)[1]);
		}
		if (result == null) {
			result = defaultValue;
		}
		return result;
	}

	public void performSubqueries(Session session) {
		for (Metric metric : subMetrics) {
			metric.performQuery(session);
		}
	}

	protected void addSubmetric(Metric metric) {
		subMetrics.add(metric);
	}

	public Chart getChart() {
		if (chart == null) {
			chart = getChartInstance();
		}
		return chart;
	}

	protected abstract Chart getChartInstance();

	public FilterHub getFilter() {
		return filter;
	}

	public void setFilter(FilterHub filter) {
		this.filter = filter;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public void setSubMetrics(List<Metric> subMetrics) {
		this.subMetrics = subMetrics;
	}

	public String getSQL() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void performQuery(Session session) {
		String sql = getSQL();
		Log.out("SQL: " + sql);
		rows = session.createNativeQuery(sql).getResultList();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(int widgetId) {
		this.widgetId = widgetId;
	}

	public String formatDateToDaily(Date date) {
		return DateUtil.getDateFormattedToSqlDaily(date);
	}

	// TODO
	public List<Object[]> getRows() {
		return rows;
	}

	public void performQuery(DashboardDAO dashboardDAO) {
		// TODO Auto-generated method stub
		
	}
}
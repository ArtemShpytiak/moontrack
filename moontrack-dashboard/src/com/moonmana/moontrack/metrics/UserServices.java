package com.moonmana.moontrack.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.moonmana.UserServicesID;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.UserServicesChart;

import hive.model.filter.FilterHub;

public class UserServices extends MetricWithFilters {

	public UserServices(FilterHub filter) {
		super(filter);
		setType(MetricType.USER_SERVICES);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT d_user_registrations.user_services_id," +
					" COUNT(d_user_registrations.user_services_id) as count" +
				" FROM d_user_registrations" +
				getFromTables() +
				" WHERE TRUE" +
					getGameWhereClause() +
					getCountriesWhereClause() +
					getOsesWhereClause() +
					getPlatformsWhereClause() +
					getRegistrationWhereClause() +
					getPayingWhereClause() +
				" GROUP BY d_user_registrations.user_services_id" +
				" ORDER BY count DESC";
		// @formatter:on
	}

	private String getPayingWhereClause() {
		if (filter.getPaying() != null) {
			if (filter.getPaying()) {
				// paying users
				Set<String> range = new HashSet<String>();
				String havingClause = "";
				if (filter.getSpentMoneyFrom() != null || filter.getSpentMoneyTo() != null) {
					havingClause = " GROUP BY iaps.r_user HAVING";
				}
				if (filter.getSpentMoneyFrom() != null) {
					range.add(" SUM(iaps.price) >= " + Float.valueOf(filter.getSpentMoneyFrom()));
				}
				if (filter.getSpentMoneyTo() != null) {
					range.add(" SUM(iaps.price) <= " + Float.valueOf(filter.getSpentMoneyTo()));
				}
				return " AND d_user_registrations.id = realm_users.d_user"
						+ " AND realm_users.id IN (SELECT iaps.r_user FROM iaps WHERE iaps.sandbox = false "
						+ havingClause + String.join(" AND ", new ArrayList<String>(range)) + ")";
			} else {
				// none paying users
				return " AND iaps.r_user IS NULL";
			}
		}
		return "";
	}

	@Override
	protected String getFromTables() {
		Set<String> tables = new HashSet<String>();

		if (!filter.getCountries().isEmpty()) {
			tables.add("a_users");
		}
		if (filter.getPaying() != null) {
			tables.add("realm_users");
		}

		if (!tables.isEmpty()) {
			List<String> list = new ArrayList<String>(tables);
			return ", " + String.join(", ", list);
		}
		return "";
	}

	@Override
	public String getKey(int index) {
		Short osId = (Short) rows.get(index)[0];
		return UserServicesID.getUserServicesName(osId.byteValue());
	}

	@Override
	protected Chart getChartInstance() {
		return new UserServicesChart(this);
	}

}
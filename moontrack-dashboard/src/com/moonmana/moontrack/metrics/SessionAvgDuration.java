package com.moonmana.moontrack.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.SessionAvgDurationChart;

import hive.model.filter.FilterCountry;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;

public class SessionAvgDuration extends MetricWithFilters {

	public SessionAvgDuration(FilterHub filter) {
		super(filter);
		setType(MetricType.SESSION_AVG_DURATION);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', logins.login_date) AS day," +
				" CAST(AVG(sessions.end_date - logins.login_date) as varchar) FROM logins, sessions" +
				getFromTables() +
				" WHERE sessions.login_id = logins.id" +
				getWhereClause() +
				getPayingWhereClause() +
			getDateRange("logins.login_date") +
			" GROUP BY day" +
			" ORDER BY day DESC" +
			" LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	protected String getFromTables() {
		Set<String> tables = new HashSet<String>();

		if (filter.getGameId() != null) {
			tables.add("d_user_registrations");
			tables.add("realm_users");
		}
		if (!filter.getCountries().isEmpty()) {
			tables.add("d_user_registrations");
			tables.add("realm_users");
			tables.add("a_users");
		}
		if (!filter.getOses().isEmpty()) {
			// do nothing
		}
		if (!filter.getPlatforms().isEmpty()) {
			// do nothing
		}
		if (filter.getRegistrationDateFrom() != null || filter.getRegistrationDateTo() != null) {
			tables.add("d_user_registrations");
			tables.add("realm_users");
		}

		if (!tables.isEmpty()) {
			List<String> list = new ArrayList<String>(tables);
			return ", " + String.join(", ", list);
		}
		return "";
	}

	protected String getWhereClause() {
		Set<String> clauses = new HashSet<String>();

		if (filter.getGameId() != null) {
			clauses.add("logins.r_user = realm_users.id");
			clauses.add("realm_users.d_user = d_user_registrations.id");
			clauses.add("d_user_registrations.game = " + filter.getGameId());
		}

		if (!filter.getCountries().isEmpty()) {
			clauses.add("logins.r_user = realm_users.id");
			clauses.add("realm_users.d_user = d_user_registrations.id");
			clauses.add("d_user_registrations.a_user = a_users.id");
			clauses.add(getCountriesWhereClause());
		}
		if (!filter.getOses().isEmpty()) {
			clauses.add(getOsesWhereClause());
		}
		if (!filter.getPlatforms().isEmpty()) {
			clauses.add(getPlatformsWhereClause());
		}
		if (filter.getRegistrationDateFrom() != null || filter.getRegistrationDateTo() != null) {
			clauses.add("logins.r_user = realm_users.id");
			clauses.add("realm_users.d_user = d_user_registrations.id");
			clauses.add("d_user_registrations.registration_date >= '"
					+ DateUtil.getDateFormattedToSqlDaily(filter.getRegistrationDateFrom()) + "'");
			clauses.add("d_user_registrations.registration_date <= '"
					+ DateUtil.getDateFormattedToSqlDaily(filter.getRegistrationDateTo()) + "'");
		}
		if (!clauses.isEmpty()) {
			List<String> list = new ArrayList<String>(clauses);
			return " AND " + String.join(" AND ", list);
		}
		return "";
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
				return " AND logins.r_user IN (SELECT iaps.r_user FROM iaps WHERE iaps.sandbox = false " + havingClause
						+ String.join(" AND ", new ArrayList<String>(range)) + ")";
			} else {
				// none paying users
				return " AND logins.r_user NOT IN (SELECT iaps.r_user FROM iaps)";
			}
		}
		return "";
	}

	@Override
	protected String getCountriesWhereClause() {
		List<FilterCountry> countries = getFilter().getCountries();
		final String param = "a_users.country = ";
		String body = "";

		for (int i = 0; i < countries.size(); i++) {
			body += param + "'" + countries.get(i).getCode() + "'";

			boolean isLast = countries.size() - 1 == i;
			if (!isLast) {
				body += " OR ";
			}
		}
		return " (" + body + ")";
	}

	@Override
	public String getPlatformsWhereClause() {
		List<FilterPlatform> platforms = getFilter().getPlatforms();
		String param = "logins.platform_id = ";
		String body = "";

		for (int i = 0; i < platforms.size(); i++) {
			body += param + Byte.toString(platforms.get(i).getCode());

			boolean isLast = platforms.size() - 1 == i;
			if (!isLast) {
				body += " OR ";
			}
		}
		return " (" + body + ")";
	}

	@Override
	public String getOsesWhereClause() {
		List<FilterOs> oses = getFilter().getOses();
		String param = "logins.os_id = ";
		String body = "";

		for (int i = 0; i < oses.size(); i++) {
			body += param + Byte.toString(oses.get(i).getCode());

			boolean isLast = oses.size() - 1 == i;
			if (!isLast) {
				body += " OR ";
			}
		}
		return " (" + body + ")";
	}

	@Override
	public String getKey(int index) {
		Timestamp date = (Timestamp) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	public Object getValue(int index) {
		LocalTime time = LocalTime.parse(String.valueOf(super.getValue(index)), DateTimeFormatter.ISO_TIME);
		return time.toSecondOfDay();
	}

	@Override
	protected Chart getChartInstance() {
		return new SessionAvgDurationChart(this);
	}

}

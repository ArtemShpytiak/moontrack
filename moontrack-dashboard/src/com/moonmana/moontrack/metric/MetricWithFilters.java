package com.moonmana.moontrack.metric;

import java.util.List;

import com.moonmana.moontrack.common.utils.DateUtil;

import hive.model.filter.FilterCountry;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;

public abstract class MetricWithFilters extends Metric {

	public MetricWithFilters(FilterHub filter) {
		super(filter);
	}

	protected String getDateRange(String param) {
		String result = "";
		if (filter.getDateFrom() != null) {
			result += " AND " + param + " >= '" + DateUtil.getDateFormattedToSqlDaily(filter.getDateFrom()) + "'";
		}
		if (filter.getDateTill() != null) {
			result += " AND " + param + " <= '" + DateUtil.getDateFormattedToSqlDaily(filter.getDateTill()) + "'";
		}
		return result;
	}

	protected String getCountriesWhereClause() {
		if (!filter.getCountries().isEmpty()) {
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
			return " AND d_user_registrations.a_user = a_users.id AND (" + body + ")";

		}
		return "";
	}

	protected String getFromTables() {
		String result = "";
		if (!filter.getCountries().isEmpty()) {
			result += ", a_users";
		}
		return result;
	}

	public String getGameWhereClause() {
		if (getFilter().getGameId() != null && getFilter().getGameId() > 0) {
			return " AND d_user_registrations.game = " + getFilter().getGameId();
		}
		return "";
	}

	public String joinIAPOnDUserRegistration() {
		boolean ranged = getFilter().getSpentMoneyFrom() != null || getFilter().getSpentMoneyTo() != null;
		boolean payingExist = getFilter().isPaying() != null;
		boolean paying = payingExist && getFilter().isPaying().booleanValue();

		if (paying && ranged) {
			// @formatter:off
			return 	  " INNER JOIN realm_users ON realm_users.d_user = d_user_registrations.id"
					+ " INNER JOIN iaps ON iaps.r_user = realm_users.id"
					+ " AND iaps.r_user IN"
					+ " (SELECT DISTINCT R FROM (SELECT iaps.r_user as R, SUM(iaps.price)"
					+ " FROM iaps WHERE iaps.sandbox = FALSE"
					+ " GROUP BY iaps.r_user HAVING SUM(price) >= 0) as RT)";
			// @formatter:on
		} else if (paying) {
			// @formatter:off
			return 	  " INNER JOIN realm_users ON realm_users.d_user = d_user_registrations.id"
					+ " INNER JOIN iaps ON iaps.r_user = realm_users.id"
					+ " AND iaps.sandbox = FALSE";
			// @formatter:on
		} else if (payingExist && !paying) {
			// @formatter:off
			return 	  " INNER JOIN realm_users ON realm_users.d_user = d_user_registrations.id"
					+ " LEFT JOIN iaps ON iaps.r_user = realm_users.id";
			// @formatter:on
		}
		return "";
	}

	public String getNonPayingUsersWhereClause() {
		boolean ranged = getFilter().getSpentMoneyFrom() != null || getFilter().getSpentMoneyTo() != null;
		boolean notPaying = getFilter().isPaying() != null && !getFilter().isPaying().booleanValue() && !ranged;

		if (notPaying) {
			return " AND iaps.r_user IS NULL";
		}
		return "";
	}

	public String getPlatformsWhereClause() {
		List<FilterPlatform> platforms = getFilter().getPlatforms();
		if (!platforms.isEmpty()) {
			String param = "d_user_registrations.platform_id = ";
			String body = "";

			for (int i = 0; i < platforms.size(); i++) {
				body += param + Byte.toString(platforms.get(i).getCode());

				boolean isLast = platforms.size() - 1 == i;
				if (!isLast) {
					body += " OR ";
				}
			}
			return " AND (" + body + ")";
		}
		return "";
	}

	public String getOsesWhereClause() {
		List<FilterOs> oses = getFilter().getOses();
		if (!oses.isEmpty()) {
			String param = "d_user_registrations.os_id = ";
			String body = "";

			for (int i = 0; i < oses.size(); i++) {
				body += param + Byte.toString(oses.get(i).getCode());

				boolean isLast = oses.size() - 1 == i;
				if (!isLast) {
					body += " OR ";
				}
			}
			return " AND (" + body + ")";
		}
		return "";
	}

	public String getRegistrationWhereClause() {
		String result = "";

		if (getFilter().getRegistrationDateFrom() != null) {
			result += " AND d_user_registrations.registration_date >= '"
					+ formatDateToDaily(getFilter().getRegistrationDateFrom()) + "'";
		}
		if (getFilter().getRegistrationDateTo() != null) {
			result += " AND d_user_registrations.registration_date <= '"
					+ formatDateToDaily(getFilter().getRegistrationDateTo()) + "'";
		}
		return result;
	}

}

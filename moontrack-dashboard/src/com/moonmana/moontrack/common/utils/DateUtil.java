package com.moonmana.moontrack.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date getStartDate(String dateFrom) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateFrom.substring(0, dateFrom.indexOf(" - ")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getFinishDate(String dateTo) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateTo.substring(dateTo.indexOf(" - ") + " - ".length()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final DateFormat dayilyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat monthlyDateFormat = new SimpleDateFormat("yyyy-MM");

	public static String getDateFormattedToSqlDaily(Date date) {
		return dayilyDateFormat.format(date);
	}

	public static String getDateFormattedToSqlMonthly(Date date) {
		return monthlyDateFormat.format(date);
	}

}

package com.moonmana.moontrack.metric;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.moonmana.utils.MapBuilder;
import com.moonmana.utils.reflection.ReflectionUtils;

public class MetricType {

	public static final byte NONE = 0;

	public static final byte REVENUE_DAILY = 1;
	public static final byte DAU = 2;
	public static final byte MAU = 3;
	public static final byte REVENUE_MONTHLY = 4;
	public static final byte DPU = 5;
	public static final byte MPU = 6;
	public static final byte ARPPU_DAILY = 7;
	public static final byte ARPPU_MONTHLY = 8;
	public static final byte ARPDAU = 9;
	public static final byte ARPMAU = 10;
	public static final byte LOGINS = 11;
	public static final byte REGISTRATIONS = 12;
	public static final byte OSES = 13;
	public static final byte DEVICES = 14;
	public static final byte PLATFORMS = 15;
	public static final byte USER_SERVICES = 16;
	public static final byte RETENTION_1_DAY = 17;
	public static final byte RETENTION_7_DAYS = 18;
	public static final byte RETENTION_30_DAYS = 19;
	public static final byte CONVERSION = 28;
	public static final byte INSTALLS_TOTAL = 29;
	public static final byte REVENUE_TOTAL = 30;
	public static final byte USERS = 31;
	public static final byte SESSION_AVG_DURATION = 32;
	public static final byte ARPU_DAILY = 33;
	public static final byte INSTALLS_DAILY = 34;

	public static final byte EVENT = 35;

	private static Map<String, Byte> typesByName;
	private static Map<Byte, String> metricsById;

	static {
		typesByName = ReflectionUtils.getStaticConstants(MetricType.class, Byte.class);
		metricsById = MapBuilder.invert(typesByName);
	}

	public static byte getTypeByName(String name) {
		return typesByName.get(name.toUpperCase());
	}

	public static String getMetricName(byte key) {
		return metricsById.get(key);
	}

	public static List<Byte> getAbtestMetricTypes() {
		return Arrays.asList(
		//@formatter:off
				USERS,
				LOGINS,
//				RETENTION_1_DAY,
//				RETENTION_7_DAYS,
//				RETENTION_30_DAYS,
//				SESSION_AVG_DURATION,
				ARPDAU,
//				ARPPU_DAILY,
				REVENUE_DAILY,
				DAU,
				DPU,
//				CONVERSION,
				DEVICES,
				OSES,
				PLATFORMS
//				USER_SERVICES
				);
		//@formatter:on
	}

	public static List<Byte> getSegmentMetricTypes() {
		return Arrays.asList(ARPDAU, DAU, DEVICES, DPU, LOGINS, OSES, PLATFORMS, REVENUE_DAILY);
	}

}

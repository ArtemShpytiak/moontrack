package com.moonmana.moontrack.metric;

import com.moonmana.moontrack.metrics.Arpdau;
import com.moonmana.moontrack.metrics.Arpmau;
import com.moonmana.moontrack.metrics.ArpuDaily;
import com.moonmana.moontrack.metrics.Conversion;
import com.moonmana.moontrack.metrics.ArppuDaily;
import com.moonmana.moontrack.metrics.RevenueDaily;
import com.moonmana.moontrack.metrics.Dau;
import com.moonmana.moontrack.metrics.Devices;
import com.moonmana.moontrack.metrics.Dpu;
import com.moonmana.moontrack.metrics.EventMetric;
import com.moonmana.moontrack.metrics.InstallsTotal;
import com.moonmana.moontrack.metrics.InstallsDaily;
import com.moonmana.moontrack.metrics.Logins;
import com.moonmana.moontrack.metrics.Mau;
import com.moonmana.moontrack.metrics.ArppuMonthly;
import com.moonmana.moontrack.metrics.RevenueMonthly;
import com.moonmana.moontrack.metrics.Mpu;
import com.moonmana.moontrack.metrics.OSes;
import com.moonmana.moontrack.metrics.Platforms;
import com.moonmana.moontrack.metrics.Registrations;
import com.moonmana.moontrack.metrics.Retention;
import com.moonmana.moontrack.metrics.SessionAvgDuration;
import com.moonmana.moontrack.metrics.UserServices;

import hive.model.filter.FilterHub;

public class MetricFactory {

	public static Metric initMetric(byte type, FilterHub filter) {

		switch (type) {
		case MetricType.REVENUE_DAILY:
			return new RevenueDaily(filter);

		case MetricType.REVENUE_MONTHLY:
			return new RevenueMonthly(filter);

		case MetricType.DAU:
			return new Dau(filter);

		case MetricType.MAU:
			return new Mau(filter);

		case MetricType.DPU:
			return new Dpu(filter);

		case MetricType.MPU:
			return new Mpu(filter);

		case MetricType.ARPPU_DAILY:
			return new ArppuDaily(filter);

		case MetricType.ARPPU_MONTHLY:
			return new ArppuMonthly(filter);

		case MetricType.ARPDAU:
			return new Arpdau(filter);

		case MetricType.ARPMAU:
			return new Arpmau(filter);

		case MetricType.LOGINS:
			return new Logins(filter);

		case MetricType.REGISTRATIONS:
			return new Registrations(filter);

		case MetricType.OSES:
			return new OSes(filter);

		case MetricType.DEVICES:
			return new Devices(filter);

		case MetricType.PLATFORMS:
			return new Platforms(filter);

		case MetricType.USER_SERVICES:
			return new UserServices(filter);

		case MetricType.RETENTION_1_DAY:
		case MetricType.RETENTION_7_DAYS:
		case MetricType.RETENTION_30_DAYS:
			return new Retention(filter, type);

		case MetricType.CONVERSION:
			return new Conversion(filter);

		case MetricType.SESSION_AVG_DURATION:
			return new SessionAvgDuration(filter);

		case MetricType.INSTALLS_TOTAL:
			return new InstallsTotal(filter);

		case MetricType.ARPU_DAILY:
			return new ArpuDaily(filter);
		case MetricType.INSTALLS_DAILY:
			return new InstallsDaily(filter);
			
		case MetricType.EVENT:
			return new EventMetric(filter);
		}

		return null;
	}
}

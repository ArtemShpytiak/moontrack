package com.moonmana.moontrack.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.moonmana.OS;
import com.moonmana.PlatformID;
import com.moonmana.moontrack.metric.MetricType;

public class DTOStorage {

	private static Map<Byte, OsDTO> oses;
	private static Map<Byte, PlatformDTO> platforms;
	private static Map<String, CountryDTO> countries;
	private static Map<Byte, MetricDTO> metrics;
	private static Map<Byte, MetricDTO> abtestMetrics;

	static {
		platforms = initPlatforms();
		oses = initOses();
		countries = initCountries();
		metrics = initMetrics();
		abtestMetrics = initAbtestMetrics();
	}

	public static CountryDTO getCountryDTO(String code) {
		return countries.get(code);
	}

	public static OsDTO getOsDTO(byte code) {
		return oses.get(code);
	}

	public static PlatformDTO getPlatformDTO(byte code) {
		return platforms.get(code);
	}

	public static Collection<OsDTO> getOses() {
		List<OsDTO> sorted = new ArrayList<>(oses.values());
		Collections.sort(sorted);
		return sorted;
	}

	public static Collection<CountryDTO> getCountries() {
		List<CountryDTO> sorted = new ArrayList<>(countries.values());
		Collections.sort(sorted);
		return sorted;
	}

	public static Collection<PlatformDTO> getPlatforms() {
		List<PlatformDTO> sorted = new ArrayList<>(platforms.values());
		Collections.sort(sorted);
		return sorted;
	}

	public static Collection<MetricDTO> getMetrics() {
		List<MetricDTO> sorted = new ArrayList<>(metrics.values());
		Collections.sort(sorted);
		return sorted;
	}

	public static Collection<MetricDTO> getAbtestMetrics() {
		return abtestMetrics.values();
	}

	private static Map<Byte, PlatformDTO> initPlatforms() {
		Map<Byte, PlatformDTO> platforms = new HashMap<>();
		Map<Byte, String> map = new HashMap<>();
		map.putAll(PlatformID.getPlatformsById());
		map.remove(PlatformID.NONE);

		for (Map.Entry<Byte, String> entry : map.entrySet()) {
			String name = "";
			String[] arrOfStr = entry.getValue().split("_");
			for (int i = 0; i < arrOfStr.length; i++) {
				name += arrOfStr[i].substring(0, 1) + arrOfStr[i].substring(1).toLowerCase() + " ";
			}
			name = name.trim();
			platforms.put(entry.getKey(), new PlatformDTO(entry.getKey(), name));
		}
		return platforms;
	}

	private static Map<Byte, OsDTO> initOses() {
		Map<Byte, OsDTO> oses = new HashMap<>();
		Map<Byte, String> map = new HashMap<>();
		map.putAll(OS.getOsesById());
		map.remove(OS.NONE);

		for (Map.Entry<Byte, String> entry : map.entrySet()) {
			String name = "";
			String[] arrOfStr = entry.getValue().split("_");
			for (int i = 0; i < arrOfStr.length; i++) {
				name += arrOfStr[i].substring(0, 1) + arrOfStr[i].substring(1).toLowerCase() + " ";
			}
			name = name.trim();
			oses.put(entry.getKey(), new OsDTO(entry.getKey(), name));
		}
		return oses;
	}

	private static Map<String, CountryDTO> initCountries() {
		Map<String, CountryDTO> countries = new HashMap<>();
		Map<String, String> map = Countries.get();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			countries.put(entry.getKey(), new CountryDTO(entry.getKey(), entry.getValue()));
		}
		return countries;
	}

	private static Map<Byte, MetricDTO> initMetrics() {
		Map<Byte, String> mainMetricTitles = new HashMap<Byte, String>();
		mainMetricTitles.put(MetricType.REVENUE_DAILY, "Revenue Daily");
		mainMetricTitles.put(MetricType.DAU, "DAU");
		mainMetricTitles.put(MetricType.MAU, "MAU");
		mainMetricTitles.put(MetricType.REVENUE_MONTHLY, "Revenue Monthly");
		mainMetricTitles.put(MetricType.DPU, "DPU");
		mainMetricTitles.put(MetricType.MPU, "MPU");
		mainMetricTitles.put(MetricType.ARPPU_DAILY, "ARPPU Daily");
		mainMetricTitles.put(MetricType.ARPPU_MONTHLY, "ARPPU Monthly");
		mainMetricTitles.put(MetricType.ARPDAU, "ARPDAU");
		mainMetricTitles.put(MetricType.ARPMAU, "ARPMAU");
		mainMetricTitles.put(MetricType.LOGINS, "Logins");
		mainMetricTitles.put(MetricType.REGISTRATIONS, "Registration");
		mainMetricTitles.put(MetricType.CONVERSION, "Conversion");
		mainMetricTitles.put(MetricType.OSES, "OSes");
		mainMetricTitles.put(MetricType.DEVICES, "Devices");
		mainMetricTitles.put(MetricType.PLATFORMS, "Platforms");
		mainMetricTitles.put(MetricType.USER_SERVICES, "User Services");
		mainMetricTitles.put(MetricType.INSTALLS_DAILY, "Installs");
//		mainMetricTitles.put(MetricType.ARPU_DAILY, "ARPU");
//		mainMetricTitles.put(MetricType.RETENTION_7_DAYS, "Day 1 Retention");
//		mainMetricTitles.put(MetricType.RETENTION_7_DAYS, "Day 7 Retention");
//		mainMetricTitles.put(MetricType.RETENTION_30_DAYS, "Day 30 Retention");
		mainMetricTitles.put(MetricType.SESSION_AVG_DURATION, "Average Session Duration");

		mainMetricTitles.put(MetricType.EVENT, "Event");

		Map<Byte, String> map = new HashMap<>();
		map.putAll(mainMetricTitles);

		Map<Byte, MetricDTO> metrics = new HashMap<>();

		for (Map.Entry<Byte, String> entry : map.entrySet()) {
			metrics.put(entry.getKey(), new MetricDTO(entry.getKey(), entry.getValue()));
		}
		return metrics;
	}

	private static Map<Byte, MetricDTO> initAbtestMetrics() {
		Map<Byte, String> mainMetricTitles = new LinkedHashMap<Byte, String>();
		mainMetricTitles.put(MetricType.USERS, "Platforms");
		mainMetricTitles.put(MetricType.LOGINS, "Logins");
		mainMetricTitles.put(MetricType.ARPDAU, "ARPDAU");
		mainMetricTitles.put(MetricType.REVENUE_DAILY, "Daily Revenue");
		mainMetricTitles.put(MetricType.DAU, "DAU");
//		mainMetricTitles.put(MetricType.MAU, "MAU");
//		mainMetricTitles.put(MetricType.MPU, "MPU");
		mainMetricTitles.put(MetricType.DPU, "DPU");
		mainMetricTitles.put(MetricType.ARPPU_DAILY, "Daily ARPPU");
//		mainMetricTitles.put(MetricType.ARPMAU, "ARPMAU");
//		mainMetricTitles.put(MetricType.REGISTRATIONS, "Registration");
		mainMetricTitles.put(MetricType.DEVICES, "Devices");
		mainMetricTitles.put(MetricType.OSES, "OSes");
		mainMetricTitles.put(MetricType.PLATFORMS, "Platforms");

		Map<Byte, String> map = new HashMap<>();
		map.putAll(mainMetricTitles);

		Map<Byte, MetricDTO> metrics = new HashMap<>();

		for (Map.Entry<Byte, String> entry : map.entrySet()) {
			metrics.put(entry.getKey(), new MetricDTO(entry.getKey(), entry.getValue()));
		}
		return metrics;
	}

}

package com.moonmana.moontrack.dto;

import java.util.Formatter;
import java.util.Locale;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.moonmana.OS;
import com.moonmana.PlatformID;
import com.moonmana.moontrack.metric.MetricType;

public class DashboardCacheDTO {

	private JSONObject cache;

	public DashboardCacheDTO(JSONObject cache) {
		this.cache = cache;
	}

	public int getRegisteredUsersToday() {
		return cache.getInt("registeredUsersToday");
	}

	public double getRevenueToday() {
		return cache.getDouble("revenueToday");
	}

	public String getRevenueTodayFormatted() {
		return String.format("%.0f", getRevenueToday());
	}

	public int getRetention1d() {
		return cache.getInt("retention1d");
	}

	public int getRetention7d() {
		return cache.getInt("retention7d");
	}

	public int getRetention30d() {
		return cache.getInt("retention30d");
	}

	public double getArpdau() {
		return cache.getDouble("arpdau");
	}

	public String getArpdauFormatted() {
		return String.format(Locale.US, "%.2f", getArpdau());
	}

	public int getInstallsTotal() {
		return cache.getInt("installsTotal");
	}

	public int getInstallsIos() {
		return cache.getInt("installsIos");
	}

	public int getInstallsX() {
		return cache.getInt("installsX");
	}

	public int getInstallsAndroid() {
		return cache.getInt("installsAndroid");
	}

	public int getInstallsFb() {
		return cache.getInt("installsFb");
	}

	public int getInstallsWin() {
		return cache.getInt("installsWin");
	}

	public int getInstallsVk() {
		return cache.getInt("installsVk");
	}

	public String getInstallsTotalFormatted() {
		return String.format("%,d", getInstallsTotal());
	}

	public String getInstallsIosFormatted() {
		return String.format("%,d", getInstallsIos());
	}

	public String getInstallsXFormatted() {
		return String.format("%,d", getInstallsX());
	}

	public String getInstallsAndroidFormatted() {
		return String.format("%,d", getInstallsAndroid());
	}

	public String getInstallsFbFormatted() {
		return String.format("%,d", getInstallsFb());
	}

	public String getInstallsWinFormatted() {
		return String.format("%,d", getInstallsWin());
	}

	public String getInstallsVkFormatted() {
		return String.format("%,d", getInstallsVk());
	}

	public double getRevenueTotal() {
		return cache.getInt("revenueTotal");
	}

	public double getRevenueIos() {
		return cache.getInt("revenueIos");
	}

	public double getRevenueX() {
		return cache.getInt("revenueX");
	}

	public double getRevenueAndroid() {
		return cache.getInt("revenueAndroid");
	}

	public double getRevenueFb() {
		return cache.getInt("revenueFb");
	}

	public double getRevenueWin() {
		return cache.getInt("revenueWin");
	}

	public double getRevenueVk() {
		return cache.getInt("revenueVk");
	}

	public int getDauTotal() {
		return cache.getInt("dauTotal");
	}

	public int getDauIos() {
		return cache.getInt("dauIos");
	}

	public int getDauX() {
		return cache.getInt("dauX");
	}

	public int getDauAndroid() {
		return cache.getInt("dauAndroid");
	}

	public int getDauFb() {
		return cache.getInt("dauFb");
	}

	public int getDauWin() {
		return cache.getInt("dauWin");
	}

	public int getDauVk() {
		return cache.getInt("dauVk");
	}

	public JSONObject getCache() {
		return cache;
	}

	public void setCache(JSONObject cache) {
		this.cache = cache;
	}

	public void setRegisteredUsersToday(int registeredUsersToday) {
		cache.put("registeredUsersToday", registeredUsersToday);
	}

	public void setRevenueToday(double revenueToday) {
		cache.put("revenueToday", revenueToday);
	}

	public void setRetention(int retentionType, int value) {
		switch (retentionType) {
		case MetricType.RETENTION_1_DAY:
			setRetention1d(value);
			break;
		case MetricType.RETENTION_7_DAYS:
			setRetention7d(value);
			break;
		case MetricType.RETENTION_30_DAYS:
			setRetention30d(value);
			break;
		}
	}
	
	public void setRetention1d(int retention1d) {
		cache.put("retention1d", retention1d);
	}

	public void setRetention7d(int retention7d) {
		cache.put("retention7d", retention7d);
	}

	public void setRetention30d(int retention30d) {
		cache.put("retention30d", retention30d);
	}

	public void setArpdau(double arpdau) {
		cache.put("arpdau", arpdau);
	}

	public void setInstallsTotal(int installsTotal) {
		cache.put("installsTotal", installsTotal);
	}

	public void setInstallsIos(int installsIos) {
		cache.put("installsIos", installsIos);
	}

	public void setInstallsX(int installsX) {
		cache.put("installsX", installsX);
	}

	public void setInstallsAndroid(int installsAndroid) {
		cache.put("installsAndroid", installsAndroid);
	}

	public void setInstallsFb(int installsFb) {
		cache.put("installsFb", installsFb);
	}

	public void setInstallsWin(int installsWin) {
		cache.put("installsWin", installsWin);
	}

	public void setInstallsVk(int installsVk) {
		cache.put("installsVk", installsVk);
	}

	public void setRevenueTotal(double revenueTotal) {
		cache.put("revenueTotal", revenueTotal);
	}

	public void setRevenueIos(double revenueIos) {
		cache.put("revenueIos", revenueIos);
	}

	public void setRevenueX(double revenueX) {
		cache.put("revenueX", revenueX);
	}

	public void setRevenueAndroid(double revenueAndroid) {
		cache.put("revenueAndroid", revenueAndroid);
	}

	public void setRevenueFb(double revenueFb) {
		cache.put("revenueFb", revenueFb);
	}

	public void setRevenueWin(double revenueWin) {
		cache.put("revenueWin", revenueWin);
	}

	public void setRevenueVk(double revenueVk) {
		cache.put("revenueVk", revenueVk);
	}

	public void setDauTotal(int dauTotal) {
		cache.put("dauTotal", dauTotal);
	}

	public void setDauIos(int dauIos) {
		cache.put("dauIos", dauIos);
	}

	public void setDauX(int dauX) {
		cache.put("dauX", dauX);
	}

	public void setDauAndroid(int dauAndroid) {
		cache.put("dauAndroid", dauAndroid);
	}

	public void setDauFb(int dauFb) {
		cache.put("dauFb", dauFb);
	}

	public void setDauWin(int dauWin) {
		cache.put("dauWin", dauWin);
	}

	public void setDauVk(int dauVk) {
		cache.put("dauVk", dauVk);
	}

	public double getInstallsIosPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsIos() * 100.0 / getInstallsTotal();
	}

	public double getInstallsXPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsX() * 100.0 / getInstallsTotal();
	}

	public double getInstallsAndroidPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsAndroid() * 100.0 / getInstallsTotal();
	}

	public double getInstallsFbPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsFb() * 100.0 / getInstallsTotal();
	}

	public double getInstallsWinPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsWin() * 100.0 / getInstallsTotal();
	}

	public double getInstallsVkPercentage() {
		if (getInstallsTotal() == 0) {
			return 0;
		}
		return getInstallsVk() * 100.0 / getInstallsTotal();
	}

	// revenue

	public double getRevenueIosPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueIos() * 100.0 / getRevenueTotal();
	}

	public double getRevenueXPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueX() * 100.0 / getRevenueTotal();
	}

	public double getRevenueAndroidPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueAndroid() * 100.0 / getRevenueTotal();
	}

	public double getRevenueFbPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueFb() * 100.0 / getRevenueTotal();
	}

	public double getRevenueWinPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueWin() * 100.0 / getRevenueTotal();
	}

	public double getRevenueVkPercentage() {
		if (getRevenueTotal() == 0) {
			return 0;
		}
		return getRevenueVk() * 100.0 / getRevenueTotal();
	}

	// revenue

	public String getRevenueTotalFormatted() {
		return getFormattedDoubleWithThousands(getRevenueTotal());
	}

	String getFormattedDoubleWithThousands(double value) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%(,.2f", value);
		formatter.close();
		return sb.toString();
	}

	public String getRevenueIosFormatted() {
		return getFormattedDoubleWithThousands(getRevenueIos());
	}

	public String getRevenueXFormatted() {
		return getFormattedDoubleWithThousands(getRevenueX());
	}

	public String getRevenueAndroidFormatted() {
		return getFormattedDoubleWithThousands(getRevenueAndroid());
	}

	public String getRevenueFbFormatted() {
		return getFormattedDoubleWithThousands(getRevenueFb());
	}

	public String getRevenueWinFormatted() {
		return getFormattedDoubleWithThousands(getRevenueWin());
	}

	public String getRevenueVkFormatted() {
		return getFormattedDoubleWithThousands(getRevenueVk());
	}

	// dau

	public String formatWithThousands(int value) {
		return String.format("%,d", value);
	}

	public String getDauTotalFormatted() {
		return formatWithThousands(getDauTotal());
	}

	public String getDauIosFormatted() {
		return formatWithThousands(getDauIos());
	}

	public String getDauXFormatted() {
		return formatWithThousands(getDauX());
	}

	public String getDauAndroidFormatted() {
		return formatWithThousands(getDauAndroid());
	}

	public String getDauFbFormatted() {
		return formatWithThousands(getDauFb());
	}

	public String getDauWinFormatted() {
		return formatWithThousands(getDauWin());
	}

	public String getDauVkFormatted() {
		return formatWithThousands(getDauVk());
	}

	// dau

	public double getDauIosPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauIos() * 100.0 / getDauTotal();
	}

	public double getDauXPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauX() * 100.0 / getDauTotal();
	}

	public double getDauAndroidPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauAndroid() * 100.0 / getDauTotal();
	}

	public double getDauFbPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauFb() * 100.0 / getDauTotal();
	}

	public double getDauWinPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauWin() * 100.0 / getDauTotal();
	}

	public double getDauVkPercentage() {
		if (getDauTotal() == 0.0) {
			return 0;
		}
		return getDauVk() * 100.0 / getDauTotal();
	}

	@SuppressWarnings("boxing")
	public void setInstallsByKey(Entry<Byte, Byte> filterKey, Integer value) {
		if (filterKey.getKey() == null) {
			setInstallsTotal(value);
			return;
		}
		switch (filterKey.getKey().byteValue()) {
		case PlatformID.APPLE:
			if (filterKey.getValue().byteValue() == OS.IOS) {
				setInstallsIos(value);
			} else if (filterKey.getValue().byteValue() == OS.MAC_OS) {
				setInstallsX(value);
			}
			break;
		case PlatformID.GOOGLE:
			setInstallsAndroid(value);
			break;
		case PlatformID.FACEBOOK:
			setInstallsFb(value);
			break;
		case PlatformID.WINDOWS_STORE:
			setInstallsWin(value);
			break;
		case PlatformID.VK:
			setInstallsVk(value);
			break;
		}

	}

	@SuppressWarnings("boxing")
	public void setRevenueTotalByKey(Entry<Byte, Byte> filterKey, Double value) {
		if (filterKey.getKey() == null) {
			setRevenueTotal(value);
			return;
		}
		switch (filterKey.getKey().byteValue()) {
		case PlatformID.APPLE:
			if (filterKey.getValue().byteValue() == OS.IOS) {
				setRevenueIos(value);
			} else if (filterKey.getValue().byteValue() == OS.MAC_OS) {
				setRevenueX(value);
			}
			break;
		case PlatformID.GOOGLE:
			setRevenueAndroid(value);
			break;
		case PlatformID.FACEBOOK:
			setRevenueFb(value);
			break;
		case PlatformID.WINDOWS_STORE:
			setRevenueWin(value);
			break;
		case PlatformID.VK:
			setRevenueVk(value);
			break;
		}

	}

	@SuppressWarnings("boxing")
	public void setDauTotalByKey(Entry<Byte, Byte> filterKey, Integer value) {
		if (filterKey.getKey() == null) {
			setDauTotal(value);
			return;
		}
		switch (filterKey.getKey().byteValue()) {
		case PlatformID.APPLE:
			if (filterKey.getValue().byteValue() == OS.IOS) {
				setDauIos(value);
			} else if (filterKey.getValue().byteValue() == OS.MAC_OS) {
				setDauX(value);
			}
			break;
		case PlatformID.GOOGLE:
			setDauAndroid(value);
			break;
		case PlatformID.FACEBOOK:
			setDauFb(value);
			break;
		case PlatformID.WINDOWS_STORE:
			setDauWin(value);
			break;
		case PlatformID.VK:
			setDauVk(value);
			break;
		}
		
		
	}

}

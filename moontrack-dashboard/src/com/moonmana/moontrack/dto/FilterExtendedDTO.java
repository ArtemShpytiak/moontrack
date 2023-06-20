package com.moonmana.moontrack.dto;

import java.util.Date;
import java.util.List;

public class FilterExtendedDTO {
	private int id;
	private short gameId;
	private List<FilterPlatformDTO> platforms;
	private List<FilterOsDTO> oses;
	private List<FilterCountryDTO> countries;
	private List<FilterDeviceDTO> devices;
	private List<FilterRealmDTO> realms;
	private Date registrationDateFrom;
	private Date registrationDateTo;
	private Boolean paying;
	private Float spentMoneyFrom;
	private Float spentMoneyTo;

	int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	short getGameId() {
		return gameId;
	}

	void setGameId(short gameId) {
		this.gameId = gameId;
	}

	List<FilterPlatformDTO> getPlatforms() {
		return platforms;
	}

	void setPlatforms(List<FilterPlatformDTO> platforms) {
		this.platforms = platforms;
	}

	List<FilterOsDTO> getOses() {
		return oses;
	}

	void setOses(List<FilterOsDTO> oses) {
		this.oses = oses;
	}

	List<FilterCountryDTO> getCountries() {
		return countries;
	}

	void setCountries(List<FilterCountryDTO> countries) {
		this.countries = countries;
	}

	List<FilterDeviceDTO> getDevices() {
		return devices;
	}

	void setDevices(List<FilterDeviceDTO> devices) {
		this.devices = devices;
	}

	List<FilterRealmDTO> getRealms() {
		return realms;
	}

	void setRealms(List<FilterRealmDTO> realms) {
		this.realms = realms;
	}

	Date getRegistrationDateFrom() {
		return registrationDateFrom;
	}

	void setRegistrationDateFrom(Date registrationDateFrom) {
		this.registrationDateFrom = registrationDateFrom;
	}

	Date getRegistrationDateTo() {
		return registrationDateTo;
	}

	void setRegistrationDateTo(Date registrationDateTo) {
		this.registrationDateTo = registrationDateTo;
	}

	Boolean getPaying() {
		return paying;
	}

	void setPaying(Boolean paying) {
		this.paying = paying;
	}

	Float getSpentMoneyFrom() {
		return spentMoneyFrom;
	}

	void setSpentMoneyFrom(Float spentMoneyFrom) {
		this.spentMoneyFrom = spentMoneyFrom;
	}

	Float getSpentMoneyTo() {
		return spentMoneyTo;
	}

	void setSpentMoneyTo(Float spentMoneyTo) {
		this.spentMoneyTo = spentMoneyTo;
	}

}

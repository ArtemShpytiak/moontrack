package hive.model.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "filter_hub")
public class FilterHub implements Serializable {

	private static final long serialVersionUID = 5788211106752622138L;

	public FilterHub() {
	}

	public FilterHub(Short gameId) {
		this.gameId = gameId;
	}

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "game_id")
	private Short gameId;

	@Column(name = "event_id")
	private Short eventId;

	@Column(name = "event_name")
	private String eventName;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "platform_filter_id")
	private List<FilterPlatform> platforms = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "os_filter_id")
	private List<FilterOs> oses = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "country_filter_id")
	private List<FilterCountry> countries = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "device_filter_id")
	private List<FilterDevice> devices = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "realm_filter_id")
	private List<FilterRealm> realms = new ArrayList<>();

	@Column(name = "registration_date_from")
	private Date registrationDateFrom;

	@Column(name = "registration_date_to")
	private Date registrationDateTo;

	@Column(name = "date_from")
	private Date dateFrom;

	@Column(name = "date_till")
	private Date dateTill;

	@Column(name = "paying")
	private Boolean paying;

	@Column(name = "spent_money_from")
	private Float spentMoneyFrom;

	@Column(name = "spent_money_to")
	private Float spentMoneyTo;

	@Override
	public String toString() {
		return FilterHub.class.getName() + "[id=" + Objects.toString(getId()) + ", gameId=" + getGameId()
				+ ", registrationDateFrom=" + Objects.toString(registrationDateFrom) + ", registrationDateTo="
				+ Objects.toString(registrationDateTo) + ", paying=" + Objects.toString(paying) + ", spentMoneyFrom="
				+ Objects.toString(spentMoneyFrom) + ", spentMoneyTo=" + Objects.toString(spentMoneyTo) + ", platforms="
				+ getPlatforms().toString() + ", countries=" + getCountries() + ", oses=" + getOses() + ", devices="
				+ getDevices() + ", realms=" + getRealms() + "]";
	}

	public Integer getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Short getEventId() {
		return eventId;
	}

	public void setEventId(Short eventId) {
		this.eventId = eventId;
	}

	public List<FilterPlatform> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<FilterPlatform> platforms) {
		this.platforms = platforms;
	}

	public List<FilterOs> getOses() {
		return oses;
	}

	public void setOses(List<FilterOs> oses) {
		this.oses = oses;
	}

	public List<FilterCountry> getCountries() {
		return countries;
	}

	public void setCountries(List<FilterCountry> countries) {
		this.countries = countries;
	}

	public Date getRegistrationDateFrom() {
		return registrationDateFrom;
	}

	public void setRegistrationDateFrom(Date registrationDateFrom) {
		this.registrationDateFrom = registrationDateFrom;
	}

	public Date getRegistrationDateTo() {
		return registrationDateTo;
	}

	public void setRegistrationDateTo(Date registrationDateTo) {
		this.registrationDateTo = registrationDateTo;
	}

	public Boolean isPaying() {
		return paying;
	}

	public void setPaying(Boolean paying) {
		this.paying = paying;
	}

	public Float getSpentMoneyFrom() {
		return spentMoneyFrom;
	}

	public void setSpentMoneyFrom(Float spentMoneyFrom) {
		this.spentMoneyFrom = spentMoneyFrom;
	}

	public Float getSpentMoneyTo() {
		return spentMoneyTo;
	}

	public void setSpentMoneyTo(Float spentMoneyTo) {
		this.spentMoneyTo = spentMoneyTo;
	}

	public List<FilterDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<FilterDevice> devices) {
		this.devices = devices;
	}

	public List<FilterRealm> getRealms() {
		return realms;
	}

	public void setRealms(List<FilterRealm> realms) {
		this.realms = realms;
	}

	public Short getGameId() {
		return gameId;
	}

	public void setGameId(Short gameId) {
		this.gameId = gameId;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTill() {
		return dateTill;
	}

	public void setDateTill(Date dateTill) {
		this.dateTill = dateTill;
	}

	public Boolean getPaying() {
		return paying;
	}

}

package analytics.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import analytics.model.AClientLoginInfo;

@Entity
@Table(name = "d_user_registrations", uniqueConstraints = @UniqueConstraint(columnNames={"game", "d_user_id"}))
public class DistributionUserRegistration {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;
	
	@Column(name = "game")
	private short game;
	
	@Column(name = "d_user_id")
	private int distributionUserId;
	
//	@Column(name = "platform") do not call column "platform" 
//	private byte platform = (byte) PlatformID.NONE;
	
	@Column(name = "registration_date")
	private Date registrationDate;
	
	@ManyToOne
	@JoinColumn(name = "a_user")
	private AnalyticsUser analyticsUser;

	@Column(name = "user_services_id")
	private Byte userServicesId;
	
	@Embedded
	private AClientLoginInfo loginInfo = AClientLoginInfo.ZERO;
	
	public DistributionUserRegistration() {
		super();
		// for hibernate only. Do not use this constructor
	}

	public DistributionUserRegistration(short game, int distributionUserId, Date registrationDate, byte userServicesId, AClientLoginInfo loginInfo) {
		super();
		this.game = game;
		this.distributionUserId = distributionUserId;
		this.registrationDate = registrationDate;
		this.userServicesId = userServicesId;
		this.loginInfo = loginInfo;
	}

	public int getId() {
		return id;
	}

	public short getGame() {
		return game;
	}
	
	public byte getUserServicesId() {
		return userServicesId;
	}

	public AClientLoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(AClientLoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void setUserServicesId(byte userServicesId) {
		this.userServicesId = userServicesId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setAnalyticsUser(AnalyticsUser analyticsUser) {
		this.analyticsUser = analyticsUser;
	}

	public void setRegistrationDate(Date value) {
		this.registrationDate = value;
	}

	public AnalyticsUser getAnalyticsUser() {
		return analyticsUser;
	}
}

package analytics.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import analytics.model.user.ARealmUser;

@Entity
@Table(name="logins", indexes = {
		@Index(columnList = "r_user", name = "logins_user_id_hidx"),
		@Index(columnList = "login_date", name = "logins_last_login_hidx")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserLogin {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private long id;

	@Column(name = "login_date")
	private Date loginDate;

	@Column(name = "session_length")
	private int sessionLength;
	
	@Column
	private Integer ipv4;
	
	@Column(name = "user_services_id")
	private Byte userServicesId;
//	
//	@Column(name = "os_type")
//	private Byte osType;
//	
//	@Column(name = "os_version")
//	private String osVersion;
//	
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "osVersion", column = @Column(name = "os_version_new")) })
	private AClientLoginInfo loginInfo = AClientLoginInfo.ZERO;

	@ManyToOne
	@JoinColumn(name = "r_user")
	private ARealmUser realmUser;

	@Column(name = "client_version")
	private Integer clientVersion;
	
	public UserLogin() {
		sessionLength = 0;
	}
	
	public UserLogin(ARealmUser realmUser, Date currentDate, byte userServicesId, int ipv4, int version, AClientLoginInfo clientLoginInfo) {
		this.loginDate = currentDate;
		this.realmUser = realmUser;
		this.ipv4 = ipv4;
		this.userServicesId = userServicesId;
		this.clientVersion = version; 
		if (clientLoginInfo != null) {
			loginInfo = clientLoginInfo;
		}
		sessionLength = 0;
	}
	
	public int getSessionLength() {
		return sessionLength;
	}

	public Date getLoginDate() {
		return loginDate;
	}
	
	public int getIpv4() {
		return ipv4;
	}

	public long getId() {
		return id;
	}

	public ARealmUser getRealmUser() {
		return realmUser;
	}

	public AClientLoginInfo getLoginInfo() {
		return loginInfo;
	}
}

package analytics.model.user;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import analytics.model.UserLogin;

@Entity
@Table(name = "realm_users")
public class ARealmUser {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;
	
	@Column(name = "r_user_id")
	private int realmUserId;
	
	@Column(name = "realm_server")
	private short realmId;
	
	@ManyToOne
	@JoinColumn(name = "d_user")
	private DistributionUserRegistration dUser;
	

	@Column(name = "start_date")
	private Date startDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_login")
	private UserLogin lastLogin;
	
	@PrimaryKeyJoinColumn
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	private IncompleteInfo incompleteInfo;
	
	public ARealmUser(int realmUserId, DistributionUserRegistration dUser, short realmId, Date startDate) {
		this.realmUserId = realmUserId;
		this.dUser = dUser;
		this.realmId = realmId;
		this.startDate = startDate;
		
		lastLogin = null;
	}
	
	public DistributionUserRegistration getdUser() {
		return dUser;
	}

	public ARealmUser() {
		super();
		// for hibernate only. Do not use this constructor
	}

	public UserLogin getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(UserLogin lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getRegistrationDate() {
		return dUser.getRegistrationDate();
	}

	public void setStartDate(Date value) {
		startDate = value;
	}

	public void setdUser(DistributionUserRegistration dUser) {
		this.dUser = dUser;
	}

	public void setCompleteInfo(boolean value) {
		incompleteInfo = value ? null : new IncompleteInfo(this);
	}

	public boolean isCompleteInfo() {
		return incompleteInfo == null;
	}
	
	public int getId() {
		return id;
	}
}

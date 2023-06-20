package analytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import analytics.model.user.DistributionUserRegistration;

@Entity
@Table(name = "activities", indexes = {
		@Index(columnList = "d_user", name = "activity_d_user_id_hidx"),
		@Index(columnList = "date", name = "activity_date_hidx")
})
public class Activity {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "d_user")
	private DistributionUserRegistration dUser;
	

	@Column(name = "date")
	private java.sql.Date date = null;

	public Activity() {
		super();
		// for hibernate only. Do not use this constructor
	}

	public int getdUserRegistrationId() {
		return dUser.getId();
	}
	
	public Activity(UserLogin login) {
		this.dUser = login.getRealmUser().getdUser();
		setDate(login.getLoginDate());
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = new java.sql.Date(date.getTime()); 
	}
}

package analytics.model.user;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import core.userIdentification.IUserServiceOwner;
import core.userIdentification.UserServicesContainer;

//TODO under construction
@Entity
@Table(name = "a_users")
public class AnalyticsUser implements IUserServiceOwner {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@Column(name = "country", length = 100)
	private String country;

	@Embedded
	private UserServicesContainer psContainer = new UserServicesContainer(this);

	@Override
	public int getId() {
		return id;
	}

	public UserServicesContainer getPsContainer() {
		return psContainer;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}

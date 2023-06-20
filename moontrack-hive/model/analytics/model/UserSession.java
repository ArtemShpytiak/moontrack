package analytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sessions")
public class UserSession {
	@Id
	@Column(name = "login_id")
	private long loginId;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "num_updates")
	private int numUpdates;
	
	public UserSession() {
	}
	
	public UserSession(long loginId, Date endDate, int numUpdates) {
		this.loginId = loginId;
		this.endDate = endDate;
		this.numUpdates = numUpdates;
	}

	public long getLoginId() {
		return loginId;
	}

	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getUpdatesCount() {
		return numUpdates;
	}

	public void setUpdatesCount(int numUpdates) {
		this.numUpdates = numUpdates;
	}
}

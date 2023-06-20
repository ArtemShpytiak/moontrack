package analytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="installs")
public class AppInstall {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;
	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "game_id")
	private Short gameId;

	@Embedded
	private AClientLoginInfo loginInfo = AClientLoginInfo.ZERO;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setGameId(Short gameId) {
		this.gameId = gameId;
	}

	public void setLoginInfo(AClientLoginInfo loginInfo) {
		this.loginInfo = loginInfo;  
	}
}
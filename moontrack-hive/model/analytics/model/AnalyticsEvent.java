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

import analytics.model.user.ARealmUser;

@Entity
@Table(name = "events", indexes = {
		@Index(columnList = "r_user", name = "user_id_hidx"),
		@Index(columnList = "type", name = "type_hidx"),
		@Index(columnList = "date", name = "date_hidx")
})
public class AnalyticsEvent {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "r_user")
	private ARealmUser realmUser;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "value")
	private int value;
	
	@Column(name = "date")
	private Date date;

	public AnalyticsEvent() { 
		
	}
	
	public AnalyticsEvent(int type, int value, Date date, ARealmUser realmUser) {
		this.type = type;
		this.value = value;
		this.date = date;
		this.realmUser = realmUser;
	}

	public long getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public Date getDate() {
		return date;
	}

}

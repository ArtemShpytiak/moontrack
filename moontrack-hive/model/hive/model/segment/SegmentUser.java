package hive.model.segment;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import analytics.model.user.ARealmUser;

@Entity
@Table(name = "segment_users")
public class SegmentUser {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", nullable = false)
	private int id;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "segment_id", referencedColumnName = "id")
	private Segment segment;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "realm_user", referencedColumnName = "id")
	private ARealmUser realmUser;

	@Column(name = "created")
	private java.sql.Date created = new java.sql.Date(new Date().getTime());

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	public ARealmUser getRealmUser() {
		return realmUser;
	}

	public void setRealmUser(ARealmUser realmUser) {
		this.realmUser = realmUser;
	}
}

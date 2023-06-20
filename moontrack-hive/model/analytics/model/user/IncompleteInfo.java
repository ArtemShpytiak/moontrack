package analytics.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "incomplete_info")
public class IncompleteInfo {
	@Id
	@Column
	private int id;
	
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner")
	private ARealmUser owner;
		
	public IncompleteInfo(ARealmUser aRealmUser) {
		owner = aRealmUser;
	}
	
	/**
	 * Hibernate purposes only
	 */
	public IncompleteInfo() {
		
	}

	
}

package hive.model.segment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import core.hibernate.idGenerator.Identifiable;
import hive.model.filter.FilterHub;

@Entity
@Table(name = "segments")
public class Segment implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 6767388875789293279L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "name", length = 32)
	private String name;

	@Column(name = "created")
	private Date created = new Date();

	@Column(name = "archived", columnDefinition = "boolean default false", nullable = false)
	private boolean archived = false;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "filter_id")
	private FilterHub filter;

	@Column(name = "company_id")
	private int companyId;

	@Override
	public String toString() {
		return Segment.class.toString() + "[id=" + getId() + ", name=" + getName() + ", created=" + getCreated()
				+ ", archived=" + isArchived() + ", companyId=" + companyId + ", filter=" + String.valueOf(getFilter())
				+ "]";
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public FilterHub getFilter() {
		return filter;
	}

	public void setFilter(FilterHub filter) {
		this.filter = filter;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

}

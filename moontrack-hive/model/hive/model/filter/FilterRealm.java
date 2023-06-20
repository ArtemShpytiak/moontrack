package hive.model.filter;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import core.hibernate.idGenerator.Identifiable;

@Entity
@Table(name = "filter_realms")
public class FilterRealm implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 6170681997774729628L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "realm")
	private int realm;

	public FilterRealm() {
	}

	public FilterRealm(int realm) {
		this.realm = realm;
	}

	@Override
	public String toString() {
		return FilterRealm.class.getName() + "[id=" + Objects.toString(getId()) + ", realm=" + getRealm() + "]";
	}

	public int getRealm() {
		return realm;
	}

	public void setRealm(int realm) {
		this.realm = realm;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

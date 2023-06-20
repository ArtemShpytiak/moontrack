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
@Table(name = "filter_platforms")
public class FilterPlatform implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 3574570259985506420L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "code")
	private byte code;

	public FilterPlatform() {
	}

	public FilterPlatform(byte code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return FilterPlatform.class.getName() + "[id=" + Objects.toString(getId()) + ", code=" + getCode() + "]";
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}
}

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
@Table(name = "filter_devices")
public class FilterDevice implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 5887276480826082631L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	public FilterDevice() {
	}

	public FilterDevice(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return FilterDevice.class.getName() + "[id=" + Objects.toString(getId()) + ", name=" + getName() + "]";
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

}
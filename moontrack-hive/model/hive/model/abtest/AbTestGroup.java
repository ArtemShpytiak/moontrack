package hive.model.abtest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import core.hibernate.idGenerator.Identifiable;

@Entity
@Table(name = "abtest_groups")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "group_type")
public abstract class AbTestGroup implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = -7507462418337301916L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "group_size")
	private int groupSize;

	@Column(name = "name", length = 32)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "abtest_id", nullable = false)
	private AbTest abtest;

	@Override
	public String toString() {
		return AbTestGroup.class.getName() + "[id=" + getId() + ", name=" + getName() + ", abtestId="
				+ getAbtest().getId() + "]";
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AbTest getAbtest() {
		return abtest;
	}

	public void setAbtest(AbTest abtest) {
		this.abtest = abtest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

}

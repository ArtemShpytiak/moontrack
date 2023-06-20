package hive.model.abtest;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import core.hibernate.idGenerator.Identifiable;

@Entity
@Table(name = "abtests")
public class AbTest implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = -6721028220925010427L;

	@Id
	@GenericGenerator(name = "assigned-identity", strategy = "core.hibernate.idGenerator.AssignedIdentityGenerator")
	@GeneratedValue(generator = "assigned-identity", strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "name", length = 32)
	private String name;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "finish_date")
	private Date finishDate;

	@Column(name = "created")
	private Date created = new Date();

	@OneToMany(mappedBy = "abtest", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<AbTestGroup> groups = new ArrayList<>();

	@Column(name = "archived", columnDefinition = "boolean default false", nullable = false)
	private boolean archived = false;

	@Column(name = "game_id")
	private short gameId;

	@Override
	public String toString() {
		return AbTest.class.getName() + "[id=" + getId() + ", name=" + getName() + ", startDate=" + getStartDate()
				+ ", finishDate=" + getFinishDate() + ", created=" + getCreated() + ", archived=" + isArchived()
				+ ", gameId=" + getGameId() + ", groups=" + groups + "]";
	}

	public AbTestGroup getGroupById(int id) {
		return groups.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
	}

	public String getDateRangeFormatted() {
		return getDateFormatted(getStartDate()) + " - " + getDateFormatted(getFinishDate());
	}

	public String getStartDateFormatted() {
		return getDateFormatted(getStartDate());
	}

	public String getFinishDateFormatted() {
		return getDateFormatted(getFinishDate());
	}

	private String getDateFormatted(Date date) {
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
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

	public short getGameId() {
		return gameId;
	}

	public void setGameId(short gameId) {
		this.gameId = gameId;
	}

	public List<AbTestGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<AbTestGroup> groups) {
		this.groups = groups;
	}

}

package hive.model.abtest;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import hive.model.filter.FilterHub;

@Entity
@DiscriminatorValue("F")
public class AbTestGroupByFilter extends AbTestGroup {

	private static final long serialVersionUID = -3861656030331891032L;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "filter_id")
	private FilterHub groupByFilter;

	public FilterHub getFilter() {
		return groupByFilter;
	}

	public void seFilter(FilterHub groupByFilter) {
		this.groupByFilter = groupByFilter;
	}

	@Override
	public String toString() {
		return AbTestGroupByFilter.class.getName() + " [super=" + super.toString() + ", groupByFilter=" + getFilter()
				+ "]";
	}
}

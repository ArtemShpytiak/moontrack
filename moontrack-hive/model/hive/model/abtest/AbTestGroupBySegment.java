package hive.model.abtest;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import hive.model.segment.Segment;

@Entity
@DiscriminatorValue("S")
public class AbTestGroupBySegment extends AbTestGroup {

	private static final long serialVersionUID = 2172703949592617692L;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "segment_id")
	private Segment segment;

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}
}

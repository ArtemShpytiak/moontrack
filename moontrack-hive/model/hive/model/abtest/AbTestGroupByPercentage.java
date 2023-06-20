package hive.model.abtest;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("P")
public class AbTestGroupByPercentage extends AbTestGroup {

	private static final long serialVersionUID = 8297596062811222410L;

	@Column(name = "percent")
	private float percent;

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public int calculateGroupSize(float usersAmount) {
		return (int) (usersAmount * percent / 100.0);
	}

	@Override
	public String toString() {
		return AbTestGroupByPercentage.class.getName() + "[super=" + super.toString() + ", percent=" + percent + "]";
	}

}

package app.NetWorth.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "targets")
public class NetWorthTarget {

	@Id
	private String id;

	private String belongsToUserId;
	private String targetType;
	private int fixedValue;
	private int initialValue;
	private float increaseRate;
	private int interval;
	private Date targetCreatedOn;

	public NetWorthTarget() {

	}

	public NetWorthTarget(String belongsToUserId, String targetType, int fixedValue, int initialValue,
			float increaseRate, int interval, Date targetCreatedOn) {
		this.belongsToUserId = belongsToUserId;
		this.targetType = targetType;
		this.fixedValue = fixedValue;
		this.initialValue = initialValue;
		this.increaseRate = increaseRate;
		this.interval = interval;
		this.targetCreatedOn = targetCreatedOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBelongsToUserId() {
		return belongsToUserId;
	}

	public void setBelongsToUserId(String belongsToUserId) {
		this.belongsToUserId = belongsToUserId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public int getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(int fixedValue) {
		this.fixedValue = fixedValue;
	}

	public int getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(int initialValue) {
		this.initialValue = initialValue;
	}

	public float getIncreaseRate() {
		return increaseRate;
	}

	public void setIncreaseRate(float increaseRate) {
		this.increaseRate = increaseRate;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Date getTargetCreatedOn() {
		return targetCreatedOn;
	}

	public void setTargetCreatedOn(Date targetCreatedOn) {
		this.targetCreatedOn = targetCreatedOn;
	}

	@Override
	public String toString() {
		return "NetWorthTarget [id=" + id + ", belongsToUserId=" + belongsToUserId + ", targetType=" + targetType
				+ ", fixedValue=" + fixedValue + ", initialValue=" + initialValue + ", increaseRate=" + increaseRate
				+ ", interval=" + interval + ", targetCreatedOn=" + targetCreatedOn + "]";
	}

}

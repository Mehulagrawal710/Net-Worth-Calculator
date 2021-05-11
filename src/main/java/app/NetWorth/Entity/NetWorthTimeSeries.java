package app.NetWorth.Entity;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timedata")
public class NetWorthTimeSeries {

	@Id
	private String id;

	private String belongsToUserId;
	private ArrayList<Date> timeCheckPoint;
	private ArrayList<Integer> dataCheckPoint;

	public NetWorthTimeSeries() {

	}

	public NetWorthTimeSeries(String belongsToUserId, ArrayList<Date> timeCheckPoint,
			ArrayList<Integer> dataCheckPoint) {
		this.belongsToUserId = belongsToUserId;
		this.timeCheckPoint = timeCheckPoint;
		this.dataCheckPoint = dataCheckPoint;
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

	public ArrayList<Date> getTimeCheckPoint() {
		return timeCheckPoint;
	}

	public void setTimeCheckPoint(ArrayList<Date> timeCheckPoint) {
		this.timeCheckPoint = timeCheckPoint;
	}

	public ArrayList<Integer> getDataCheckPoint() {
		return dataCheckPoint;
	}

	public void setDataCheckPoint(ArrayList<Integer> dataCheckPoint) {
		this.dataCheckPoint = dataCheckPoint;
	}

	@Override
	public String toString() {
		return "NetWorthTimeSeries [id=" + id + ", belongsToUserId=" + belongsToUserId + ", timeCheckPoint="
				+ timeCheckPoint + ", dataCheckPoint=" + dataCheckPoint + "]";
	}

}

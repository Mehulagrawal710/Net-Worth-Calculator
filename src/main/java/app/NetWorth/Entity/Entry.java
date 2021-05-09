package app.NetWorth.Entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entries")
public class Entry {

	@Id
	private String id;

	private String belongsToSectionId;
	private String title;
	private int amount;

	@CreatedDate
	private Date createdOn;

	public Entry() {
	}

	public Entry(String belongsToSectionId, String title, int amount, Date createdOn) {
		this.belongsToSectionId = belongsToSectionId;
		this.title = title;
		this.amount = amount;
		this.createdOn = createdOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBelongsToSectionId() {
		return belongsToSectionId;
	}

	public void setBelongsToSectionId(String belongsToSectionId) {
		this.belongsToSectionId = belongsToSectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "Entry [id=" + id + ", belongsToSectionId=" + belongsToSectionId + ", title=" + title + ", amount="
				+ amount + ", createdOn=" + createdOn + "]";
	}

}

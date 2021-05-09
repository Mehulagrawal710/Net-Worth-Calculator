package app.NetWorth.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sections")
public class Section {

	@Id
	private String id;

	private String title;
	private String type;
	private int totalAmount;
	private String belongsToUserId;
	private Boolean isDefault;

	public Section() {
	}

	public Section(String title, String type, int totalAmount, String belongsToUserId, Boolean isDefault) {
		this.title = title;
		this.type = type;
		this.totalAmount = totalAmount;
		this.belongsToUserId = belongsToUserId;
		this.isDefault = isDefault;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBelongsToUserId() {
		return belongsToUserId;
	}

	public void setBelongsToUserId(String belongsToUserId) {
		this.belongsToUserId = belongsToUserId;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "Section [id=" + id + ", title=" + title + ", type=" + type + ", totalAmount=" + totalAmount
				+ ", belongsToUserId=" + belongsToUserId + ", isDefault=" + isDefault + "]";
	}

}

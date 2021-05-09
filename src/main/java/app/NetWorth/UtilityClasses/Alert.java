package app.NetWorth.UtilityClasses;

public class Alert {

	private String msg;
	private String bootstrapClass;

	public Alert(String msg, String bootstrapClass) {
		this.msg = msg;
		this.bootstrapClass = bootstrapClass;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBootstrapClass() {
		return bootstrapClass;
	}

	public void setBootstrapClass(String bootstrapClass) {
		this.bootstrapClass = bootstrapClass;
	}

	@Override
	public String toString() {
		return "Alert [msg=" + msg + ", bootstrapClass=" + bootstrapClass + "]";
	}

}

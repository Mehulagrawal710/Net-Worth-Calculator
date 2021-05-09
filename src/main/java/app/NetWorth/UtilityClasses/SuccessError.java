package app.NetWorth.UtilityClasses;

public class SuccessError {

	private String msg;

	public SuccessError(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SuccessError [msg=" + msg + "]";
	}

}
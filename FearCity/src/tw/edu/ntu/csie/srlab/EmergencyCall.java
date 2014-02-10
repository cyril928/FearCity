package tw.edu.ntu.csie.srlab;

public class EmergencyCall {
	private String index;
	private String GroupName;
	private String sender;
	private String msgBody;
	private String time;
	public String getGroupName() {
		return  GroupName;
	}

	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}
	public String getindex() {
		return index;
	}

	public void setindex(String index) {
		this.index = index;
	}
	public String getsender() {
		return sender;
	}
	public void setsender(String sender) {
		this.sender = sender;
	}
	public String getmsgBody() {
		return msgBody;
	}
	public void setmsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String gettime() {
		return time;
	}
	public void settime(String time) {
		this.time = time;
	}
 
}

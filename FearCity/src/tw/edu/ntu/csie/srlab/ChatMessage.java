package tw.edu.ntu.csie.srlab;

public class ChatMessage {
	private String msgBody,sender,time;
	public ChatMessage(String msgBody,String time ,String sender){
		this.msgBody = msgBody;
		this.time = time;
		this.sender = sender;
	}
	public ChatMessage() {
		// TODO Auto-generated constructor stub
	}
	public void setmsgBody(String msgBody ){
		this.msgBody = msgBody;
	}
	public String getmsgBody( ){
		return  this.msgBody;
	}
	public void settime(String time ){
		this.time = time;
	}
	public String gettime( ){
		return  this.time;
	}
	public void setsender(String sender ){
		this.sender = sender;
	}
	public String getsender( ){
		return  this.sender;
	}
}

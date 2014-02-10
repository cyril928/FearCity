package tw.edu.ntu.csie.srlab.packet;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.User;

 

public class IQQueryGroupMember extends IQ {
	public static String NAME = "query";
	String GroupName ;
	public static String NAMESPACE = "srlab:group:iq:queryGroupMember";
	

	private ArrayList<User> users ;
	public void  setusers(ArrayList<User>  user){
		this.users=user;
	}
	public ArrayList<User>   getusers( ){
		return users;
	}
	public IQQueryGroupMember(String GroupName){
		this.GroupName=GroupName;
		
	}
 
 
	public IQQueryGroupMember() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("<GroupName>"+GroupName+"</GroupName>"); 
//		buffer.append("<Description>"+GroupName+"</Description>");  
//		buffer.append("<StartTime>"+StartTime+"</StartTime>");  
//		buffer.append("<EndTime>"+EndTime+"</EndTime>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

package tw.edu.ntu.csie.srlab.packet;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.User;

 

public class IQQueryGroupMemberLocation extends IQ {
	public static String NAME = "query";
	private String UserName=null ;
	public static String NAMESPACE = "srlab:group:iq:queryGroupMemberLocation";
	

	private ArrayList<User> users;
	public void  setusers(ArrayList<User>  user){
		this.users=user;
	}
	public ArrayList<User>   getusers( ){
		return users;
	}
 
 
 
	public IQQueryGroupMemberLocation() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		if(UserName!=null){buffer.append("<UserName>"+UserName+"</UserName>");} 
//		buffer.append("<Description>"+GroupName+"</Description>");  
//		buffer.append("<StartTime>"+StartTime+"</StartTime>");  
//		buffer.append("<EndTime>"+EndTime+"</EndTime>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

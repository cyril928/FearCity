package tw.edu.ntu.csie.srlab.packet;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.GroupList;

public class IQQueryGroup extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:queryGroup";
	boolean isJustName=false;

	private ArrayList<GroupList> groups;
	public void  setgroup(ArrayList<GroupList>  groups){
		this.groups=groups;
	}
	public ArrayList<GroupList>   getgroup(){
		return groups;
	}
	public void  setisJustName(boolean isJustName){
		this.isJustName=isJustName;
	}
 
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		if(isJustName){
			buffer.append("<isJustName>"+true+"</isJustName>"); 
		}else{
			buffer.append("<isJustName>"+false+"</isJustName>"); 
		}
//		buffer.append("<GroupName>"+GroupName+"</GroupName>"); 
//		buffer.append("<Description>"+GroupName+"</Description>");  
//		buffer.append("<StartTime>"+StartTime+"</StartTime>");  
//		buffer.append("<EndTime>"+EndTime+"</EndTime>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

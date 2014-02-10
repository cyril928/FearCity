package tw.edu.ntu.csie.srlab.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQCreateGroup extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:createGroup";
	

	private String GroupName;
	private String StartTime;
	private String EndTime;
	private String Description;
	public IQCreateGroup(String GroupName, String Description,String StartTime, String EndTime){
		this.GroupName = GroupName;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
		this.Description=Description;
	}

	
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("<GroupName>"+GroupName+"</GroupName>");  
		buffer.append("<Description>"+Description+"</Description>");  
		buffer.append("<StartTime>"+StartTime+"</StartTime>");  
		buffer.append("<EndTime>"+EndTime+"</EndTime>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

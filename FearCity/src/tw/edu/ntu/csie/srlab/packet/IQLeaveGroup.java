package tw.edu.ntu.csie.srlab.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQLeaveGroup extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:leaveGroup";
	

	private String GroupName;
 
	public IQLeaveGroup(String GroupName ){
		this.GroupName = GroupName;
	}
  
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}
	
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("<GroupName>"+GroupName+"</GroupName>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

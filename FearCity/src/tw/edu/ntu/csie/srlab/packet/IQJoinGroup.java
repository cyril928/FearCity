package tw.edu.ntu.csie.srlab.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQJoinGroup extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:joinGroup";
	

	private String GroupName;
 
	public IQJoinGroup(String GroupName ){
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

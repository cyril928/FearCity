package tw.edu.ntu.csie.srlab.packet;

import org.jivesoftware.smack.packet.IQ;

import android.util.Log;

public class IQEmergencyCall extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:EmergencyCall";
	

	private String msgBody;
 
	public IQEmergencyCall(String msgBody ){
		this.msgBody=msgBody;
	}
  
 
	
	@Override
	public String getChildElementXML() {//Log.d("TAG","123");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("<msgBody>"+msgBody+"</msgBody>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

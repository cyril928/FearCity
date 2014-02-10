package tw.edu.ntu.csie.srlab.packet;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.EmergencyCall;

import android.util.Log;

public class IQQueryEmergencyRecordCall extends IQ {
	public static String NAME = "query";
	
	public static String NAMESPACE = "srlab:group:iq:EmergencyCallrecord";
	private ArrayList<EmergencyCall> call;
 
 
	public void setcall( ArrayList<EmergencyCall> call){
		this.call=call;
	}
 
	public  ArrayList<EmergencyCall> getcall(){
		return call;
	}
	@Override
	public String getChildElementXML() {//Log.d("TAG","123");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}

package tw.edu.ntu.csie.srlab.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

 
import tw.edu.ntu.csie.srlab.EmergencyCall;
import tw.edu.ntu.csie.srlab.User;
 
import tw.edu.ntu.csie.srlab.packet.IQQueryEmergencyRecordCall;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;

import android.util.Log;


public class QueryEmergencyRecordCallProvider implements IQProvider {
	
	@Override
	public IQ parseIQ(XmlPullParser arg0) throws Exception {
 

		boolean done = false;

		IQQueryEmergencyRecordCall iq=new IQQueryEmergencyRecordCall(); 
		Log.d("TAG", "QueryEmergencyRecordCallProvider was been used.");
		ArrayList<EmergencyCall> calllist=new ArrayList<EmergencyCall>();
		EmergencyCall call = null;
		while (!done) {
			int eventType = arg0.next();	
			if (eventType == XmlPullParser.START_TAG) {
//				Log.d("TAG", "arg0.nextText()"+arg0.nextText());
				if (arg0.getName().equals("call")) {
					
					call=new EmergencyCall(); 
					 
				} else if (arg0.getName().equals("GroupName")) {
					
					call.setGroupName(arg0.nextText());
					 
				}else if (arg0.getName().equals("index")) {
					
					call.setindex(arg0.nextText());
					
				} else if (arg0.getName().equals("sender")) {
					
					call.setsender(arg0.nextText());
					
				}  else if (arg0.getName().equals("msgBody")) {
					
					call.setmsgBody(arg0.nextText());
				
				}  else if (arg0.getName().equals("time")) {
					String str=arg0.nextText();
					call.settime(str.substring(0,str.length()-4));
				
				} 
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (arg0.getName().equals("query")) {
					done = true;
					iq.setcall(calllist);
				} else if(arg0.getName().equals("call")){
					calllist.add(call);
				}
				         
			}
		}

		Log.d("TAG", "provider creates iq: " + iq.toXML());

		return iq;
	}

}
